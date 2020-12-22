### 简单的测试环境

#### 使用到的东西

- jar 包
- elasticsearch
- kibana 提供可视化

#### 环境搭建的流程

1. 编译 jar 包，拷贝到 `./test_env/webapp` 目录下
2. `cd ./test_env`
3. `docker-compose up -d`

webapp 使用 8080 端口，es 使用 9200 9300 端口，kibana 使用 5601 端口

#### paper 索引的建立

``` JSON
{
"mappings":{
        "properties":{
            "title":{
                "type": "text",
                "fields":{
                    "raw":{
                        "type": "keyword"
                    }
                }

            },
            "abstract":{
                "type": "text"

            },
            "keywords":{
                "type": "text",
                "fields":{
                    "raw":{
                        "type": "keyword"
                    }
                }
            },
            "fos":{
                "type": "text",
                "fields":{
                    "raw":{
                        "type": "keyword"
                    }
                }
            },
            "venue":{
                "type": "text",
                "fields":{
                    "raw":{
                        "type": "keyword"
                    }
                }
            },
            "year":{
                "type": "date"
            },
            "n_citation":{
                "type": "integer"
            },
            "authors":{
                "properties":{
                    "name":{
                        "type": "text",
                        "fields":{
                            "raw":{
                                "type": "keyword"
                            }
                        }
                    },
                    "org":{
                        "type": "text",
                        "fields":{
                            "raw":{
                                "type": "keyword"
                            }
                        }
                    }
                }
            },
            "url":{
                "type": "keyword",
                "index": "false"
            }
        }
    }

}
```

#### 数据插入

``` JSON
POST localhost:9200/paper/_doc/1
{
    "abstract":"simple abstract",
    "author":[{
        "name":"author name",
        "org":"author org"
    }],
    "fos":"some fos",
    "keywords":["some keywords"],
    "n_citation":114514,
    "title":"paper title",
    "url":"127.0.0.1",
    "venue":"some venue",
    "year":2020
}
```

#### 访问测试

http://127.0.0.1:8080/api/paper/basic/detail/1

http://127.0.0.1:8080/api/paper/basic/ref/1