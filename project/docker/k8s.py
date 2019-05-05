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















