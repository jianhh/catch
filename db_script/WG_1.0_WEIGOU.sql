create table T_CONFIG_SQL
(
  SQL_ID    VARCHAR2(200) primary key,
  SQL_VALUE VARCHAR2(2000)
);

create table T_CONFIG
(
  TYPENAME    VARCHAR2(50) not null,
  CONFIGNAME  VARCHAR2(1000) not null,
  CONFIGVALUE VARCHAR2(1000),
  DESCS       VARCHAR2(1000) not null,
  LUPDDATE    DATE not null,
  LUPDUSER    VARCHAR2(1000) not null
);

--config-XCached.xml
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','serviesPool','10.101.10.41:13222 10.101.10.41:13223 10.101.10.42:13224 10.101.10.42:13225 10.101.10.43:13226 10.101.10.43:13227','集群配置ip1:port1 ip2:port2',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','connectionPoolSize','5','连接池大小',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','compressionSize','102400','缓存的最大对象K为单位',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','timeout','10000','获取缓存对象连接超时时间单位:ms',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','expire','86400','获取缓存对象有效期:1天',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','weights','8','权重:要大于等于集群配置数',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','recBuffer','64','接收缓冲大小：单位KB',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','setBuffer','32','发送缓冲大小：单位KB',sysdate,'tangbiao');

--config-ThreadPool.xml
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('threadPoolConfig','min_size','50','最小线程数量',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('threadPoolConfig','max_size','200','最大线程数量',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('threadPoolConfig','keepAliveTime','60','线程的存活时间，即完成任务后多久可再使用(秒)',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('threadPoolConfig','queueSize','100','等待队列的长度',sysdate,'tangbiao');

--cacheTableConfig.xml
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('configitem','T_CONFIG_SQL','SQL_ID;60;SQL_VALUE','缓存表T_PPS_CONFIG_SQL',sysdate,'tangbiao');


commit;



