## 工程简介
### 账本管理系统
## 项目部署说明
### 1、安装mysql数据库，推荐使用docker部署
#### 1.1、docker run -d -p 3306:3306 --restart always -v /etc/localtime:/etc/localtime -v /home/www/ap/mysql/conf:/etc/mysql/conf.d -v /home/www/ap/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=www362412 --name mysql mysql --lower-case-table-names=1
#### 1.2、创建数据库ledger及用户ledger_ap/www362412
##### CREATE USER `ledger_ap`@`%` IDENTIFIED WITH mysql_native_password BY 'www362412';
##### GRANT Delete, Insert, Select, Update ON `shop`.* TO `ledger_ap`@`%`;
##### GRANT Delete, Insert, Select, Update ON *.* TO `ledger_ap`@`%`;
#### 1.3、执行表模型及初始化数据脚本
### 2、安装redis，推荐使用docker部署
#### 2.1、上传redis.conf配置到/home/www/ap/redis/conf
#### 2.2、使用docker安装redis
##### docker run -d -p 6379:6379 --restart always --name redis -v /home/www/ap/redis/conf/redis.conf:/etc/redis/redis.conf -v /home/www/ap/redis/data:/data redis:6.2.4 redis-server /etc/redis/redis.conf
### 3、