#!/bin/bash
# 运行应用
docker run -d -p 8090:8090 --name ledger --restart always -e EUREKA_INSTANCE_IP-ADDRESS=192.168.1.140 -v /etc/localtime:/etc/localtime:ro -v /home/www/ap/ledger/logs/:/home/www/ap/ledger/logs/ -v /home/www/ap/ledger/doc/:/home/www/ap/ledger/doc/ ledger