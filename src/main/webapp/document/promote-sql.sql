/*第2步：创建数据表空间  */
create tablespace promote_TEMP
logging  
datafile 'D:\oracle_server\oradata\orcl\promote_TEMP.DBF' 
size 50m  
autoextend on  
next 50m maxsize 20480m  
extent management local;  



create user promote identified by tigerr 
default tablespace promote_TEMP;

grant connect,resource,dba to promote;
ALTER USER promote QUOTA 50M ON promote_TEMP; 
ALTER USER  promote  QUOTA UNLIMITED ON promote_TEMP;
select * from dba_ts_quotas;

alter user  promote quota unlimited on USERS;



