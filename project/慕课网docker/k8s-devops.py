kubectl create -f pod_nginx.yml 

kubectl get pods

# 格式化输出更多信息
kubectl get pods -o wide
# 进入容器中
docker exec -it dad893207d47 sh
# 查看网络
docker network ls
# 网络详情
docker inspect NET-WORD-ID

# 查看pod
kubectl get pod
# 通过pod进入容器，有多个容器默认进入第一个容器
kubectl exec -it nginx /bin/bash
# pods详情
kubectl describe pods nginx



kubectl create -f rc_nginx.yml 
# 删除pod
kubectl delete pods nginx-cxvb6
# pod数量还是3个，会重启一个pod
kubectl get pod
# 改变replicas
kubectl scale rc nginx --replicas=2
# 删除部署
kubectl delete -f rc_nginx.yml

kubectl create -f rs_nginx.yml 


kubectl create -f deployment_nginx.yml 
kubectl get deployment
# 修改版本
kubectl set image deployment nginx-deployment nginx=nginx:1.13
# 查看版本
kubectl get deployment -o wide
# 查看历史
kubectl rollout history deployment nginx-deployment
# 回退版本
kubectl rollout undo deployment nginx-deployment
# 查看版本
kubectl get deployment -o wide


kubectl create -f deployment_nginx.yml 
# 暴露端口
kubectl expose deployment nginx-deployment --type=NodePort

kubectl get svc 

kubectl delete -f deployment_nginx.yml 

kubectl create -f pod_busybox.yml 
kubectl create -f pod_nginx.yml 
kubectl get pods -o wide
# 在busybox中可以ping通 pod_nginx
kubectl exec -it busybox-pod sh
# 在master、node节点上也可ping通pod_nginx、pod_busybox

# 暴露服务，服务ip是10.98.121.43，但只能在集群中访问
kubectl expose pods nginx-pod
# svc表示service
kubectl get svc

kubectl create -f pod_nginx.yml
service_nginx.yml
修改nodePort: 32333
kubectl create -f service_nginx.yml
# 浏览器访问
http://192.168.4.151:32333/

#################yml文件label作用######################
# 展示node的label
kubectl get node --show-labels
# 给node设置label
kubectl label node k8s02 hardware=good

pod_busybox_label.yml
  nodeSelector:
    hardware: good
# 创建pod，k8s02有label=good，pod会在k8s02创建
kubectl create -f pod_busybox_label.yml    



#################容器监控####################
docker top 容器id
# 容器占用系统资源情况
docker stats

# weaveworks监控工具
sudo curl -L git.io/scope -o /usr/local/bin/scope
sudo chmod +x /usr/local/bin/scope
scope launch 192.168.4.160
提示通过  http://192.168.4.241:4040/  访问

# 没生效
sudo curl -L git.io/scope -o /usr/local/bin/scope
sudo chmod +x /usr/local/bin/scope
/usr/local/bin/scope launch 192.168.4.241 192.168.4.152




##################heapster，安装成功，但是没有监控信息######################
# 容器安装在从节点，在从节点拉取镜像
docker pull mirrorgooglecontainers/heapster-grafana-amd64:v4.4.3
docker pull mirrorgooglecontainers/heapster-amd64:v1.4.2
docker pull mirrorgooglecontainers/heapster-influxdb-amd64:v1.3.3

docker tag mirrorgooglecontainers/heapster-grafana-amd64:v4.4.3 k8s.gcr.io/heapster-grafana-amd64:v4.4.3
docker tag mirrorgooglecontainers/heapster-amd64:v1.4.2 k8s.gcr.io/heapster-amd64:v1.4.2
docker tag mirrorgooglecontainers/heapster-influxdb-amd64:v1.3.3 k8s.gcr.io/heapster-influxdb-amd64:v1.3.3

# 主节点安装
kubectl create -f influxdb.yaml 
kubectl create -f heapster.yaml 
kubectl create -f grafana.yaml 

kubectl get svc -n kube-system
# 主节点 也可以访问grafana
http://192.168.4.151:30413/?orgId=1
admin  admin



#####################gitlab###########################
阅读操作 gitlab-server安装.md

http://192.168.4.162/
root
cpq..123

######################### 安装 gitlab ci runner ##############################
阅读操作 gitlab-ci安装.md

gitlab-ci-multi-runner register
http://192.168.4.162
提示输入token
Settings ——> CI/CD ——> Runners(Expand) ——> 复制token，粘贴
回车。输入test,demo。回车。回车。shell

gitlab-ci-multi-runner list

# gitlab查看
http://192.168.4.162/demo/helloworld/settings/ci_cd

项目根目录下新建 .gitlab-ci.yml

stages:
  - build
  - test
  - deploy

job1:
  stage: test
  tags:
    - demo
  script:
    - echo "I am job1"  
    - echo "I am in test stage"

job2:
  stage: build
  tags:
    - demo
  script:
    - echo "I am job2"
    - echo "I am in build stage"

job3:
  stage: deploy
  tags:
    - demo  
  script:
    - echo "I am job3"
    - echo "I am in build stage"

Jobs ——> 点击#即可看到输出日志

shell脚本运行失败，则ci、cd失败


新建project ——> Import project ——> git Repo by URL ——> 网页的httpsurl



安装docker类型的runner，避免机器本地安装太多依赖（比如java、python、JS）
gitlab-ci-multi-runner register
http://192.168.4.162
提示输入token
Settings ——> CI/CD ——> Runners(Expand) ——> 复制token，粘贴
描述 输入 描述
tags 输入 python2.7
untagged builds   ——>   false
lock Runner to current project   ——>   false
executor  ——>  docker
image  ——>  python:2.7
# 这样本地环境就不需要安装python2.7环境依赖了，只是安装了一个docker image

# 提起把镜像先拉下来
docker pull python:2.7
docker pull python:3.4


http://192.168.4.162/demo/docker-cloud-flask-demo
新建 .gitlab-ci.yml

stages:
  - style
  - test

pep8:
  stage: style
  script:
    - pip install tox  
    - tox -e pep8
  tags:
    - python2.7

unittest-py27:
  stage: test
  script:
    - pip install tox
    - tox -e py27
  tags:
    - python2.7

unittest-py34:
  stage: test
  script:
    - pip install tox
    - tox -e py34
  tags:
    - python3.4

# 添加Pipeline status
Settings ——> CI/CD ——> General pipelines ——> Pipeline status
拷贝Markdown代码到 README.md
就可以显示pipeline的状态了



安装maven的docker
gitlab-ci-multi-runner register
http://192.168.4.162
提示输入token   DF9k-kBNiZYGg8c8sSMB
Settings ——> CI/CD ——> Runners(Expand) ——> 复制token，粘贴
描述 输入 描述
tags 输入 maven
untagged builds   ——>   false
lock Runner to current project   ——>   false
executor  ——>  docker
image  ——>  maven:latest

删除runner
vim /etc/gitlab-runner/config.toml


重启runner
gitlab-runner install -u gitlab-runner
gitlab-runner start


# 部署，权限不够，失败了
stages:
  - style
  - deploy

pep8:
  stage: style
  script:
    - pip install tox  
    - tox -e pep8
  tags:
    - python2.7

docker-deploy:
  stage: deploy
  script:
    - sudo docker build -t falsk-demo .
    - sudo if [ $(docker ps -aq --filter name=web) ]; then docker rm -f web;fi
    - sudo docker run -d -p 5000:5000 falsk-demo
  tags:
    - demo
  only:
    - master



# 不允许直接修改master
stages:
  - style
  - test

style1:
  stage: style
  script:
    - echo *************style*************
  tags:
    - demo

test1:
  stage: test
  script:
    - echo *************test*************
  tags:
    - demo
  only:
    - master    


新建dev，dev合并到master


# 不允许直接改master分支
Settings ——> Repository ——> Protected Branches
Allowed to push  改成 No one


Settings ——> General ——> Merge requests
Only allow merge requests to be merged if the pipeline succeeds


style1:
  stage: style
  script:
    - echo *************style*************
  tags:
    - demo
  except:  # 打标签不执行此job
    - tags 
# 打标签，镜像发布到仓库
docker-image-release:
  stage: release
  script:
    - docker build -t 私有仓库/项目名称:$CI_COMMIT_TAG .
    - docker push -t 私有仓库/项目名称:$CI_COMMIT_TAG
  tags:
    - demo
  only:
    - tags





    
