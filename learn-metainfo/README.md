### jetty插件META-INF支持
####场景一

jsp文件位于目录中。目录为classpath下的META-INF/resources。此时jetty插件不会自动读取该目录下的jsp。
此时plugin的配置为：

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-war-plugin</artifactId>
        <version>3.0.0</version>
        <configuration>
        </configuration>
    </plugin>
    <plugin>
        <groupId>org.eclipse.jetty</groupId>
        <artifactId>jetty-maven-plugin</artifactId>
        <version>9.4.6.v20170531</version>
    </plugin>
    
运行时使用jetty:run
 
####场景二

jsp文件位于jar中。在jar中的位置为META-INF/resources。此时jetty插件可以读取到此处的jsp。
此时plugin的配置为：

    <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-war-plugin</artifactId>
            <version>3.0.0</version>
            <configuration>
                <archiveClasses>true</archiveClasses>
            </configuration>
    </plugin>
    <plugin>
        <groupId>org.eclipse.jetty</groupId>
        <artifactId>jetty-maven-plugin</artifactId>
        <version>9.4.6.v20170531</version>
    </plugin>

运行时使用jetty:run-war。
使用run-war的原因是，此方式会先打包war文件，然后再运行。在打包成war之后，jsp文件也就被打在jar中了（`archiveClasses`参数的作用）。

###结论

jetty插件只支持jar文件中的META-INF。对于目录中的META-INF不做处理。

###解决方案

我们在开发过程中，通过jetty:run运行，需要支持目录中的META-INF。此时可以通过额外的配置来达到。

    <plugin>
        <groupId>org.eclipse.jetty</groupId>
        <artifactId>jetty-maven-plugin</artifactId>
        <version>9.4.6.v20170531</version>
        <configuration>
            <webApp>
                <resourceBases>
                    <resourceBase>${project.basedir}/src/main/webapp</resourceBase>
                    <resourceBase>${project.basedir}/src/main/resources/META-INF/resources</resourceBase>
                </resourceBases>
            </webApp>
        </configuration>
    </plugin>
    
如上配置，即手动指定要加载的目录。
当然这种配置会造成jetty:run-war无法运行，不过这个又不常用，可以无视（或者注释掉额外的配置）。

### tomcat插件META-INF支持

目录模式和jar模式均可，无需额外配置。