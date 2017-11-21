### 部署java client

由于java client尚未上传到maven官方仓库，所以我们无法直接通过pom引用该jar。
要想在pom中使用该jar，我们需要将其install到本地仓库中。

先从官方网址`https://github.com/happyfish100/fastdfs-client-java.git`检出项目到本地。
然后进入检出后的项目根目录，执行命令：`mvn -DskipTests=true clean install`。
然后即可在项目中进行如下配置：
```
<dependency>
    <groupId>org.csource</groupId>
    <artifactId>fastdfs-client-java</artifactId>
    <version>1.27-SNAPSHOT</version>
</dependency>
```