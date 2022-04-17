1. 表结构参考resources/mall-tables.sql
   
2. 批量插入见BatchInsertEfficiencyTest
   
    - 使用连接池+PreparedStatement+batch+WorkStealingPool 插入100w条数据耗时约15s
      
    - 删除索引后插入耗时可减少至约11s
      
    - 不使用连接池并发插入与使用连接池并发插入耗时相差不大，可能是因为并发+请求本地MySQL服务器，因此网络开销较小
    
3. 动态数据源切换(AbstractRoutingDataSource & ShardingSphere) 见 DataSourceConfig、DynamicDataSource及ShadowController