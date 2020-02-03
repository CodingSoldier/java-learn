// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package hawkular

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/http/httptest"
	"net/url"
	"strconv"
	"strings"
	"sync"
	"testing"
	"time"

	"github.com/hawkular/hawkular-client-go/metrics"
	"k8s.io/heapster/metrics/core"

	assert "github.com/stretchr/testify/require"
)

func dummySink() *hawkularSink {
	return &hawkularSink{
		expReg: make(map[string]*expiringItem),
		models: make(map[string]*metrics.MetricDefinition),
	}
}

func TestDescriptorTransform(t *testing.T) {

	hSink := dummySink()

	ld := core.LabelDescriptor{
		Key:         "k1",
		Description: "d1",
	}
	smd := core.MetricDescriptor{
		Name:      "test/metric/1",
		Units:     core.UnitsBytes,
		ValueType: core.ValueInt64,
		Type:      core.MetricGauge,
		Labels:    []core.LabelDescriptor{ld},
	}

	md := hSink.descriptorToDefinition(&smd)

	assert.Equal(t, smd.Name, md.ID)
	assert.Equal(t, 3, len(md.Tags)) // descriptorTag, unitsTag, typesTag, k1

	assert.Equal(t, smd.Units.String(), md.Tags[unitsTag])
	assert.Equal(t, "d1", md.Tags["k1_description"])

	smd.Type = core.MetricCumulative

	md = hSink.descriptorToDefinition(&smd)
	assert.EqualValues(t, md.Type, metrics.Counter)
}

func TestMetricTransform(t *testing.T) {
	hSink := dummySink()

	l := make(map[string]string)
	l["spooky"] = "notvisible"
	l[core.LabelHostname.Key] = "localhost"
	l[core.LabelHostID.Key] = "localhost"
	l[core.LabelContainerName.Key] = "docker"
	l[core.LabelPodId.Key] = "aaaa-bbbb-cccc-dddd"
	l[core.LabelNodename.Key] = "myNode"

	metricName := "test/metric/1"
	labeledMetricNameA := "test/labeledmetric/A"
	labeledMetricNameB := "test/labeledmetric/B"

	metricSet := core.MetricSet{
		Labels: l,
		MetricValues: map[string]core.MetricValue{
			metricName: {
				ValueType:  core.ValueInt64,
				MetricType: core.MetricGauge,
				IntValue:   123456,
			},
		},
		LabeledMetrics: []core.LabeledMetric{
			{
				Name: labeledMetricNameA,
				Labels: map[string]string{
					core.LabelResourceID.Key: "XYZ",
				},
				MetricValue: core.MetricValue{
					MetricType: core.MetricGauge,
					FloatValue: 124.456,
				},
			},
			{
				Name: labeledMetricNameB,
				MetricValue: core.MetricValue{
					MetricType: core.MetricGauge,
					FloatValue: 454,
				},
			},
		},
	}

	metricSet.LabeledMetrics = append(metricSet.LabeledMetrics, metricValueToLabeledMetric(metricSet.MetricValues)...)

	now := time.Now()
	//
	m, err := hSink.pointToLabeledMetricHeader(&metricSet, metricSet.LabeledMetrics[2], now)
	assert.NoError(t, err)

	assert.Equal(t, fmt.Sprintf("%s/%s/%s", metricSet.Labels[core.LabelContainerName.Key],
		metricSet.Labels[core.LabelPodId.Key], metricName), m.ID)

	assert.Equal(t, 1, len(m.Data))
	_, ok := m.Data[0].Value.(float64)
	assert.True(t, ok, "Value should have been converted to float64")

	delete(l, core.LabelPodId.Key)

	//
	m, err = hSink.pointToLabeledMetricHeader(&metricSet, metricSet.LabeledMetrics[2], now)
	assert.NoError(t, err)

	assert.Equal(t, fmt.Sprintf("%s/%s/%s", metricSet.Labels[core.LabelContainerName.Key], metricSet.Labels[core.LabelNodename.Key], metricName), m.ID)

	//
	m, err = hSink.pointToLabeledMetricHeader(&metricSet, metricSet.LabeledMetrics[0], now)
	assert.NoError(t, err)

	assert.Equal(t, fmt.Sprintf("%s/%s/%s/%s", metricSet.Labels[core.LabelContainerName.Key],
		metricSet.Labels[core.LabelNodename.Key], labeledMetricNameA,
		metricSet.LabeledMetrics[0].Labels[core.LabelResourceID.Key]), m.ID)

	//
	m, err = hSink.pointToLabeledMetricHeader(&metricSet, metricSet.LabeledMetrics[1], now)
	assert.NoError(t, err)
	assert.Equal(t, fmt.Sprintf("%s/%s/%s", metricSet.Labels[core.LabelContainerName.Key],
		metricSet.Labels[core.LabelNodename.Key], labeledMetricNameB), m.ID)
}

func TestMetricIds(t *testing.T) {
	hSink := dummySink()

	l := make(map[string]string)
	l["spooky"] = "notvisible"
	l[core.LabelHostname.Key] = "localhost"
	l[core.LabelHostID.Key] = "localhost"
	l[core.LabelContainerName.Key] = "docker"
	l[core.LabelPodId.Key] = "aaaa-bbbb-cccc-dddd"
	l[core.LabelNodename.Key] = "myNode"
	l[core.LabelNamespaceName.Key] = "myNamespace"

	metricName := "test/metric/nodeType"

	metricSet := core.MetricSet{
		Labels: l,
		MetricValues: map[string]core.MetricValue{
			metricName: {
				ValueType:  core.ValueInt64,
				MetricType: core.MetricGauge,
				IntValue:   123456,
			},
		},
	}
	metricSet.LabeledMetrics = metricValueToLabeledMetric(metricSet.MetricValues)

	now := time.Now()
	//
	m, err := hSink.pointToLabeledMetricHeader(&metricSet, metricSet.LabeledMetrics[0], now)
	assert.NoError(t, err)
	assert.Equal(t, fmt.Sprintf("%s/%s/%s", metricSet.Labels[core.LabelContainerName.Key], metricSet.Labels[core.LabelPodId.Key], metricName), m.ID)

	//
	metricSet.Labels[core.LabelMetricSetType.Key] = core.MetricSetTypeNode
	m, err = hSink.pointToLabeledMetricHeader(&metricSet, metricSet.LabeledMetrics[0], now)
	assert.NoError(t, err)
	assert.Equal(t, fmt.Sprintf("%s/%s/%s", "machine", metricSet.Labels[core.LabelNodename.Key], metricName), m.ID)

	//
	metricSet.Labels[core.LabelMetricSetType.Key] = core.MetricSetTypePod
	m, err = hSink.pointToLabeledMetricHeader(&metricSet, metricSet.LabeledMetrics[0], now)
	assert.NoError(t, err)
	assert.Equal(t, fmt.Sprintf("%s/%s/%s", core.MetricSetTypePod, metricSet.Labels[core.LabelPodId.Key], metricName), m.ID)

	//
	metricSet.Labels[core.LabelMetricSetType.Key] = core.MetricSetTypePodContainer
	m, err = hSink.pointToLabeledMetricHeader(&metricSet, metricSet.LabeledMetrics[0], now)
	assert.NoError(t, err)
	assert.Equal(t, fmt.Sprintf("%s/%s/%s", metricSet.Labels[core.LabelContainerName.Key], metricSet.Labels[core.LabelPodId.Key], metricName), m.ID)

	//
	metricSet.Labels[core.LabelMetricSetType.Key] = core.MetricSetTypeSystemContainer
	m, err = hSink.pointToLabeledMetricHeader(&metricSet, metricSet.LabeledMetrics[0], now)
	assert.NoError(t, err)
	assert.Equal(t, fmt.Sprintf("%s/%s/%s/%s", core.MetricSetTypeSystemContainer, metricSet.Labels[core.LabelContainerName.Key], metricSet.Labels[core.LabelPodId.Key], metricName), m.ID)

	//
	metricSet.Labels[core.LabelMetricSetType.Key] = core.MetricSetTypeCluster
	m, err = hSink.pointToLabeledMetricHeader(&metricSet, metricSet.LabeledMetrics[0], now)
	assert.NoError(t, err)
	assert.Equal(t, fmt.Sprintf("%s/%s", core.MetricSetTypeCluster, metricName), m.ID)

	//
	metricSet.Labels[core.LabelMetricSetType.Key] = core.MetricSetTypeNamespace
	m, err = hSink.pointToLabeledMetricHeader(&metricSet, metricSet.LabeledMetrics[0], now)
	assert.NoError(t, err)
	assert.Equal(t, fmt.Sprintf("%s/%s/%s", core.MetricSetTypeNamespace, metricSet.Labels[core.LabelNamespaceName.Key], metricName), m.ID)

}

func TestRecentTest(t *testing.T) {
	hSink := dummySink()

	modelT := make(map[string]string)

	id := "test.name"
	modelT[descriptorTag] = "d"
	modelT[groupTag] = id
	modelT["hep"+descriptionTag] = "n"

	model := metrics.MetricDefinition{
		ID:   id,
		Tags: modelT,
	}

	liveT := make(map[string]string)
	for k, v := range modelT {
		liveT[k] = v
	}

	live := metrics.MetricDefinition{
		ID:   "test/" + id,
		Tags: liveT,
	}

	assert.True(t, hSink.recent(&live, &model), "Tags are equal, live is newest")

	delete(liveT, "hep"+descriptionTag)
	live.Tags = liveT

	assert.False(t, hSink.recent(&live, &model), "Tags are not equal, live isn't recent")

}

func TestParseFiltersErrors(t *testing.T) {
	_, err := parseFilters([]string{"(missingcommand)"})
	assert.Error(t, err)

	_, err = parseFilters([]string{"missingeverything"})
	assert.Error(t, err)

	_, err = parseFilters([]string{"labelstart:^missing$)"})
	assert.Error(t, err)

	_, err = parseFilters([]string{"label(endmissing"})
	assert.Error(t, err)

	_, err = parseFilters([]string{"label(wrongsyntax)"})
	assert.Error(t, err)
}

// Integration tests
func integSink(uri string) (*hawkularSink, error) {

	u, err := url.Parse(uri)
	if err != nil {
		return nil, err
	}

	sink := &hawkularSink{
		uri: u,
	}
	if err = sink.init(); err != nil {
		return nil, err
	}

	return sink, nil
}

// Test that Definitions is called for Gauges & Counters
// Test that we have single registered model
// Test that the tags for metric is updated..
func TestRegister(t *testing.T) {
	m := &sync.Mutex{}
	// definitionsCalled := make(map[string]bool)
	updateTagsCalled := false
	requests := 0

	s := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		m.Lock()
		defer m.Unlock()
		w.Header().Set("Content-Type", "application/json")

		if strings.Contains(r.RequestURI, "tenants") {
			fmt.Fprintln(w, `[{ "id": "test-heapster"},{"id": "fgahj-fgas-basf-gegsg" }]`)
		} else {
			tenant := r.Header.Get("Hawkular-Tenant")
			if tenant != "test-heapster" {
				requests++
				w.WriteHeader(http.StatusNoContent)
				return
			}
			if strings.Contains(r.RequestURI, "metrics?tags=descriptor_name%3A%2A") || strings.Contains(r.RequestURI, "openshift") {
				requests++
				fmt.Fprintln(w, `[{ "id": "test.create.gauge.1", "tenantId": "test-heapster", "type": "gauge", "tags": { "descriptor_name": "test/metric/1" } }]`)
			} else if strings.Contains(r.RequestURI, "/tags") && r.Method == "PUT" {
				updateTagsCalled = true
				defer r.Body.Close()
				b, err := ioutil.ReadAll(r.Body)
				assert.NoError(t, err)

				tags := make(map[string]string)
				err = json.Unmarshal(b, &tags)
				assert.NoError(t, err)

				_, kt1 := tags["k1_description"]
				_, dt := tags["descriptor_name"]

				assert.True(t, kt1, "k1_description tag is missing")
				assert.True(t, dt, "descriptor_name is missing")

				w.WriteHeader(http.StatusOK)
			}
		}
	}))
	defer s.Close()

	hSink, err := integSink(s.URL + "?tenant=test-heapster")
	assert.NoError(t, err)

	md := make([]core.MetricDescriptor, 0, 1)
	ld := core.LabelDescriptor{
		Key:         "k1",
		Description: "d1",
	}
	smd := core.MetricDescriptor{
		Name:      "test/metric/1",
		Units:     core.UnitsBytes,
		ValueType: core.ValueInt64,
		Type:      core.MetricGauge,
		Labels:    []core.LabelDescriptor{ld},
	}
	smdg := core.MetricDescriptor{
		Name:      "test/metric/2",
		Units:     core.UnitsBytes,
		ValueType: core.ValueFloat,
		Type:      core.MetricCumulative,
		Labels:    []core.LabelDescriptor{},
	}

	md = append(md, smd, smdg)

	err = hSink.Register(md)
	assert.NoError(t, err)

	assert.Equal(t, 2, len(hSink.models))
	assert.Equal(t, 1, len(hSink.expReg))

	assert.True(t, updateTagsCalled, "Updating outdated tags was not called")
	assert.Equal(t, 1, requests)

	// Try without pre caching
	updateTagsCalled = false

	hSink, err = integSink(s.URL + "?tenant=test-heapster&disablePreCache=true")
	assert.NoError(t, err)

	err = hSink.Register(md)
	assert.NoError(t, err)

	assert.Equal(t, 2, len(hSink.models))
	assert.Equal(t, 0, len(hSink.expReg))

	assert.False(t, updateTagsCalled, "Updating outdated tags was called")
}

// Store timeseries with both gauges and cumulatives
func TestStoreTimeseries(t *testing.T) {
	m := &sync.Mutex{}
	ids := make([]string, 0, 2)
	calls := make([]string, 0, 2)
	s := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		m.Lock()
		defer m.Unlock()
		calls = append(calls, r.RequestURI)
		w.Header().Set("Content-Type", "application/json")

		typ := r.RequestURI[strings.Index(r.RequestURI, "hawkular/metrics/")+17:]
		typ = typ[:len(typ)-4]

		switch typ {
		case "counters":
			assert.Equal(t, "test-label", r.Header.Get("Hawkular-Tenant"))
			break
		case "gauges":
			assert.Equal(t, "test-heapster", r.Header.Get("Hawkular-Tenant"))
			break
		default:
			assert.FailNow(t, "Unrecognized type "+typ)
		}

		defer r.Body.Close()
		b, err := ioutil.ReadAll(r.Body)
		assert.NoError(t, err)

		mH := []metrics.MetricHeader{}
		err = json.Unmarshal(b, &mH)
		assert.NoError(t, err)

		assert.Equal(t, 1, len(mH))

		ids = append(ids, mH[0].ID)
	}))
	defer s.Close()

	hSink, err := integSink(s.URL + "?tenant=test-heapster&labelToTenant=projectId")
	assert.NoError(t, err)

	l := make(map[string]string)
	l["projectId"] = "test-label"
	l[core.LabelContainerName.Key] = "test-container"
	l[core.LabelPodId.Key] = "test-podid"

	lg := make(map[string]string)
	lg[core.LabelContainerName.Key] = "test-container"
	lg[core.LabelPodId.Key] = "test-podid"

	metricSet1 := core.MetricSet{
		Labels: l,
		MetricValues: map[string]core.MetricValue{
			"test/metric/1": {
				ValueType:  core.ValueInt64,
				MetricType: core.MetricCumulative,
				IntValue:   123456,
			},
		},
	}

	metricSet2 := core.MetricSet{
		Labels: lg,
		MetricValues: map[string]core.MetricValue{
			"test/metric/2": {
				ValueType:  core.ValueFloat,
				MetricType: core.MetricGauge,
				FloatValue: 123.456,
			},
		},
	}

	data := core.DataBatch{
		Timestamp: time.Now(),
		MetricSets: map[string]*core.MetricSet{
			"pod1": &metricSet1,
			"pod2": &metricSet2,
		},
	}

	hSink.ExportData(&data)
	assert.Equal(t, 2, len(calls))
	assert.Equal(t, 2, len(ids))

	assert.NotEqual(t, ids[0], ids[1])
}

func TestTags(t *testing.T) {
	m := &sync.Mutex{}
	calls := make([]string, 0, 2)
	serverTags := make(map[string]string)
	// how many times tags have been updated
	tagsUpdated := 0
	s := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		m.Lock()
		defer m.Unlock()
		calls = append(calls, r.RequestURI)
		w.Header().Set("Content-Type", "application/json")

		defer r.Body.Close()
		b, err := ioutil.ReadAll(r.Body)
		assert.NoError(t, err)

		if strings.HasSuffix(r.RequestURI, "/tags") {
			err := json.Unmarshal(b, &serverTags)
			assert.NoError(t, err)
			tagsUpdated++
		}
	}))
	defer s.Close()

	hSink, err := integSink(s.URL + "?tenant=test-heapster&labelToTenant=projectId")
	assert.NoError(t, err)

	l := make(map[string]string)
	l["projectId"] = "test-label"
	l[core.LabelContainerName.Key] = "test-container"
	l[core.LabelPodId.Key] = "test-podid"
	l[core.LabelLabels.Key] = "testLabelA:testValueA,testLabelB:testValueB"

	labeledMetric := core.LabeledMetric{
		Name: "test/metric/A",
		Labels: map[string]string{
			core.LabelResourceID.Key: "XYZ",
		},
		MetricValue: core.MetricValue{
			MetricType: core.MetricGauge,
			FloatValue: 124.456,
		},
	}

	metricSet := core.MetricSet{
		Labels:         l,
		LabeledMetrics: []core.LabeledMetric{labeledMetric},
		MetricValues: map[string]core.MetricValue{
			"test/metric/A": {
				ValueType:  core.ValueInt64,
				MetricType: core.MetricCumulative,
				IntValue:   123456,
			},
		},
	}

	smd := core.MetricDescriptor{
		Name:      "test/metric/A",
		Units:     core.UnitsBytes,
		ValueType: core.ValueInt64,
		Type:      core.MetricGauge,
		Labels:    []core.LabelDescriptor{},
	}

	//register the metric definitions
	hSink.Register([]core.MetricDescriptor{smd})
	//register the metrics themselves
	wg := &sync.WaitGroup{}
	hSink.registerLabeledIfNecessaryInline(&metricSet, labeledMetric, wg)

	wg.Wait()
	assert.Equal(t, 1, tagsUpdated)

	assert.True(t, hSink.expReg["test-container/test-podid/test/metric/A/XYZ"].hash > 0)

	assert.Equal(t, 10, len(serverTags))
	assert.Equal(t, "test-label", serverTags["projectId"])
	assert.Equal(t, "test-container", serverTags[core.LabelContainerName.Key])
	assert.Equal(t, "test-podid", serverTags[core.LabelPodId.Key])
	assert.Equal(t, "test-container/test/metric/A", serverTags["group_id"])
	assert.Equal(t, "test/metric/A", serverTags["descriptor_name"])
	assert.Equal(t, "XYZ", serverTags[core.LabelResourceID.Key])
	assert.Equal(t, "bytes", serverTags["units"])

	assert.Equal(t, "testLabelA:testValueA,testLabelB:testValueB", serverTags[core.LabelLabels.Key])
	assert.Equal(t, "testValueA", serverTags["labels.testLabelA"])
	assert.Equal(t, "testValueB", serverTags["labels.testLabelB"])

	// Make modifications to the metrics and check that they're updated correctly

	// First, no changes - no update should happen
	hSink.registerLabeledIfNecessaryInline(&metricSet, labeledMetric, wg)
	wg.Wait()
	assert.Equal(t, 1, tagsUpdated)

	// Now modify the labels and expect an update
	metricSet.Labels[core.LabelLabels.Key] = "testLabelA:testValueA,testLabelB:testValueB,testLabelC:testValueC"
	hSink.registerLabeledIfNecessaryInline(&metricSet, labeledMetric, wg)
	wg.Wait()
	assert.Equal(t, 2, tagsUpdated)

	assert.Equal(t, "testLabelA:testValueA,testLabelB:testValueB,testLabelC:testValueC", serverTags[core.LabelLabels.Key])
	assert.Equal(t, "testValueA", serverTags["labels.testLabelA"])
	assert.Equal(t, "testValueB", serverTags["labels.testLabelB"])
	assert.Equal(t, "testValueC", serverTags["labels.testLabelC"])
}

func TestExpiringCache(t *testing.T) {
	total := 10
	s := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
	}))
	defer s.Close()

	hSink, err := integSink(s.URL + "?tenant=test-heapster&labelToTenant=projectId&batchSize=20&concurrencyLimit=5")
	assert.NoError(t, err)

	l := make(map[string]string)
	l["projectId"] = "test-label"
	l[core.LabelContainerName.Key] = "test-container"
	l[core.LabelPodId.Key] = "test-podid"

	metrics := make(map[string]core.MetricValue, total)
	descriptors := make([]core.MetricDescriptor, total)
	for i := 0; i < total; i++ {
		id := fmt.Sprintf("test/metric/%d", i)
		metrics[id] = core.MetricValue{
			ValueType:  core.ValueInt64,
			MetricType: core.MetricCumulative,
			IntValue:   123 * int64(i),
		}
		descriptors = append(descriptors, core.MetricDescriptor{
			Name:      id,
			Units:     core.UnitsBytes,
			ValueType: core.ValueInt64,
			Type:      core.MetricGauge,
		})
	}

	err = hSink.Register(descriptors)
	assert.NoError(t, err)

	metricSet := core.MetricSet{
		Labels:       l,
		MetricValues: metrics,
	}

	data := core.DataBatch{
		Timestamp: time.Now(),
		MetricSets: map[string]*core.MetricSet{
			"pod1": &metricSet,
		},
	}

	hSink.ExportData(&data)
	hSink.regLock.RLock()
	assert.Equal(t, total, len(hSink.expReg))
	assert.Equal(t, uint64(1), hSink.expReg["test-container/test-podid/test/metric/9"].ttl)
	hSink.regLock.RUnlock()

	// Now delete part of the metrics and then check that they're expired
	delete(metrics, "test/metric/1")
	delete(metrics, "test/metric/6")

	data.Timestamp = time.Now()
	hSink.ExportData(&data)
	hSink.regLock.RLock()
	assert.Equal(t, total, len(hSink.expReg))
	assert.Equal(t, uint64(2), hSink.expReg["test-container/test-podid/test/metric/9"].ttl)
	hSink.regLock.RUnlock()

	data.Timestamp = time.Now()
	hSink.ExportData(&data)
	hSink.regLock.RLock()
	assert.Equal(t, total-2, len(hSink.expReg))
	hSink.regLock.RUnlock()
}

func TestUserPass(t *testing.T) {
	s := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("X-Authorization", r.Header.Get("Authorization"))
		auth := strings.SplitN(r.Header.Get("Authorization"), " ", 2)
		if len(auth) != 2 || auth[0] != "Basic" {
			assert.FailNow(t, "Could not find Basic authentication")
		}
		assert.True(t, len(auth[1]) > 0)
		w.WriteHeader(http.StatusNoContent)
	}))
	defer s.Close()

	hSink, err := integSink(s.URL + "?user=tester&pass=hidden")
	assert.NoError(t, err)
	assert.Equal(t, 1, len(hSink.modifiers))

	// md := make([]core.MetricDescriptor, 0, 1)
	ld := core.LabelDescriptor{
		Key:         "k1",
		Description: "d1",
	}
	smd := core.MetricDescriptor{
		Name:      "test/metric/1",
		Units:     core.UnitsBytes,
		ValueType: core.ValueInt64,
		Type:      core.MetricGauge,
		Labels:    []core.LabelDescriptor{ld},
	}
	err = hSink.Register([]core.MetricDescriptor{smd})
	assert.NoError(t, err)
}

func TestFiltering(t *testing.T) {
	m := &sync.Mutex{}
	mH := []metrics.MetricHeader{}
	s := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		m.Lock()
		defer m.Unlock()
		if strings.Contains(r.RequestURI, "raw") {
			defer r.Body.Close()
			b, err := ioutil.ReadAll(r.Body)
			assert.NoError(t, err)

			err = json.Unmarshal(b, &mH)
			assert.NoError(t, err)
		}
	}))
	defer s.Close()

	hSink, err := integSink(s.URL + "?filter=label(namespace_id:^$)&filter=label(container_name:^[/system.slice/|/user.slice].*)&filter=name(remove*)")
	assert.NoError(t, err)

	l := make(map[string]string)
	l["namespace_id"] = "123"
	l["container_name"] = "/system.slice/-.mount"
	l[core.LabelPodId.Key] = "aaaa-bbbb-cccc-dddd"

	l2 := make(map[string]string)
	l2["namespace_id"] = "123"
	l2["container_name"] = "/system.slice/dbus.service"
	l2[core.LabelPodId.Key] = "aaaa-bbbb-cccc-dddd"

	l3 := make(map[string]string)
	l3["namespace_id"] = "123"
	l3[core.LabelPodId.Key] = "aaaa-bbbb-cccc-dddd"

	l4 := make(map[string]string)
	l4["namespace_id"] = ""
	l4[core.LabelPodId.Key] = "aaaa-bbbb-cccc-dddd"

	l5 := make(map[string]string)
	l5["namespace_id"] = "123"
	l5[core.LabelPodId.Key] = "aaaa-bbbb-cccc-dddd"

	metricSet1 := core.MetricSet{
		Labels: l,
		MetricValues: map[string]core.MetricValue{
			"/system.slice/-.mount//cpu/limit": {
				ValueType:  core.ValueInt64,
				MetricType: core.MetricCumulative,
				IntValue:   123456,
			},
		},
	}

	metricSet2 := core.MetricSet{
		Labels: l2,
		MetricValues: map[string]core.MetricValue{
			"/system.slice/dbus.service//cpu/usage": {
				ValueType:  core.ValueInt64,
				MetricType: core.MetricCumulative,
				IntValue:   123456,
			},
		},
	}

	metricSet3 := core.MetricSet{
		Labels: l3,
		MetricValues: map[string]core.MetricValue{
			"test/metric/1": {
				ValueType:  core.ValueInt64,
				MetricType: core.MetricCumulative,
				IntValue:   123456,
			},
		},
	}

	metricSet4 := core.MetricSet{
		Labels: l4,
		MetricValues: map[string]core.MetricValue{
			"test/metric/1": {
				ValueType:  core.ValueInt64,
				MetricType: core.MetricCumulative,
				IntValue:   123456,
			},
		},
	}

	metricSet5 := core.MetricSet{
		Labels: l5,
		MetricValues: map[string]core.MetricValue{
			"removeme": {
				ValueType:  core.ValueInt64,
				MetricType: core.MetricCumulative,
				IntValue:   123456,
			},
		},
	}

	data := core.DataBatch{
		Timestamp: time.Now(),
		MetricSets: map[string]*core.MetricSet{
			"pod1": &metricSet1,
			"pod2": &metricSet2,
			"pod3": &metricSet3,
			"pod4": &metricSet4,
			"pod5": &metricSet5,
		},
	}
	hSink.ExportData(&data)

	assert.Equal(t, 1, len(mH))
}
func TestBatchingTimeseries(t *testing.T) {
	total := 1000
	m := &sync.Mutex{}
	ids := make([]string, 0, total)
	calls := 0

	s := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		m.Lock()
		defer m.Unlock()

		w.Header().Set("Content-Type", "application/json")

		defer r.Body.Close()
		b, err := ioutil.ReadAll(r.Body)
		assert.NoError(t, err)

		mH := []metrics.MetricHeader{}
		err = json.Unmarshal(b, &mH)
		assert.NoError(t, err)

		for _, v := range mH {
			ids = append(ids, v.ID)
		}

		calls++
	}))
	defer s.Close()

	hSink, err := integSink(s.URL + "?tenant=test-heapster&labelToTenant=projectId&batchSize=20&concurrencyLimit=5")
	assert.NoError(t, err)

	l := make(map[string]string)
	l["projectId"] = "test-label"
	l[core.LabelContainerName.Key] = "test-container"
	l[core.LabelPodId.Key] = "test-podid"

	metrics := make(map[string]core.MetricValue)
	for i := 0; i < total; i++ {
		id := fmt.Sprintf("test/metric/%d", i)
		metrics[id] = core.MetricValue{
			ValueType:  core.ValueInt64,
			MetricType: core.MetricCumulative,
			IntValue:   123 * int64(i),
		}
	}

	metricSet := core.MetricSet{
		Labels:       l,
		MetricValues: metrics,
	}

	data := core.DataBatch{
		Timestamp: time.Now(),
		MetricSets: map[string]*core.MetricSet{
			"pod1": &metricSet,
		},
	}

	hSink.ExportData(&data)
	assert.Equal(t, total, len(ids))
	assert.Equal(t, calls, 50)

	// Verify that all ids are unique
	newIds := make(map[string]bool)
	for _, v := range ids {
		if newIds[v] {
			t.Errorf("Key %s was duplicate", v)
		}
		newIds[v] = true
	}
}

func BenchmarkTagsUpdates(b *testing.B) {
	http.DefaultTransport.(*http.Transport).MaxIdleConnsPerHost = 100
	s := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
	}))
	defer s.Close()
	hSink, err := integSink(s.URL + "?tenant=test-heapster&labelToTenant=projectId&batchSize=1000&concurrencyLimit=16")
	if err != nil {
		b.FailNow()
	}

	smd := core.MetricDescriptor{
		Name:      "test/metric/A",
		Units:     core.UnitsBytes,
		ValueType: core.ValueInt64,
		Type:      core.MetricGauge,
		Labels:    []core.LabelDescriptor{},
	}

	//register the metric definitions
	hSink.Register([]core.MetricDescriptor{smd})
	total := 10000

	mset := make(map[string]*core.MetricSet)
	for i := 0; i < total; i++ {
		id := fmt.Sprintf("pod-%d", i)

		l := make(map[string]string)
		l["projectId"] = strconv.Itoa(i)
		for i := 0; i < 32; i++ {
			tagName := fmt.Sprintf("tag_name_%d", i)
			tagValue := fmt.Sprintf("tag_value_%d", i)
			l[tagName] = tagValue
			l[core.LabelPodId.Key] = id
		}

		metrics := make(map[string]core.MetricValue)
		metrics["test/metric/A"] = core.MetricValue{
			ValueType:  core.ValueInt64,
			MetricType: core.MetricCumulative,
			IntValue:   123,
		}

		metricSet := core.MetricSet{
			Labels:       l,
			MetricValues: metrics,
		}
		mset[id] = &metricSet
	}

	data := core.DataBatch{
		Timestamp:  time.Now(),
		MetricSets: mset,
	}

	fmt.Printf("Generated data with %d metricSets\n", len(data.MetricSets))
	hSink.init()
	hSink.Register([]core.MetricDescriptor{smd})
	b.ResetTimer()
	for j := 0; j < b.N; j++ {
		for a := 0; a < 10; a++ {
			data.Timestamp = time.Now()
			hSink.ExportData(&data)
		}
	}

	fmt.Printf("Amount of unique definitions: %d\n", len(hSink.expReg))
}
