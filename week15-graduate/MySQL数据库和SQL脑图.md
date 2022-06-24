# 数据库、sql
## 类型
- 关系型数据库
- 非关系型数据库
## 关系型数据库
### 范式（避免冗余）
- 第一范式
- 第二范式
- 第三范式
- BC范式

### MySQL
#### buffer: 查询缓存、buffer pool
#### log
- redolog
- binlog：格式、与redolog结合
- undolog
#### mvcc：行记录格式、undolog的作用
#### 锁
- 表锁
- 行锁
- next-key
- gap
- 死锁检测
#### engine： Innodb、MyISAM、Memory
#### innodb索引：
- B+树：B+树调整、删除（页空洞）
- 最左前缀
- 加锁规则
- 索引失效：隐式转换、索引区分度
#### 高可用：主从、读写分离
#### SQL优化