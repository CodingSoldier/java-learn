########war包方式启动jenkins##########
#!/bin/bash
JAVA_HOME=/usr/local/software/jdk1.8.0_201
DJENKINS_HOME=/usr/local/software/jenkins/jenkins_home
nohup $JAVA_HOME/bin/java -DJENKINS_HOME=$DJENKINS_HOME -jar -Xms512m -Xmx512m -XX:+UseConcMarkSweepGC  /usr/local/software/jenkins/jenkins.war --httpPort=8888 > /tmp/jenkins.log 2>&1 &


jenkins开机启动
ll /etc/rc.d/init.d/jenkins.sh

nohup java -DJENKINS_HOME=/var/lib/jenkins -jar /usr/lib/jenkins/jenkins.war --logfile=/var/log/jenkins/jenkins.log --webroot=/var/cache/jenkins/war --httpPort=8080 --debug=5 >/dev/null 2>&1 &

#################安装jenkins#################
官方地址：https://pkg.jenkins.io/redhat-stable/
关闭防火墙、selinux
wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key

yum -y install java

yum -y install jenkins

useradd deploy
vim /etc/sysconfig/jenkins
/JENKINS_USER
JENKINS_USER=deploy

chown -R deploy:deploy /var/lib/jenkins
chown -R deploy:deploy /var/log/jenkins
chown -R deploy:deploy /var/cache/jenkins

systemctl start jenkins

# 查看日志、初始密码
tail -111f /var/log/jenkins/jenkins.log

ss -lntp|grep 8080

打开，输入密码，选择社区推荐的插件
http://192.168.1.203:8080/

创建第一个管理员用户
使用admin继续，登陆后修改密码
http://192.168.1.203:8080/
账号密码 admin admin



###################jenkins介绍###################
Jenkins Freestyle
	1、需在页面添加模块配置项与参数完成配置
	2、每个Job仅能实现一个开发功能
	3、无法将配置代码化，不利于Job配置迁移与版本控制
	4、逻辑相对简单，无需额外学习成本

Pipeline Job
	匹配持续集成与持续交付的概念
	1、所有模块，参数配置都可以体现为一个pipeline脚本
	2、可以定义多个stage构建一个管道与版本控制
	3、所有配置代码化，方便Job配置迁移与版本控制
	4、需要pipeline脚本语法基础


Jenkins Job 配置
1、配置Jenkins server本地Gitlab DNS
2、安装git client，curl工具依赖
3、关闭Git http.sslVerify
4、Dashboard —> 配置 —> Git plugin
	填写user name、email


Jenkins新建自由风格的软件项目

#####################Jenkins Pipeline Job编写规范####################
样例
#!groovy
pipeline {
	agent any
	environment {
		host='test.example.com'
	}	
	stages {
		stage('build') {	
			steps {
				sh "cat $host"
				echo $host
			}
		}		
	}	
}
pipeline基础架构
	所有代码包裹在pipeline{}层内
	stages{}层用来包含该pipeline所有stage子层
	stage{}层用来包含具体的steps{}子层
	steps{}层用来添加具体需要调用的模块语句

agent区域
	agent定义pipeline在哪里运行
	可以使用any、none，或具体的Jenkins node主机名
	例如：如果我们要特指在node1上执行，可以写成
	agent { node {label 'node1'} }


environment区域
	定义全局环境变量，变量名称=变量值
	也可以在stage定义环境变量，应用于单独的stage任务
pipeline {
	agent any
	environment {
		host='test.example.com'
	}
	stages {
		// 在stage中定义环境变量
		environment {
			host='test.example.com'
		}	
		stage('build') {	
			steps {
				sh "cat $host"
				echo $host
			}
		}		
	}	
}	

script区域
	在steps内定义script{}
	使用groovy脚本语言
steps{
	echo "hello world"
	script{
		def servers = ['node1','node2']
	}
}


常用steps区域
echo: 打印输出
sh: 调用Linux系统shell命令
git url: 调用git模块进行git相关操作
steps{
	echo $deploy
	sh "cat 'hello world'"
	git url: "https://xxxx.git"
}



#####################Jenkins Pipeline Job例子####################
1、jenkins机器安装git
	并配置全局user.name、user.email
		git config --global user.name chenpiqian
		git config --global user.email chenpiqian@megvii.com
2、jenkins页面添加credentialsId。jenkins页面不配置git也可以


1、新建pepiline流水线
2、流水线 —> 定义，使用Pipeline script，使用如下脚本
3、修改credentialsId与url
4、！！！坑！！！，github上的仓库必须新建master分支

#!groovy

pipeline {
    // agent {node {label 'master'}}
    agent any
    environment {
        PATH="/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin"
    }
    // parameters中配置的参数会作用于jenkins的页面中
    // choice是选项参数
    // string是字符串参数
    parameters {
        choice(
            name: 'deploy_env',
            choices: 'dev\nprod',
            description: 'choose deploy environment'
            )
        string (name: 'version', defaultValue: '1.0.0', description: 'build version')
    }
    stages {
        stage("克隆代码") {
            steps{
                sh 'git config --global http.sslVerify false'
                dir ("${env.WORKSPACE}") {
                    git branch: 'master', credentialsId: 'gitee-id', url: 'https://gitee.com/CodingSoldier/test-jenkins.git'
                }              
            }
        }
        stage("打印参数") {
            steps {
                dir ("${env.WORKSPACE}") {
                	// sh模块可通过$加变量名获取parameters中定义的变量
                	// freestyle类型的job的shell模块也是使用$加变量名获取parameters
                    sh """
                    echo "【信息】当前环境是 $deploy_env" >> test.properties
                    echo "【信息】构建版本是 $version" >> test.properties
                    """
                }
            }
        }
        stage("测试") {
            steps{
                dir ("${env.WORKSPACE}") {
                    sh """
                    echo "【信息】是否存在test.properties？"
                    if [ -s test.properties ]
                    then 
                        cat test.properties
                        echo "【信息】存在"
                    else
                        echo "【信息】不存在test.properties"
                    fi
                    """
                    echo "【信息】构建完成"
                }
            }
        }        
        stage("测试java maven") {
            steps{
            	sh """
            	java -version
            	mvn -v
            	"""
            }
        }
    }
}


安装maven
wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
yum -y install apache-maven
1、查找包路径
rpm -qa|grep apache-maven

2、根据包路径查找安装目录
rpm -ql java-1.8.0-openjdk-devel-1.8.0.282.b08-1.el7_9.x86_64

Jenkins配置Java、Maven
Dashboard -> 全局工具配置
	新增JDK，设置JAVA_HOME
	新增Maven，设置MAVEN_HOME

新增一个stage测试
    stage("测试java maven") {
        steps{
        	sh """
        	java -version
        	mvn -v
        	"""
        }
    }



#############Jenkins文档##############
由于流水线代码（特别是脚本式流水线）是使用类似 Groovy 的语法编写的, 如果你的IDE不能正确的使用语法高亮显示你的 Jenkinsfile，可以尝试在 Jenkinsfile 文件的顶部插入行 #!/usr/bin/env groovy 纠正这个问题。

steps {
    /* `make check` 在测试失败后返回非零的退出码；
    * 使用 `true` 允许流水线继续进行
    */
    sh 'make check || true' 
}



// 在 Groovy 中使用单引号而不是双引号来定义脚本（``sh`` 的隐式参数）。单引号将使 secret 被 shell 作为环境变量展开。双引号可能不太安全，因为这个 secret 是由 Groovy 插入的
node {
  withCredentials([string(credentialsId: 'mytoken', variable: 'TOKEN')]) {
    sh /* 错误！ */ """
      set +x
      curl -H 'Token: $TOKEN' https://some.api/
    """
    sh /* 正确 */ '''
      set +x
      curl -H 'Token: $TOKEN' https://some.api/
    '''
  }
}


登陆远程主机
#!/usr/bin/env groovy
pipeline {
    agent any
    environment {
        // 用户名与密码类型凭证的使用 https://www.jenkins.io/zh/doc/book/pipeline/jenkinsfile/
        USERNAME_PWD = credentials('192.168.1.150.username.pwd')
    }
    stages {
        stage('Example stage 1') {
            steps {
                script{
                    // 定义远程服务器信息，需要安装 SSH Pipeline Steps
                    def remote = [:]
                    remote.name = "192.168.1.150"
                    remote.host = "192.168.1.150"
                    remote.user = USERNAME_PWD_USR
                    remote.password = USERNAME_PWD_PSW
                    remote.port = 22
                    remote.allowAnyHosts = true

                    // 远程服务器执行shell命令
                    sshCommand remote: remote, command: """
                        echo 登陆远程主机
                        ip a
                    """
                }
            }
        }
    }
}


登陆远程主机的第二种写法
#!/usr/bin/env groovy
pipeline {
    agent any
    environment {
        USERNAME_PWD = credentials('192.168.1.150.username.pwd')
    }
    stages {
        stage('Example stage 1') {
            steps {
                script{
                    // withCredentials方法可通过流水线语法生成器生成
                    withCredentials([usernamePassword(credentialsId: '192.168.1.150.username.pwd', passwordVariable: 'password', usernameVariable: 'username')]) {

                        // 定义远程服务器信息，需要安装 SSH Pipeline Steps
                        def remote = [:]
                        remote.name = "192.168.1.150"
                        remote.host = "192.168.1.150"
                        remote.user = username
                        remote.password = password
                        remote.port = 22
                        remote.allowAnyHosts = true

                        // 远程服务器执行shell命令
                        sshCommand remote: remote, command: """
                            echo 登陆
                            ip a
                        """
                    }                    
                }
            }
        }
    }
}



在pipeline script填写parameter，只有string类型的可用，其他参数没法做到GUI页面覆盖script默认值
pipeline {
    agent any
    parameters {
        string(defaultValue: params.gitUrl ? params.gitUrl : '请填写项目git ssh地址', description: '项目git ssh地址', name: 'gitUrl', trim: true), 
        gitParameter(branch:'', branchFilter: 'origin/(.*)', defaultValue: 'master', description: '', name: 'git_branch', quickFilterEnabled: false, selectedValue: 'NONE', sortMode: 'NONE', tagFilter: '*', type: 'PT_BRANCH', useRepository: 'git@git-pd.megvii-inc.com:ebg-bizpf/platform-sbgp/xuanyuan-web-components.git'),
        gitParameter(branch: '', branchFilter: '.*', defaultValue: '', description: 'tag', name: 'git_tag', quickFilterEnabled: false, selectedValue: 'NONE', sortMode: 'NONE', tagFilter: '*', type: 'PT_TAG', useRepository: 'git@git-pd.megvii-inc.com:ebg-bizpf/platform-sbgp/xuanyuan-web-components.git'),
        choice(choices: params.projectName ? params.projectName : [], description: '项目名', name: 'projectName')
    }
    stages {
        stage('Example') {
            steps {
                echo "参数：${params}"
                sh 'printenv'
            }
        }
    }
}


##############Jenkins集成GItlab自动构建################
1、Jenkins安装 Generic Webhook Trigger
2、打开Jenkins一个流水线Job
3、勾选 构建触发器 Generic Webhook Trigger
4、填写一个Token。Generic Webhook Trigger通过Token区分Job

1、打开Gitlab，设置允许本地网络，地址/admin/application_settings/network
    Outbound requests
    勾选 Allow requests to the local network from web hooks and services
2、打开Gitlab项目，Settings -> Webhooks
    URL 填写 http://JenkinsIP:端口/generic-webhook-trigger/invoke?token=第4步填写的token值
    Push events 填写 test
    SSL verification 取消勾选
3、修改test分支的代码即可触发Jenkins Job









