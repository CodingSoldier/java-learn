vim /etc/my.cnf
# 二进制日志
server_id=1
log_bin=master_log
max_binlog_size=1000M
# 行模式，记录数据值而不是记录语句
binlog_format=row
expire_logs_days=7
# 每次事务提交,把日志刷到磁盘
sync_binlog=1









