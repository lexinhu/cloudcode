# Elasticsearch搜索引擎

elasticsearch 是一款非常强大的开源搜索引擎，可以帮助我们从海量数据中快速找到需要的内容。

文档：一条数据就是一个文档，es中是Json格式

字段：Json文档中的字段

索引：同类型文档的集合

映射：索引中文档的约束，比如字段名称、类型

elasticsearch 与数据库的关系：

- 数据库负责事务类型操作
- elasticsearch负责海量数据的搜索、分析、计算

IK分词器有几种模式？

- ik_smart：智能切分，粗粒度
- ik_max_word：最细切分，细粒度

