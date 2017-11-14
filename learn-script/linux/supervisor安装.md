### 安装文档

官方地址：`http://supervisord.org/installing.html#installing-via-pip`

### 创建默认配置文件

官方说明：`http://supervisord.org/installing.html#creating-a-configuration-file`  
其实就是执行`echo_supervisord_conf > /etc/supervisord.conf`。  
其中`/etc/supervisord.conf`可以根据情况自由变更。

### 配置文件参数说明

官方说明：`http://supervisord.org/configuration.html`

### 分离程序配置文件

在`/etc/supervisord.conf`中加入
```
[include]
files = /usr/local/src/supervisor_program/*.conf
```
在`/usr/local/src/supervisor_program`中创建`app1.conf`，内容如下
```
[program:app1]
command=/usr/local/bin/app1 -c /usr/local/src/app1.json
```
然后重启`service supervisord restart`