### 登录sqlplus 
命令 `sqlplus  username/password as sysdba`  
如：`sqlplus sys/admin as sysdba`

### 查询

>   查看表空间的名称及大小
```sql
SELECT t.tablespace_name, round(SUM(bytes / (1024 * 1024)), 0) ts_size 
FROM dba_tablespaces t, dba_data_files d 
WHERE t.tablespace_name = d.tablespace_name 
GROUP BY t.tablespace_name; 
```
>   查看表空间物理文件的名称及大小 
```sql
SELECT tablespace_name, 
file_id, 
file_name, 
round(bytes / (1024 * 1024), 0) total_space 
FROM dba_data_files 
ORDER BY tablespace_name; 
```
>   查看控制文件 
```sql
SELECT NAME FROM v$controlfile; 
```
>   查看日志文件 
```sql
SELECT MEMBER FROM v$logfile; 
```
>   查看表空间的使用情况 
```sql
SELECT SUM(bytes) / (1024 * 1024) AS free_space, tablespace_name 
FROM dba_free_space 
GROUP BY tablespace_name; 
SELECT a.tablespace_name, 
a.bytes total, 
b.bytes used, 
c.bytes free, 
(b.bytes * 100) / a.bytes "% USED ", 
(c.bytes * 100) / a.bytes "% FREE " 
FROM sys.sm$ts_avail a, sys.sm$ts_used b, sys.sm$ts_free c 
WHERE a.tablespace_name = b.tablespace_name 
AND a.tablespace_name = c.tablespace_name; 
```
>   查看数据库库对象
```sql
SELECT owner, object_type, status, COUNT(*) count# 
FROM all_objects 
GROUP BY owner, object_type, status; 
```
>   查看数据库的版本
```sql
SELECT version 
FROM product_component_version 
WHERE substr(product, 1, 6) = 'Oracle'; 
```
>   表空间使用情况/使用率
```sql
SELECT a.tablespace_name "表空间名", 
total "表空间大小", 
free "表空间剩余大小", 
(total - free) "表空间使用大小", 
total / (1024 * 1024 * 1024) "表空间大小(G)", 
free / (1024 * 1024 * 1024) "表空间剩余大小(G)", 
(total - free) / (1024 * 1024 * 1024) "表空间使用大小(G)", 
round((total - free) / total, 4) * 100 "使用率 %" 
FROM (SELECT tablespace_name, SUM(bytes) free 
FROM dba_free_space 
GROUP BY tablespace_name) a, 
(SELECT tablespace_name, SUM(bytes) total 
FROM dba_data_files 
GROUP BY tablespace_name) b 
WHERE a.tablespace_name = b.tablespace_name 
```
### 创建
> 创建临时表空间
```sql
create temporary tablespace ywk_temp
tempfile '/home/data/oracle/oradata/ora11tbs/ywk_temp.dbf'
size 50m
autoextend on
next 50m maxsize 1024m
extent management local;
```
>   创建表空间
```sql
create tablespace ywk_data
logging
datafile '/home/data/oracle/oradata/ora11tbs/ywk_data.dbf'
size 50m
autoextend on
next 50m maxsize 1024m
extent management local;
```
>   创建用户并指定表空间
```sql
create user yiwangkao identified by ywkpass
default tablespace ywk_data
temporary tablespace ywk_temp;
```
>   给用户授权-普通权限
```sql
grant connect,resource to yiwangkao;
```
>   给用户授权-dba权限
```sql
grant connect,resource,dba to user_name;
```
>   修改用户密码
```sql
alter user yiwangkao identified by ywkpass;
```
