### 项目地址

https://github.com/landyking/jeesite.git

### 请求流程
1.	浏览器打开http://localhost:8181/jeesite。本项目使用的spring mvc。
1.	spring-mvc.xml中有如下配置：
	
		<!-- 定义无Controller的path<->view直接映射 -->
		<mvc:view-controller path="/" view-name="redirect:${web.view.index}"/>

1.	根据jeesite.properties中配置的`web.view.index=/a`。故重定向到http://localhost:8181/jeesite/a
1.	web.xml中使用shiro进行权限拦截，配置如下：
	
		<!-- Apache Shiro -->
		<filter>
			<filter-name>shiroFilter</filter-name>
			<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
			<init-param>
				<param-name>targetFilterLifecycle</param-name>
				<param-value>true</param-value>
			</init-param>
		</filter>
		<filter-mapping>
			<filter-name>shiroFilter</filter-name>
			<url-pattern>/*</url-pattern>
		</filter-mapping>

1.	由于此时未登录，故重定向到登录地址http://localhost:8181/jeesite/a/login。
	1.	需要拦截的地址在spring-context-shiro.xml中配置。

			<!-- Shiro权限过滤过滤器定义 -->
			<bean name="shiroFilterChainDefinitions" class="java.lang.String">
				<constructor-arg>
					<value>
						/static/** = anon
						/userfiles/** = anon
						${adminPath}/cas = cas
						${adminPath}/login = authc
						${adminPath}/logout = logout
						${adminPath}/** = user
						/act/editor/** = user
						/ReportServer/** = user
					</value>
				</constructor-arg>
			</bean>

	1.	登录地址在spring-context-shiro.xml中配置。
	
			<!-- 安全认证过滤器 -->
			<bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
				<property name="securityManager" ref="securityManager" /><!-- 
				<property name="loginUrl" value="${cas.server.url}?service=${cas.project.url}${adminPath}/cas" /> -->
				<property name="loginUrl" value="${adminPath}/login" />
				<property name="successUrl" value="${adminPath}?login" />
				<property name="filters">
		            <map>
		                <entry key="cas" value-ref="casFilter"/>
		                <entry key="authc" value-ref="formAuthenticationFilter"/>
		            </map>
		        </property>
				<property name="filterChainDefinitions">
					<ref bean="shiroFilterChainDefinitions"/>
				</property>
			</bean>

1.	输入账号密码，成功登录后，重定向到地址http://localhost:8181/jeesite/a?login
1.	根据spring mvc注解配置，该地址对应的controller为LoginController#index。
	1.	将log4j.properties中spring的输出等级调整为debug。
	1.	重新启动程序，可以见到此语句`2017-08-14 15:19:53,030 INFO  [servlet.mvc.method.annotation.RequestMappingHandlerMapping] - Mapped "{[/a]}" onto public java.lang.String com.thinkgem.jeesite.modules.sys.web.LoginController.index(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)`
	1.	LoginController#index方法返回字符串`modules/sys/sysIndex`。
1.	根据spring-mvc.xml配置的页面模板信息，得到实际的模板位置为`WEB-INF\views\modules\sys\sysIndex.jsp`.
	
		<!-- 定义视图文件解析 -->
		<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
			<property name="prefix" value="/WEB-INF/views/"/>
			<property name="suffix" value=".jsp"/>
		</bean>

1.	补充一点，系统使用了sitemesh进行布局。
	1.	web.xml的配置
		
			<!-- SiteMesh -->
			<filter>
				<filter-name>sitemeshFilter</filter-name>
				<filter-class>com.opensymphony.sitemesh.webapp.SiteMeshFilter</filter-class>
			</filter>
			<filter-mapping>
				<filter-name>sitemeshFilter</filter-name>
				<url-pattern>/a/*</url-pattern>
			</filter-mapping>
			<filter-mapping>
				<filter-name>sitemeshFilter</filter-name>
				<url-pattern>/f/*</url-pattern>
			</filter-mapping>

	1.	WEB-INF/decorators.xml中的配置。

		<?xml version="1.0" encoding="UTF-8"?>
		<decorators defaultdir="/WEB-INF/views">
			
			<!-- 默认装饰页面, 在需要装饰的页面增加<meta name="decorator" content="default"/> -->
			<decorator name="blank" page="layouts/blank.jsp" />
			<decorator name="default" page="layouts/default.jsp" />
			
			<!-- CMS基础主题装饰页面 -->
			<decorator name="cms_default_basic" page="modules/cms/front/themes/basic/layouts/default.jsp" />
			<decorator name="cms_default_weixin" page="modules/cms/front/themes/weixin/layouts/default.jsp" />
			
		</decorators>

1.	sysIndex.jsp包含了此句`<meta name="decorator" content="blank"/>`.可知，使用了`/WEB-INF/views/layouts/blank.jsp`作为布局。
	1.	blank.jsp的主要内容如下。主要引入公共的js文件等内容。

			<%@ page contentType="text/html;charset=UTF-8"%>
			<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
			<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
			<!DOCTYPE html>
			<html style="overflow-x:auto;overflow-y:auto;">
			<head>
				<title><sitemesh:title/></title><!--  - Powered By JeeSite -->
				<%@include file="/WEB-INF/views/include/head.jsp" %>
				<!-- Baidu tongji analytics --><script>var _hmt=_hmt||[];(function(){var hm=document.createElement("script");hm.src="//hm.baidu.com/hm.js?82116c626a8d504a5c0675073362ef6f";var s=document.getElementsByTagName("script")[0];s.parentNode.insertBefore(hm,s);})();</script>
				<sitemesh:head/>
			</head>
			<body>
				<sitemesh:body/>
			</body>
			</html>

1.	最终http://localhost:8181/jeesite/a?login返回的是sysIndex.jsp和blank.jsp组合而成的页面。
	1.	页面首先展示顶部菜单，通过jsp标签进行加载。
	1.	默认选中第一个顶部菜单，通过`http://localhost:8181/jeesite/a/sys/menu/tree?parentId=`加载左侧菜单。
	1.	默认选中第一个左侧菜单，加载对应页面。
	1.	使用了jquery的插件为jquery.jerichotab.js。

### 用户和权限系统
#### 用户
#### 机构
#### 区域
#### 角色
#### 菜单

### 内置组件
