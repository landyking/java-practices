### 参考（抄袭）地址

地址在此：`https://ruiaylin.github.io/2014/12/12/python%20update/`
主要是归档和备份，方便自己后期查找。

### 升级步骤 

**更新系统和开发工具集**

更新指令
```
yum -y update
yum groupinstall -y 'development tools'
```
另外还需要安装 python 工具需要的额外软件包 SSL, bz2, zlib
```
yum install -y zlib-devel bzip2-devel openssl-devel xz-libs wget
```
**源码安装Python 2.7.x**

```
wget http://www.python.org/ftp/python/2.7.8/Python-2.7.8.tar.xz
xz -d Python-2.7.8.tar.xz
tar -xvf Python-2.7.8.tar
```
**安装详情：**

```
# 进入目录:
cd Python-2.7.8
# 运行配置 configure:
./configure --prefix=/usr/local
# 编译安装:
make
make altinstall
# 检查 Python 版本:
[root@dbmasterxxx ~]# python2.7 -V
Python 2.7.8
```
**设置 PATH**

为了我们能够方便的使用Python，我们需要设置系统变量或者建立 软连接将新版本的 Python
加入到 path 对应的目录 ：
```
export PATH="/usr/local/bin:$PATH"
# 或者 
ln -s /usr/local/bin/python2.7  /usr/bin/python
# 检查
[root@dbmasterxxx ~]# python -V
Python 2.7.8
[root@dbmasterxxx ~]# which python 
/usr/bin/python
```

**安装 setuptools**

```
#获取软件包
wget --no-check-certificate https://pypi.python.org/packages/source/s/setuptools/setuptools-1.4.2.tar.gz
# 解压:
tar -xvf setuptools-1.4.2.tar.gz
cd setuptools-1.4.2
# 使用 Python 2.7.8 安装 setuptools
python2.7 setup.py install
```
*安装 PIP*

```
curl  https://bootstrap.pypa.io/get-pip.py | python2.7 -
```
**修复 yum 工具**

此时yum应该是失效的，因为此时默认python版本已经是2.7了。而yum需要的是2.6 所以：
```
[root@dbmasterxxx ~]# which yum 
/usr/bin/yum
#修改 yum中的python 
将第一行  #!/usr/bin/python  改为 #!/usr/bin/python2.6
此时yum就ok啦
```