####################安装gitlab#####################
https://github.com/liuyi01/imooc-docs/blob/master/gitlab-install.md

docker pull registry.cn-hangzhou.aliyuncs.com/imooc/gitlab-ce:latest

cd /app/gitlab

cat <<EOF > start.sh
#!/bin/bash
HOST_NAME=gitlab.mooc.com
GITLAB_DIR=`pwd`
docker stop gitlab
docker rm gitlab
docker run -d \\
    --hostname \${HOST_NAME} \\
    -p 9443:443 -p 9080:80 \\
    --name gitlab \\
    -v \${GITLAB_DIR}/config:/etc/gitlab \\
    -v \${GITLAB_DIR}/logs:/var/log/gitlab \\
    -v \${GITLAB_DIR}/data:/var/opt/gitlab \\
    registry.cn-hangzhou.aliyuncs.com/imooc/gitlab-ce:latest
EOF


vim /etc/hostname 
127.0.0.1 gitlab.mooc.com

要先启动k8s集群
访问
http://192.168.4.164:9080



######################jenkins安装#############################
java -jar jenkins.war --httpPort=8888

密码  e3e2a41c85014fbfa1a9a2241db7c2e5

Pipelines插件全加上

admin
cpq..123


###################gitlab 和 jenkins 集成 #####################
新建流水线类型的job  user-edge-service
勾选Trigger builds remotely 
Authentication Token  瞎写 123456

gitlab
Integrations Settings 
URL填写  http://192.168.4.151:8888/job/user-edge-service/build?token=123456
Add webhook

jenkins保存user-edge-service
全局安全配置  取消   CSRF Protection
勾选 Allow anonymous read access

gitlab webhook test push event 测试



yum install git

git config --global user.name "cpq"
git config --global user.email "237053176@qq.com"
ssh-keygen -t rsa -C "237053176@qq.com"
vim /root/.ssh/id_rsa.pub 
将key加入到gitlab中


jenkins Configure的Pipeline中填入
#!groovy
pipeline {
	agent any
	environment {
		PEPOSITORY="http://192.168.4.164:9080/demo/microservice.git"
	}
	stages {
		stage('获取代码') {
			steps {
				echo "克隆代码 ${PEPOSITORY}"
				deleteDir()
				git credentialsId: '664e5f34-5800-4712-8b4b-9aac78d20345', url: "${PEPOSITORY}"
			}
		}
	}
}

运行第一次构建



k8s01上安装maven，全局配置jenkins，添加maven。
重新开一个shell，重启jenkins

#!groovy
pipeline {
	agent any
	environment {
		PEPOSITORY="http://192.168.4.164:9080/demo/microservice.git"
		MODULE="user-edge-service"
	}
	stages {
		stage('获取代码') {
			steps {
				echo "克隆代码 ${PEPOSITORY}"
				deleteDir()
				git credentialsId: '664e5f34-5800-4712-8b4b-9aac78d20345', url: "${PEPOSITORY}"
			}
		}
		stage('编译') {
			steps {
				echo "编译代码"
				sh "mvn -U -pl ${MODULE} -am clean package"
			}
		}		
	}	
}

# jenkins maven目录
/root/.jenkins/workspace/user-edge-service

docker login 登陆

mkdir /script
cd /script
vim build_image.sh
#!/bin/bash
MODULE=$1
TIME=`date "+%Y%m%d%H%M"`
GIT_REVISION=`git log -1 --pretty=format:"%h"`
IMAGE_NAME=codingsoldier/${MODULE}:${TIME}_${GIT_REVISION}
cd ${MODULE}
docker build -t ${IMAGE_NAME} .
cd -
docker push ${IMAGE_NAME}

# 修改执行权限
chmod 755 build_image.sh



#!groovy
pipeline {
	agent any
	environment {
		PEPOSITORY="http://192.168.4.164:9080/demo/microservice.git"
		MODULE="user-edge-service"
		SCRIPT_PATH="/script"
	}
	stages {
		stage('获取代码') {
			steps {
				echo "克隆代码 ${PEPOSITORY}"
				deleteDir()
				git credentialsId: '664e5f34-5800-4712-8b4b-9aac78d20345', url: "${PEPOSITORY}"
			}
		}
		stage('编译') {
			steps {
				echo "编译代码"
				sh "mvn -U -pl ${MODULE} -am clean package"
			}
		}
		stage('构建镜像') {
			steps {
				echo "构建镜像"
				sh "${SCRIPT_PATH}/build_image.sh ${MODULE}"
			}
		}				
	}	
}




pipeline script from SCM 例子

#!groovy
pipeline {
	agent any
	stages {
		stage('克隆代码') {	
			steps {
				echo "代码分支： ${params.git_branch}"
				// 递归删除旧的代码
				// deleteDir()

				// 通过 Pipeline Syntax 生成语法
				git branch: "${params.git_branch}", credentialsId: 'cb9567cd-88c5-4153-ac24-11051bd8ea6b', url: "${params.git_url}"
			}
		}
		stage('编译代码') {
			steps {
				echo "编译代码，跳过测试用例"
				dir("${pwd()}/${params.project_name}"){
					sh "/usr/local/software/apache-maven-3.6.0/bin/mvn clean package -Dmaven.test.skip=true"
				}
			}
		}
		stage('构建Docker镜像') {
			steps {
				dir("${pwd()}/${params.project_name}/target"){
					sh "cp /usr/local/software/tenant/docker/Dockerfile ${pwd()}/"
					sh "cp /usr/local/software/tenant/docker/run.sh ${pwd()}/"
					sh "chmod a+x run.sh"
					sh "docker build -t ${params.project_name} ."
				}
			}
		}
		stage('运行Docker容器') {
			steps {
				sh "docker rm -f ${params.project_name} || true"
				sh "docker run -d --net='host' --restart=always --name=${params.project_name} -m 620m  ${params.project_name}"
			}
		}		
	}	
}















