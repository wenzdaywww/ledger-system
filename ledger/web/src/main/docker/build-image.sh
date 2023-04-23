#!/bin/bash
# 删除运行中的应用
docker rm -f ledger
# 延时3秒
sleep 3s
# 删除镜像
docker rmi -f ledger
sleep 3s
# 创建镜像
docker build -f Dockerfile -t ledger .