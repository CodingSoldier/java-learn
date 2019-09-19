window版本启动  
  elasticsearch.bat 启动es
  npm run start  启动head插件

配置文件位于config目录中
  elasticsearch.yml    es配置
  jvm.options          jvm参数相关
  log4j2.properties    日志相关

elasticsearch.yml
  cluster.name  集群名称，以此作为是否在同一集群的判断条件
  node.name   节点名称，以此作为集群中不同节点的区分条件
  network.host/http.port  网络地址和端口，用于http、transport服务使用
  path.data  数据存储地址
  path.log   日志存储地址

window快速启动集群
  ./elasticsearch.bat
  ./elasticsearch.bat -E http.port=8200 -E path.data=node2
  ./elasticsearch.bat -E http.port=7200 -E path.data=node3

集群信息
http://localhost:8200/_cat/nodes?v
http://localhost:8200/_cluster/stats

Kibana
  启动 kibana.bat
  http://localhost:5601/app/kibana

kibana.yml配置
  server.host/server/port     访问kibana的地址和端口
  elasticsearch.url      elasticsearch的地址

在kibana 的dev tools 中调用api
#####  索引API
# 创建索引
PUT /test_index

# 获取索引
GET _cat/indices

# 删除索引
DELETE /test_index


##############Document
# 指定id创建文档，若索引不存在，es会自动创建索引
# 再次运行version会变成2，result显示为更新
PUT /test_index/doc/1
{
  "username": "alfred",
  "age": 1
}

# post可以不指定id 
POST /test_index/doc
{
  "username": "tom",
  "age": 2
}

# post也可以指定id
POST /test_post_index/doc/1
{
  "username": "tom",
  "age": 2
}

# 查询文档
GET /test_index/doc/1
# 查询文档，文档不存在
GET /test_index/doc/1122312

# 查询指定索引下的所有文档
GET /test_index/doc/_search

######批量操作api bulk
# 必须把json收缩才能执行成功
# 更新的写法比较特别
# 查看返回结果中的errors判断是否存在失败的操作
POST _bulk
{"index":{"_index":"test_index","_type":"doc","_id":"2"}}
{"username":"u222","age":"10"}
{"delete":{"_index":"test_index","_type":"doc","_id":"1"}}
{"update":{"_index":"test_index","_type":"doc","_id":"3"}}
{"doc":{"username":"更新的文档，更新内容写在doc中"}}


# 一次查询获取多个文档，前面的/可要可不要
GET /_mget
{
  "docs":[
    {
      "_index": "test_index",
      "_type": "doc",
      "_id": "2"      
    },
    {
      "_index": "test_index",
      "_type": "doc",
      "_id": "kw8nK20BhzWF8o3fK6kF"  
    }    
    ]
}

# 分词测试api
POST _analyze
{
  "analyzer": "standard",
  "text": "hello world"
}

# 指定索引中的字段测试分词,未指定分词器，默认使用standard分词器
POST test_index/_analyze
{
  "field": "username",
  "text": "hello world"
}

# 自定义分词器测试
POST _analyze
{
  "tokenizer": "standard",
  "filter": ["lowercase"],
  "text": "Hello World!"
}

# 创建、更新文档时，会对文档分词
# 查询时，会对查询语句进行分词

# 索引时分词是配置Index Mapping中每个字段的analyzer属性实现的
# 不指定分词时，使用默认standard

# 索引时分词，配置index mapping的analyzer属性
PUT test_index_ananlyzer
{
  "mappings":{
    "doc":{
      "properties":{
        "title":{
          "type": "text",
          "analyzer": "whitespace"
        }
      }
    }
  }
}


# 查询时分词，配置index mapping的search_analyzer属性
# 查询、索引分词可以不一样，但是建议不指定search_analyzer,让查询、索引分词保持一致
PUT test_index_ananlyzer_search
{
  "mappings":{
    "doc":{
      "properties":{
        "title":{
          "type": "text",
          "analyzer": "whitespace",
          "search_analyzer": "standard"
        }
      }
    }
  }
}

# 定义Mapping相当于定义数据库的表结构，mapping也可以定义索引

# 获取mapping
GET /test_index/_mapping

# 自定义索引的mapping
# doc是类型，未来或取消，不管它，但在6.X版本还必须写上，不能取消
PUT mapping_index
{
  "mappings" : {
    "doc" : {
      "properties" : {
        "age" : {
          "type" : "long"
        },
        "username" : {
          "type" : "text"
        }
      }
    }
  }
}

# Mapping中的字段一旦设定，禁止直接修改
  # Lucene实现的倒排索引生成后不允许修改
# 若要修改Mapping中的字段类型，需新建新的索引，然后reindex操作

# 自定义mapping与新增字段
# 通过dynamic参数来控制
# true，默认，允许自动新增字段
# false，不允许自动新增字段，但是文档可以正常写入，但是无法对字段进行查询操作
# strict 文档新增字段，写入就报错

# 配置mapping的dynamic
PUT mapping_dynamic
{
  "mappings":{
    "doc":{
      "dynamic": false,
      "properties":{
        "title":{
          "type": "text"
        },
        "name":{
          "type": "keyword"
        }        
      }
    }
  }
}

# 新增，mapping中不存在desc
PUT /mapping_dynamic/doc/1
{
  "title": "hello world",
  "desc": "nothing here"
}

# 通过title查询能获取到结果
GET /mapping_dynamic/doc/_search
{
  "query": {
    "match":{
      "title": "hello"
    }
  }
}

# 通过desc查询，不能查询到结果
GET /mapping_dynamic/doc/_search
{
  "query": {
    "match":{
      "desc": "nothing"
    }
  }
}


# 字段的copy_to属性，将first_name,last_name拷贝到full_name
DELETE my_index
PUT my_index
{
  "mappings": {
    "doc": {
      "properties": {
        "first_name": {
          "type": "text",
          "copy_to": "full_name" 
        },
        "last_name": {
          "type": "text",
          "copy_to": "full_name" 
        },
        "full_name": {
          "type": "text"
        }
      }
    }
  }
}

# 新增文档，没写full_name
PUT my_index/doc/1
{
  "first_name": "John",
  "last_name": "Smith"
}

# 查询可以通过full_name查询
GET my_index/_search
{
  "query": {
    "match": {
      "full_name": { 
        "query": "John Smith",
        "operator": "and"
      }
    }
  }
}


# index，控制当前字段是否能被索引
DELETE my_index
PUT my_index
{
  "mappings": {
    "doc": {
      "properties": {
        "cookie": {
          "type": "text",
          "index": "false" 
        }
      }
    }
  }
}

# 插入数据
PUT my_index/doc/1
{
  "cookie": "name=alfred"
}

# 搜索cookie会报错
GET my_index/_search
{
  "query": {
    "match": {
      "cookie":"name"
    }
  }
}


# null_value设置默认值
DELETE my_index
PUT my_index
{
  "mappings": {
    "my_type": {
      "properties": {
        "status_code": {
          "type":       "keyword",
          "null_value": "1" 
        }
      }
    }
  }
}

# 核心数据类型
字符串类型 text、keyword。text能分词、keyword不能分词


# 多字段类型，比如用pingying也可以搜索汉字，必须要有pinyin这个分词插件
DELETE my_index
PUT my_index
{
  "mappings": {
    "doc": {
      "properties": {
        "username": {
          "type": "text",
          "fields": {
            "pinyin":{
              "type": "text",
              "analyzer": "pinin"
            }
          }
        }
      }
    }
  }
}

# 多字段查询
GET test_index/_search
{
  "query": {
    "match": {
      "username.pinyin": "haha"
    }
  }
}


# 日期格式化
DELETE my_index
PUT my_index
{
  "mappings": {
    "doc": {
      "dynamic_date_formats": ["MM/dd/yyyy"]
    }
  }
}

# 插入时间
PUT my_index/doc/1
{
  "create_date": "09/25/2015"
}

#获取时间
GET my_index/doc/1

# 获取mapping
GET my_index/_mapping


#########自定义mapping的操作步骤
1、写入一条文档到es的临时索引中，获取es自动生成的mapping
2、修改步骤1中得到的mapping
3、使用步骤2的mapping创建实际所需索引


通过url query参数实现搜搜，常用参数如下：
  q 指定查询的语句，语法为query string syntax
  df q中不指定字段时，默认查询的字段。如果不指定，es默认查询所有字段
  sort 排序
  timeout 指定超时时间，默认不超时
  form,size 用于分页

#查询user字段包含alfred的文档，结果按照age升序排列，返回第5~14个文档，如果超过1s没有结束，则以超时结束
GET /test_index/_search?q=alfred&df=user&sort=age:asc&from=4&size=10&timeout=1s




########  插入数据  #######
DELETE test_search_index

PUT test_search_index
{
  "settings": {
    "index":{
        "number_of_shards": "1"
    }
  }
}

POST test_search_index/doc/_bulk
{"index":{"_id":"1"}}
{"username":"alfred way","job":"java engineer","age":18,"birth":"1990-01-02","isMarried":false}
{"index":{"_id":"2"}}
{"username":"alfred","job":"java senior engineer and java specialist","age":28,"birth":"1980-05-07","isMarried":true}
{"index":{"_id":"3"}}
{"username":"lee","job":"java and ruby engineer","age":22,"birth":"1985-08-07","isMarried":false}
{"index":{"_id":"4"}}
{"username":"alfred junior way","job":"ruby engineer","age":23,"birth":"1989-08-07","isMarried":false}

GET test_search_index/doc/_search



# match，包含alfred、way
GET test_search_index/_search
{
  "query":{
    "match":{
      "username":"alfred way"
    }
  }
}

# operator，and同时包含alfred、way
GET test_search_index/_search
{
  "query":{
    "match":{
      "username":{
        "query": "alfred way",
        "operator": "and"
      }
    }
  }
}

# minimum_should_match，包含的分词个数
GET test_search_index/_search
{
  "query":{
    "match":{
      "job":{
        "query": "java ruby engineer",
        "minimum_should_match": 3
      }
    }
  }
}


# explain true查看相关性算分
GET test_search_index/_search
{
  "explain": true, 
  "query":{
    "match":{
      "username":"alfred way"
    }
  }
}


# 短语查询
GET test_search_index/_search
{
  "query":{
    "match_phrase":{
      "job":"java engineer"
    }
  }
}

# 短语查询，slop允许有差异
GET test_search_index/_search
{
  "query":{
    "match_phrase":{
      "job":{
        "query": "java engineer",
        "slop": 2
      }
    }
  }
}

# username中同时包含alfred和way
GET test_search_index/_search
{
  "query":{
    "query_string":{
      "default_field": "username",
      "query": "alfred AND way"
    }
  }
}

# username、job包含alfred或者java和rubby
GET test_search_index/_search
{
  "query":{
    "query_string":{
      "fields":["username", "job"],
      "query":"alfred OR (java AND ruby)"
    }
  }
}

# term不分词，直接去倒排索引匹配
GET test_search_index/_search
{
  "query":{
    "term":{
      "username":"alfred"
    }
  }
}

# 文档中有username=alfred way，但是倒排索引中没有username=alfred way，索引返回空
GET test_search_index/_search
{
  "query":{
    "term":{
      "username":"alfred way"
    }
  }
}

# username倒排索引中包含alfred或way
GET test_search_index/_search
{
  "query":{
    "terms":{
      "username":[
        "alfred",
        "way"
      ]
    }
  }
}

# 范围查询
GET test_search_index/_search
{
  "query":{
    "range":{
      "age":{
        "gte": 10,
        "lte": 20
      }
    }
  }
}

# 日期可以使用字符串
GET test_search_index/_search
{
  "query":{
    "range":{
      "birth":{
        "gte": "1990-01-01"
      }
    }
  }
}

# now当前时间
GET test_search_index/_search
{
  "query":{
    "range":{
      "birth":{
        "gte": "now-30y"
      }
    }
  }
}

# constant_score自定义得分
GET test_search_index/_search
{
  "query":{
    "constant_score": {
      "filter": {
        "match":{
          "username":"alfred"
        }
      }
    }
  }
}

# bool查询，使用filter后不计算相关性得分，可以提高查询效率
GET test_search_index/_search
{
  "query":{
    "bool":{
      "filter":[{
        "term":{
          "username":"alfred"
        }
      }]
    }
  }
}

# must中的条件都要满足
GET test_search_index/_search
{
  "query":{
    "bool":{
      "must":[
        {
          "match":{
            "username":"alfred"
          }
        },
        {
          "match":{
            "job":"specialist"
          }
        }
      ]
    }
  }
}

# 排除must_not中的所有条件
GET test_search_index/_search
{
  "query":{
    "bool":{
      "must":[{
        "match":{
          "username":"alfred"
        }
      }],
      "must_not": [
        {
          "match": {
            "job": "ruby"
          }
        }
      ]
    }
  }
}

# bool查询中只包含should，不包含must。则必须有一个should条件成立
GET test_search_index/_search
{
  "query":{
    "bool":{
      "should":[{
        "match":{
          "username":"junior"
        }
      },{
        "match":{
          "job":"ruby"
        }
      }]
    }
  }
}

# 使用minimum_should_match指定必须匹配的条件个数
GET test_search_index/_search
{
  "query":{
    "bool":{
      "should":[
        {"term": {"job": "java"}},
        {"term": {"job": "ruby"}},
        {"term": {"job": "specialist"}}
      ],
      "minimum_should_match": 2
    }
  }
}

# bool查询条件中同时包含should、must。不要求满足should条件，但满足should条件的文档得分会提高
GET test_search_index/_search
{
  "query":{
    "bool":{
      "must":[
        {"term": {"username": "alfred"}}
      ],      
      "should":[
        {"term": {"job": "ruby"}}
      ]
    }
  }
}


## _count API获取文档数量
GET test_search_index/_count
{
  "query":{
    "match": {
      "username": "alfred"
    }
  }
}

# _source只返回部分字段，还有其他的写法
GET test_search_index/_search
{
  "query":{
    "match": {
      "username": "alfred"
    }
  }
  , "_source": ["username", "age"]
}


############# 集群  #################
使用cerebro管理集群，双击 cerebro.bat 启动cerebro
启动3个节点
.\elasticsearch.bat -E cluster.name=my_cluster -E path.data=my_cluster_node1 -E node.name=node1 -E http.port=5200
.\elasticsearch.bat -E cluster.name=my_cluster -E path.data=my_cluster_node2 -E node.name=node2 -E http.port=5300
.\elasticsearch.bat -E cluster.name=my_cluster -E path.data=my_cluster_node3 -E node.name=node3 -E http.port=5400

# 指定3个分片和1个副本
# 结果是在集群的3个节点上建立1个主分片和1个副分片，总共6个分片
# 仔细观察发现，主分片和副本分片不会在同一个节点上
PUT test_index
{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 1
  }
}

# 集群有3个节点，索引设置5个分片，也是可以的





############# 排序  
POST test_search_index/doc/_bulk
{"index":{"_id":"1"}}
{"username":"alfred way","job":"java engineer","age":18,"birth":"1990-01-02","isMarried":false,"salary":10000}
{"index":{"_id":"2"}}
{"username":"tom","job":"java senior engineer","age":28,"birth":"1980-05-07","isMarried":true,"salary":30000}
{"index":{"_id":"3"}}
{"username":"lee","job":"ruby engineer","age":22,"birth":"1985-08-07","isMarried":false,"salary":15000}
{"index":{"_id":"4"}}
{"username":"Nick","job":"web engineer","age":23,"birth":"1989-08-07","isMarried":false,"salary":8000}
{"index":{"_id":"5"}}
{"username":"Niko","job":"web engineer","age":18,"birth":"1994-08-07","isMarried":false,"salary":5000}
{"index":{"_id":"6"}}
{"username":"Michell","job":"ruby engineer","age":26,"birth":"1987-08-07","isMarried":false,"salary":12000}


# 分布式系统深度分页问题，会在所有分片上都执行from=10000, size=2，合并所有分片数据之后再筛选出from=10000, size=2
# es为防止数据太大，限制分页只能获取前一万条数据，下面的查询报错
GET test_search_index/_search
{
  "from": 10000,
  "size": 2
}

# 这个查询可以用，因为分页数据没超过一万
GET test_search_index/_search
{
  "from": 9998,
  "size": 2
}


# scroll遍历文档集API，在遍历的时候创建一个快照，避免了深度分页的问题，但是由于是快照，所以数据不是实时的

# 创建一个快照搜索，scroll=5m快照有效期是5分钟，每次返回1个文档
# 返回结果中会有scroll_id，下一个scroll查询会用到
GET test_search_index/_search?scroll=5m
{
  "size": 1
}

# 下一个快照查询，可以重新指定快照过期时间，防止快照过期
# 当hits为空数组的时候，就可以结束遍历了
POST _search/scroll
{
  "scroll": "5m",
  "scroll_id": "DnF1ZXJ5VGhlbkZldGNoBQAAAAAAABg3FlVEREw4TjNLU2ZLUnJxaXlzSlZaZkEAAAAAAAAYOBZVRERMOE4zS1NmS1JycWl5c0pWWmZBAAAAAAAAGDkWVURETDhOM0tTZktScnFpeXNKVlpmQQAAAAAAABg1FlVEREw4TjNLU2ZLUnJxaXlzSlZaZkEAAAAAAAAYNhZVRERMOE4zS1NmS1JycWl5c0pWWmZB"
}


# 删除所有快照
DELETE _search/scroll/_all



# search_after避免深度分页的性能问题，提供实时获取下一页文档的能力
# 缺点：不能指定页数并且只能获取下一页不能获取上一页

# 第一步为正常搜索，但查询中必须带有sort值
GET test_search_index/_search
{
  "size": 1,
  "sort": {
    "age": "desc",
    "_id": "desc"
  }
}

# 下一次查询的时候search_after写上一步返回结果中的sort
GET test_search_index/_search
{
  "size": 1,
  "search_after": ["28", "2"],
  "sort": {
    "age": "desc",
    "_id": "desc"
  }
}



# 聚合分析，获取age的最小值
# size=0表示不返回文档内容
GET test_search_index/_search
{
  "size":0,
  "aggs":{
    "min_age":{
      "min":{
        "field":"age"
      }
    }
  }
}

# 最大值
GET test_search_index/_search
{
  "size":0,
  "aggs":{
    "max_age":{
      "max":{
        "field":"age"
      }
    }
  }
}


# 求多个聚合值
GET test_search_index/_search
{
  "size":0,
  "aggs":{
    "min_age":{
      "min":{
        "field":"age"
      }
    },    
    "max_age":{
      "max":{
        "field":"age"
      }
    },
    "avg_age":{
      "avg":{
        "field":"age"
      }
    }    
  }
}

# cardinality查找字段不相同值的个数
GET test_search_index/_search
{
  "size":0,
  "aggs":{
    "count_of_job":{
      "cardinality":{
        "field":"job.keyword"
      }
    }
  }
}

# stats返回一系列统计值
GET test_search_index/_search
{
  "size":0,
  "aggs":{
    "stats_age":{
      "stats":{
        "field":"age"
      }
    }
  }
}

# 百分位数统计
GET test_search_index/_search
{
  "size":0,
  "aggs":{
    "per_salary":{
      "percentiles":{
        "field":"salary"
      }
    }
  }
}


# bucket 按照一定的规则将文档分配到不同的桶中，已达到分类分析的目的
GET test_search_index/_search
{
  "size": 0,
  "aggs": {
    "jobs": {
      "terms": {
        "field": "job.keyword",
        "size": 5
      }
    }
  }
}

# 区间分桶，key是指定返回结果中的key
GET test_search_index/_search
{
  "size": 0,
  "aggs": {
    "salary_range": {
      "range": {
        "field": "salary",
        "ranges": [
          {
            "key": "<10000",
            "to": 10000
          },
          {
            "key": "10000~20000",
            "from": 10000,
            "to": 20000
          },
          {
            "key": ">20000",
            "from": 20000
          }          
        ]
      }
    }
  }
}

# 分桶后再对桶内数据做聚合分析
GET test_search_index/_search
{
  "size": 0,
  "aggs":{
    "jobs":{
      "terms": {
        "field": "job.keyword",
        "size": 10
      },
      "aggs": {
        "age_range": {
            "range":{
              "field": "age",
              "ranges":[
                {
                  "to": 20
                },
                {
                  "from": 20,
                  "to": 30
                },
                {
                  "from": 30
                }                
              ]
            }
        }
      }
    }
  }
}

# 对分桶结果做指标分析
GET test_search_index/_search
{
  "size": 0,
  "aggs":{
    "jobs":{
      "terms": {
        "field": "job.keyword",
        "size": 10
      },
      "aggs": {
        "salary": {
            "stats":{
              "field": "salary"
            }
        }
      }
    }
  }
}

# Pipeline聚合分析
# 对聚合分析的结果再次进行聚合分析，而且支持链式调用
# 语句中包含buckets_path就是Pipeline聚合分析
# Pipeline聚合分析结果。Parent结果内嵌到现有的聚合分析结果中。Sibling结果和现有的聚合分析结果同级

# min_bucket找出最小的桶
# buckets_path即路径，路径为jobs里面的avg_salary
GET test_search_index/_search
{
  "size": 0,
  "aggs":{
    "jobs":{
      "terms": {
        "field": "job.keyword",
        "size": 10
      },
      "aggs": {
        "avg_salary": {
            "avg":{
              "field": "salary"
            }
        }
      }
    },
    "min_salary_by_job":{
      "min_bucket":{
        "buckets_path": "jobs>avg_salary"
      }
    }    
  }
}


# derivative求导数是内嵌到参数中
GET test_search_index/_search
{
  "size": 0,
  "aggs":{
    "birth":{
      "date_histogram": {
        "field": "birth",
        "interval": "year",
        "min_doc_count": 0
      },
      "aggs": {
        "avg_salary": {
            "avg":{
              "field": "salary"
            }
        },
        "derivative_avg_salary":{
          "derivative":{
            "buckets_path": "avg_salary"
          }
        }          
      }
    }
  }
}

# 聚合搜索+排序
GET test_search_index/_search
{
  "size": 0,
  "aggs":{
    "jobs":{
      "terms":{
        "field": "job.keyword",
        "size": 10,
        "order":[
          {
            "_count": "asc"
          },
          {
            "_key": "desc"
          }          
        ]
      }
    }
  }
}




# 字段中包含大文本的处理，比如文章内容
DELETE bolog_index

# title字段type为text类型可以用于分词搜索
# 再添加keyword类型，可以用于排序、聚合、全文匹配
PUT bolog_index
{
  "mappings": {
    "doc":{
      "properties":{
        "title":{
          "type":"text",
          "fields":{
            "keyword":{
              "type": "keyword",
              "ignore_above": 100
            }
          }
        },
        "publish_date":{
          "type": "date"
        },
        "author":{
          "type":"keyword",
          "ignore_above": 100
        },
        "abstract":{
          "type": "text"
        },  
        "url":{
          "enabled": false
        },
        "content":{
          "type": "text"
        }
      }
    }
  }
}

# 插入数据
PUT bolog_index/doc/1
{
  "title": "blog title",
  "content": "blog content"
}

# 获取数据
GET bolog_index/_search





# 删除索引
DELETE bolog_index

#_source.enabled=false，查询时候返回结果不会返回_source字段
# 因为content字段数据很多，不用返回content字段的内容
PUT bolog_index
{
  "mappings": {
    "doc":{
      "_source":{
        "enabled": false
      },
      "properties":{
        "title":{
          "type":"text",
          "fields":{
            "keyword":{
              "type": "keyword",
              "ignore_above": 100
            }
          },
          "store": true
        },
        "publish_date":{
          "type": "date",
          "store": true
        },
        "author":{
          "type":"keyword",
          "ignore_above": 100,
          "store": true
        },
        "abstract":{
          "type": "text",
          "store": true
        },  
        "url":{
          "type": "keyword",
          "doc_values": false,
          "norms": false,
          "ignore_above": 100,
          "store": true
        },
        "content":{
          "type": "text",
          "store": true
        }
      }
    }
  }
}

# 插入数据
PUT bolog_index/doc/1
{
  "title": "blog title",
  "content": "blog content"
}

# 获取数据
GET bolog_index/_search

# 获取数据，这种是在分区返回数据时只返回出title字段，而不是返回文档所有字段后过滤出指定字段，效率就高了
GET bolog_index/_search
{
  "stored_fields": ["title"]
}

#高亮搜索，content中返回高亮内容
GET bolog_index/_search
{
  "stored_fields": ["title"],
  "query":{
    "match": {
      "content": "blog"
    }
  },
  "highlight":{
    "fields": {"content": {}}
  }
}




########### 重建索引，新索引不需要提前创建，但这样似乎就是改索引名字而已。
# "conflicts": "proceed"冲突时候覆盖
POST _reindex
{
  "conflicts": "proceed",
  "source": {
    "index": "bolog_index"
  },
  "dest":{
    "index": "bolog_new_index"
  }
}

# 获取数据
GET bolog_new_index/_search

# 删除索引
DELETE bolog_new_index

# 先新建索引，索引的mapping可以修改
PUT bolog_new_index
{
  "mappings": {
    "doc":{
      "_source":{
        "enabled": false
      },
      "properties":{
        "title":{
          "type":"text",
          "fields":{
            "keyword":{
              "type": "keyword",
              "ignore_above": 100
            }
          },
          "store": true
        },
        "publish_date":{
          "type": "date",
          "store": true
        },
        "author":{
          "type":"keyword",
          "ignore_above": 100,
          "store": true
        },
        "abstract":{
          "type": "text",
          "store": true
        },  
        "url":{
          "type": "keyword",
          "doc_values": false,
          "norms": false,
          "ignore_above": 100,
          "store": true
        },
        "content":{
          "type": "text",
          "store": true
        }
      }
    }
  }
}

# 数据迁移，重建索引
POST _reindex
{
  "conflicts": "proceed",
  "source": {
    "index": "bolog_index"
  },
  "dest":{
    "index": "bolog_new_index"
  }
}

# 获取数据
GET bolog_new_index/_search
{
  "stored_fields": ["title"]
}





# 集群设置  https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html
# JVM内存不要超过31GB
# 预留一半内存给操作系统，用来做文件缓存



# elasticsearch.yml 关闭xpack安全校验，在kibana中使用就不需要输入账号密码
xpack.security.enabled: false

kibana中的Monitoring有监控功能

















































































