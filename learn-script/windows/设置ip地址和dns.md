代码如下
```
@echo off
rem eth //eth 为网卡名称，可在网络连接中查询，如"本地连接"
set eth="以太网"

echo "当前ip设置为："
netsh interface ip show address %eth%
echo "当前dns设置为："
netsh interface ip show dns %eth%

echo "清理ip设置"
netsh interface ip set address %eth% dhcp
echo "清理dns设置"
netsh interface ip delete dns %eth% all

echo "清理后的ip设置"
netsh interface ip show address %eth%
echo "清理后的dns设置"
netsh interface ip show dns %eth%

echo "设置新ip"
netsh interface ip set address %eth% static 192.168.29.31 255.255.255.0 192.168.29.1 1
netsh interface ip add address %eth% 192.168.22.29 255.255.255.0
echo "设置新dns"
netsh interface ip set dns %eth% static 192.168.88.21
netsh interface ip add dns %eth% 114.114.114.114

echo "新的ip为"
netsh interface ip show address %eth%
echo "新的dns为"
netsh interface ip show dns %eth%

pause
`````