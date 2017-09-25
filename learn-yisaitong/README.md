### yisaitong 脱密程序

#### 环境

可以正常解密的环境

#### 流程
 
 1. 复制java.exe为EXCEL.exe（WINWORD.EXE）
 1. 当前目录下打开命令行：./WINWORD.EXE -jar ${this}.jar *.docx 或 ./EXCEL.EXE -jar ${this}.jar *.xls
 1. 生成的随机文件，使用word或者excel强行打开，如果打开显示正常，证明脱密成功。
 1. 切记不要将随机文件再改回正常的后缀，这样会导致重新加密。