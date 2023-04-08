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
### 3、构建ledger的docker镜像
#### 3.1、将ledger/web/src/main/docker文件夹中的Dockerfile文件和ledger的jar上传到服务器的/home/www/ap/ledger-system
#### 3.2、cd进入3.1步骤上传文件的文件夹中，执行以下命令构建镜像
##### docker build -f Dockerfile -t ledger .
#### 3.3、3.1和3.2的步骤也可以使用docker插件创建镜像文件，执行Maven窗口的docker:build操作
#### 3.4、执行以下命令创建容器
##### docker run -d -p 8090:8090 --name ledger --restart always -e EUREKA_INSTANCE_IP-ADDRESS=192.168.1.140 -v /etc/localtime:/etc/localtime:ro -v /home/www/ap/ledger/logs/:/home/www/ap/ledger/logs/ -v /home/www/ap/ledger/doc/:/home/www/ap/ledger/doc/ ledger
### 4、部署前端应用
#### 4.1、上传view.conf到/home/www/ap/nginx/conf/conf.d
#### 4.2、上传nginx.conf到/home/www/ap/nginx/conf
#### 4.3、创建nginx容器，执行以下命令
##### docker run -d --name nginx --restart always -p 80:80 -v /etc/localtime:/etc/localtime:ro -v /home/www/ap/nginx/root:/usr/share/nginx/html -v /home/www/ap/nginx/conf/nginx.conf:/etc/nginx/nginx.conf -v /home/www/ap/nginx/conf/conf.d/:/etc/nginx/conf.d/ docker.io/nginx
#### 4.5、view项目执行npm run build-prod 后得到dist文件夹，将dist文件夹上传到/home/www/ap/nginx/root后重启nginx或者重新加载nginx
