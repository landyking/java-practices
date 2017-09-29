### tomcat中静态变量的隔离性

1.  如果class是放在war包中的，具备隔离性。
1.  如果class放在jar包中，而jar包放在tomcat的共享目录里。不具备隔离性，静态变量之间会有干扰。