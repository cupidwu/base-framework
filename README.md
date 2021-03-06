###框架说明


base-framework是对常用的java web开发封装实用功能来提高开发效率的底层框架。base-framework基于spring做核心框架、hibernate或spring data jpa做持久化框架,用spring mvc框架对mvc做管理。使用到的新功能有spring缓存工厂、apeche shiro安全框架、spring mvc,spring data jpa等主要流行技术, 该项目分为两个部分做底层的封装，和带有一个项目演示例子。

[相关帮助文档](https://github.com/dactiv/base-framework/wiki)

#### 初始化工作:

***

##### 配置maven

1. 下载[maven](http://maven.apache.org/download.html)
1. 解压maven-[版本]-bin.zip到你想安装的位置
1. 设置maven系统环境变量，M2_HOME=[maven安装目录位置]
1. 添加maven bin目录至系统环境变量PATH中， %M2_HOME%\bin
1. 确认Maven的安装：cmd > mvn -version

##### 安装base-framework到maven中

1. 使用git或者svn下载base-framework

***
	git地址:git://github.com/dactiv/base-framework.git
	svn地址:https://github.com/dactiv/base-framework.git
***

1. 点击根目录下的quick-start.bat文件进行安装,当看见以下信息时表示安装成功:

***
	[INFO] ------------------------------------------------------------------------
	[INFO] BUILD SUCCESS
	[INFO] ------------------------------------------------------------------------
	[INFO] Total time: 6.147s
	[INFO] Finished at: Mon Oct 07 10:59:23 CST 2013
	[INFO] Final Memory: 9M/156M
	[INFO] ------------------------------------------------------------------------
	[INFO] start basic curd app
	[INFO] Please wait a moment. When you see "[INFO] Started Jetty Server", you can visit: http://localhost:8080/dactiv-base-curd/ to view the demo
***

你可以通过http://localhost:8080/dactiv-base-curd/来访问项目

该安装过程会将base-framework中的dactiv-common和dactiv-orm生成jar包放入maven的.m2文件夹中（当然,这两个框架使用到的依赖jar包也会下载到.m2文件夹中）.同时也会初始化h2数据库和启动jetty，让执行完成后直接访问。

##### dactiv common 简单说明

该jar包是对基本的常用工具类的一些简单封装。如泛型，反射，配置文件等工具类的封装。

##### dactiv orm 简单说明

该jar包是对持久化层的框架封装，目前只对Hibernate4和spring data jpa的curd和辅助查询功能封装。

##### 项目功能演示例子

在文件夹的shorcase里有一个base-curd项目。该项目是对以上两个框架(dactiv-common和dactiv-orm)和其他技术的整合做的例子，通过该例子使用maven做了一个archetype基础模板。可以通过该archetype来生成一个新的项目。该文件在base-curd\bin下面（archetype-generate.bat）。

通过base-curd项目文件夹中的bin/jetty.bat文件运行项目，也可以用eclipse.bat生成项目导入到开发工具中在运行。该工程下有一个基于jeety运行的java文件org.dactiv.showcase.test.LaunchJetty.你也可以通过该文件运行整个项目.

##### 导入eclipse或者myeclipse
在根目录下的bin目录有一个eclipse.bat和myeclipse.bat，点击eclipse.bat/myeclipse.bat会生成project，看见以下信息表示生成成功，可以直接导入eclipse/myclipse

***
	[INFO] ------------------------------------------------------------------------
	[INFO] Reactor Summary:
	[INFO]
	[INFO] dactiv parent pom................................... SUCCESS [0.552s]
	[INFO] dactiv common jar ............................... SUCCESS [2:21.572s]
	[INFO] dactiv orm jar .................................. SUCCESS [6.828s]
	[INFO] base curd project war ................................ SUCCESS [1:07.878s]
	[INFO] base web framework project ........................ SUCCESS [0.644s]
	[INFO] ------------------------------------------------------------------------
	[INFO] BUILD SUCCESS
	[INFO] ------------------------------------------------------------------------
	[INFO] Total time: 3:39.025s
	[INFO] Finished at: Wed Dec 04 14:50:14 CST 2013
	[INFO] Final Memory: 10M/25M
	[INFO] ------------------------------------------------------------------------
***

注意:过于旧版本的的eclipse和myeclipse在导入项目时如果发现有错,请检查工具是否已经配置了maven的仓库路径.具体查看在工具的:window->preferences,然后进入到Java->Build Path->Classpath Variables查看左边是否存在M2_REPO,如果不存在,点击New按钮,Name:M2_REPO,Path:你仓库的位置.如:C:\Users\feng\.m2\repository
