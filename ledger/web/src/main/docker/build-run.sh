#!/bin/bash
# 应用名称
app_name=ledger
# 删除运行中的应用
docker rm -f ${app_name}
# 延时3秒
sleep 3s
# 删除镜像
docker rmi -f ${app_name}
sleep 3s
# 创建镜像
docker build -f Dockerfile -t ${app_name} .
sleep 8s
# 运行应用
docker run -d -p 8090:8090 --name ${app_name} --restart always -v /etc/localtime:/etc/localtime:ro -v /home/www/ap/ledger/logs/:/home/www/ap/ledger/logs/ -v /home/www/ap/ledger/doc/:/home/www/ap/ledger/doc/ ${app_name}