# 安装helm


参考链接https://github.com/kubernetes/helm/blob/master/docs/install.md


## 下载helm可执行文件

假如我们的系统是Linux，则(注意版本号，可以按照需求选择)

```
wget https://storage.googleapis.com/kubernetes-helm/helm-v2.8.2-linux-amd64.tar.gz
```

然后把可执行文件添加到 /usr/local/bin下

```
tar zxvf helm-v2.8.2-linux-amd64.tar.gz
rm -rf helm-v2.8.2-linux-amd64.tar.gz
cd linux-amd64/
sudo mv helm /usr/local/bin/
```



## 准备k8s集群

请首先确保kubectl能够正常工作

```
[vagrant@kops-host ~]$ kubectl get node
NAME                                          STATUS    ROLES     AGE       VERSION
ip-172-20-48-147.us-west-1.compute.internal   Ready     node      11m       v1.8.7
ip-172-20-54-223.us-west-1.compute.internal   Ready     master    13m       v1.8.7
ip-172-20-58-107.us-west-1.compute.internal   Ready     node      12m       v1.8.7
[vagrant@kops-host ~]$
```

然后运行 helm init

```
[vagrant@kops-host ~]$ helm init
Creating /home/vagrant/.helm
Creating /home/vagrant/.helm/repository
Creating /home/vagrant/.helm/repository/cache
Creating /home/vagrant/.helm/repository/local
Creating /home/vagrant/.helm/plugins
Creating /home/vagrant/.helm/starters
Creating /home/vagrant/.helm/cache/archive
Creating /home/vagrant/.helm/repository/repositories.yaml
Adding stable repo with URL: https://kubernetes-charts.storage.googleapis.com
Adding local repo with URL: http://127.0.0.1:8879/charts
$HELM_HOME has been configured at /home/vagrant/.helm.

Tiller (the Helm server-side component) has been installed into your Kubernetes Cluster.

Please note: by default, Tiller is deployed with an insecure 'allow unauthenticated users' policy.
For more information on securing your installation see: https://docs.helm.sh/using_helm/#securing-your-helm-installation
Happy Helming!
```

这样我们会在k8s集群里看到一个新的pod

```
[vagrant@kops-host ~]$ kubectl get pods --namespace kube-system
```

这个pod叫`tiller-deploy-f44659b6c-mwkrn` 大家的名字都会以tiller开头

