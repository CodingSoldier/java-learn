# searxng搜索引擎
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


# product表
```
CREATE TABLE `product` (
`id` bigint NOT NULL COMMENT '主键',
`product_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '商品的编号',
`product_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '商品的名称',
`brand` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '商品的品牌',
`description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '商品的简介（可以为空）',
`price` float(10,2) DEFAULT '0.00' COMMENT '商品的价格',
`stock` int DEFAULT '0' COMMENT '商品的库存数量',
`status` int DEFAULT NULL COMMENT '商品的状态（上架状态的值为1/下架状态的值为0/预售状态的值为2）',
`created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品表';


```

