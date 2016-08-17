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
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','serviesPool','10.101.10.41:13222 10.101.10.41:13223 10.101.10.42:13224 10.101.10.42:13225 10.101.10.43:13226 10.101.10.43:13227','��Ⱥ����ip1:port1 ip2:port2',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','connectionPoolSize','5','���ӳش�С',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','compressionSize','102400','�����������KΪ��λ',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','timeout','10000','��ȡ����������ӳ�ʱʱ�䵥λ:ms',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','expire','86400','��ȡ���������Ч��:1��',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','weights','8','Ȩ��:Ҫ���ڵ��ڼ�Ⱥ������',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','recBuffer','64','���ջ����С����λKB',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('xcached','setBuffer','32','���ͻ����С����λKB',sysdate,'tangbiao');

--config-ThreadPool.xml
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('threadPoolConfig','min_size','50','��С�߳�����',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('threadPoolConfig','max_size','200','����߳�����',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('threadPoolConfig','keepAliveTime','60','�̵߳Ĵ��ʱ�䣬�����������ÿ���ʹ��(��)',sysdate,'tangbiao');
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('threadPoolConfig','queueSize','100','�ȴ����еĳ���',sysdate,'tangbiao');

--cacheTableConfig.xml
insert into t_config(siteName,typeName,configName,configValue,Descs,lupdDate,lupdUser) values('configitem','T_CONFIG_SQL','SQL_ID;60;SQL_VALUE','�����T_PPS_CONFIG_SQL',sysdate,'tangbiao');


commit;



