数据地址
        https://www.elastic.co/guide/en/kibana/6.6/tutorial-load-dataset.html

        # 新建索引
        PUT/logstash-2015.05.18
        {
        "mappings":{
        "log":{
        "properties":{
        "geo":{
        "properties":{
        "coordinates":{
        "type":"geo_point"
        }
        }
        }
        }
        }
        }
        }

        PUT/logstash-2015.05.19
        {
        "mappings":{
        "log":{
        "properties":{
        "geo":{
        "properties":{
        "coordinates":{
        "type":"geo_point"
        }
        }
        }
        }
        }
        }
        }

        PUT/logstash-2015.05.20
        {
        "mappings":{
        "log":{
        "properties":{
        "geo":{
        "properties":{
        "coordinates":{
        "type":"geo_point"
        }
        }
        }
        }
        }
        }
        }


        postman导入logs.jsonl
        GET logstash-*/_search
        新建index_pattern ——>logstash-*
        在Discover中最开始没有数据，要选择最近5年









