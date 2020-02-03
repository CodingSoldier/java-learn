# GitLab CI 服务器的搭建

GitLab CI服务器最好是单独与gitlab服务器的一台Linux机器。

## 1. 安装Docker


```
curl -sSL https://get.docker.com/ | sh
```


## 2. 安装gitlab ci runner

```
curl -L https://packages.gitlab.com/install/repositories/runner/gitlab-ci-multi-runner/script.rpm.sh | sudo bash
sudo yum install gitlab-ci-multi-runner -y
```

查看是否运行正常

```
[vagrant@gitlab-ci ~]$ sudo gitlab-ci-multi-runner status
gitlab-runner: Service is running!
[vagrant@gitlab-ci ~]$
```

## 3. 设置Docker权限

为了能让gitlab-runner能正确的执行docker命令，需要把gitlab-runner用户添加到docker group里, 然后重启docker和gitlab ci runner

```
[vagrant@gitlab-ci ~]$ sudo usermod -aG docker gitlab-runner
[vagrant@gitlab-ci ~]$ sudo service docker restart
Redirecting to /bin/systemctl restart docker.service
[vagrant@gitlab-ci ~]$ sudo gitlab-ci-multi-runner restart
```

