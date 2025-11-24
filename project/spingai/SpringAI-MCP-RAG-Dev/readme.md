## searxng搜索引擎
### 本地安装
mkdir -p /etc/searxng/config

mkdir -p /var/searxng/data

docker pull docker.io/searxng/searxng:2025.8.1-3d96414

docker run --name searxng -d \
-p 8888:8080 \
-v "/etc/searxng/config/:/etc/searxng/" \
-v "/var/searxng/data/:/var/cache/searxng/" \
docker.io/searxng/searxng:2025.8.1-3d96414

vim /etc/searxng/config/settings.yml

```
search:
  formats:
    - html
    - json  # 加上json
```

编辑settings.yml，禁用engines下的Google、wikidata等无法访问的引擎，开启baidu、bing这些能访问的引擎。
具体配置参考searxng的settings.yml

docker restart 容器ID

本地访问：http://192.168.1.221:8888/





