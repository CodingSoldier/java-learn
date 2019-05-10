/*
   Copyright 2015-2017 Red Hat, Inc. and/or its affiliates
   and other contributors.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package metrics

import (
	"crypto/tls"
	"encoding/json"
	// "fmt"
	"net/http"
	"net/url"
	"time"
)

// HawkularClientError Extracted error information from Hawkular-Metrics server
type HawkularClientError struct {
	msg  string
	Code int
}

// Parameters is a struct used as initialization parameters to the client
type Parameters struct {
	Tenant      string // Technically optional, but requires setting Tenant() option every time
	Url         string
	TLSConfig   *tls.Config
	Username    string
	Password    string
	Token       string
	Concurrency int
	AdminToken  string
}

// Client is HawkularClient's internal data structure
type Client struct {
	Tenant      string
	url         *url.URL
	client      *http.Client
	Credentials string // base64 encoded username/password for Basic header
	Token       string // authentication token for Bearer header
	AdminToken  string // authentication for items behind admin token
	pool        chan (*poolRequest)
}

type poolRequest struct {
	req   *http.Request
	rChan chan (*poolResponse)
}

type poolResponse struct {
	err  error
	resp *http.Response
}

// HawkularClient is a base type to define available functions of the client
type HawkularClient interface {
	Send(*http.Request) (*http.Response, error)
}

// Modifier Modifiers base type
type Modifier func(*http.Request) error

// Filter Filter type for querying
type Filter func(r *http.Request)

// Endpoint Endpoint type to define request URL
type Endpoint func(u *url.URL)

// MetricType restrictions
type MetricType string

const (
	Gauge        MetricType = "gauge"
	Availability            = "availability"
	Counter                 = "counter"
	Generic                 = "metrics"
	String                  = "string"
)

// MetricHeader is the header struct for time series, which has identifiers (tenant, type, id) for uniqueness
// and []Datapoint to describe the actual time series values.
type MetricHeader struct {
	Tenant string      `json:"-"`
	Type   MetricType  `json:"-"`
	ID     string      `json:"id"`
	Data   []Datapoint `json:"data"`
}

// Datapoint is a struct that represents a single time series value.
// Value should be convertible to float64 for gauge/counter series.
// Timestamp accuracy is milliseconds since epoch
type Datapoint struct {
	Timestamp time.Time         `json:"-"`
	Value     interface{}       `json:"value"`
	Tags      map[string]string `json:"tags,omitempty"`
}

// MarshalJSON is modified JSON marshalling for Datapoint object to modify time.Time to milliseconds since epoch
func (d Datapoint) MarshalJSON() ([]byte, error) {
	structCopy := map[string]interface{}{
		"timestamp": ToUnixMilli(d.Timestamp),
		"value":     d.Value,
	}

	if len(d.Tags) > 0 {
		structCopy["tags"] = d.Tags
	}

	b, err := json.Marshal(structCopy)

	return b, err
}

// To avoid recursion in UnmarshalJSON
type datapoint Datapoint

type datapointJSON struct {
	datapoint
	Ts int64 `json:"timestamp"`
}

// UnmarshalJSON is a custom unmarshaller for Datapoint for timestamp modifications
func (d *Datapoint) UnmarshalJSON(b []byte) error {
	dp := datapointJSON{}
	err := json.Unmarshal(b, &dp)
	if err != nil {
		return err
	}

	*d = Datapoint(dp.datapoint)
	d.Timestamp = FromUnixMilli(dp.Ts)

	return nil
}

// HawkularError is the return payload from Hawkular-Metrics if processing failed
type HawkularError struct {
	ErrorMsg string `json:"errorMsg"`
}

// MetricDefinition is a struct that describes the stored definition of a time serie
type MetricDefinition struct {
	Tenant        string            `json:"-"`
	Type          MetricType        `json:"type,omitempty"`
	ID            string            `json:"id"`
	Tags          map[string]string `json:"tags,omitempty"`
	RetentionTime int               `json:"dataRetention,omitempty"`
}

// TODO Fix the Start & End to return a time.Time

// Bucketpoint is a return structure for bucketed data requests (stats endpoint)
type Bucketpoint struct {
	Start       time.Time    `json:"-"`
	End         time.Time    `json:"-"`
	Min         float64      `json:"min"`
	Max         float64      `json:"max"`
	Avg         float64      `json:"avg"`
	Median      float64      `json:"median"`
	Empty       bool         `json:"empty"`
	Samples     uint64       `json:"samples"`
	Percentiles []Percentile `json:"percentiles"`
}

type bucketpoint Bucketpoint

type bucketpointJSON struct {
	bucketpoint
	StartTs int64 `json:"start"`
	EndTs   int64 `json:"end"`
}

// UnmarshalJSON is a custom unmarshaller to transform int64 timestamps to time.Time
func (b *Bucketpoint) UnmarshalJSON(payload []byte) error {
	bp := bucketpointJSON{}
	err := json.Unmarshal(payload, &bp)
	if err != nil {
		return err
	}

	*b = Bucketpoint(bp.bucketpoint)
	b.Start = FromUnixMilli(bp.StartTs)
	b.End = FromUnixMilli(bp.EndTs)

	return nil
}

// Percentile is Hawkular-Metrics' estimated (not exact) percentile
type Percentile struct {
	Quantile float64 `json:"quantile"`
	Value    float64 `json:"value"`
}

// Order is a basetype for selecting the sorting of requested datapoints
type Order int

const (
	// ASC Ascending
	ASC = iota
	// DESC Descending
	DESC
)

// String returns a string representation of type
func (o Order) String() string {
	switch o {
	case ASC:
		return "ASC"
	case DESC:
		return "DESC"
	}
	return ""
}

// TenantDefinition is the structure that defines a tenant
type TenantDefinition struct {
	ID         string             `json:"id"`
	Retentions map[MetricType]int `json:"retentions"`
}
