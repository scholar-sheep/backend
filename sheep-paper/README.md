### 目录结构
.
├───java
│   └───sheep
│       └───paper
│           │   SheepPaperApplication.java          主应用
│           │
│           ├───Configuration
│           │       ElasticSearchClient.java        用来访问 ElasticSearch 的 Client
│           │
│           ├───Controller
│           │       PaperController.java            和 Paper 相关的请求在此处理，依赖 PaperRepository 接口和 PaperService 接口
│           │
│           ├───Entity
│           │       Paper.java                      对应 MySQL 中存放 Paper 信息的实体类
│           │
│           ├───Repository
│           │       PaperRepository.java            操作 MySQL 的接口
│           │
│           └───Service                             针对 ElasticSearch 提供所需的服务
│               │   PaperService.java               服务接口
│               │
│               └───impl
│                       PaperServiceImpl.java       服务实现
│
└───resources
        application.properties                      必要配置
