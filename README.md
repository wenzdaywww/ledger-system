### 账本管理系统
#### 项目部署说明
* 安装mysql数据库，推荐使用docker部署
    + 执行以下脚本创建mysql服务
    ````
    docker run -d -p 3306:3306 --restart always -v /etc/localtime:/etc/localtime -v /home/www/ap/mysql/conf:/etc/mysql/conf.d -v /home/www/ap/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=www362412 --name mysql mysql --lower-case-table-names=1
    ````
    + 创建数据库ledger及用户ledger_ap/www362412
    ````
    CREATE USER `ledger_ap`@`%` IDENTIFIED WITH mysql_native_password BY 'www362412';
    GRANT Delete, Insert, Select, Update ON `shop`.* TO `ledger_ap`@`%`;
    GRANT Delete, Insert, Select, Update ON *.* TO `ledger_ap`@`%`;
    ````
    + 执行doc文件夹的表模型脚本及初始化数据脚本
* 安装redis，推荐使用docker部署
    + 上传redis.conf配置到/home/www/ap/redis/conf
    + 使用docker安装redis
    ````
    docker run -d -p 6379:6379 --restart always --name redis -v /home/www/ap/redis/conf/redis.conf:/etc/redis/redis.conf -v /home/www/ap/redis/data:/data redis:6.2.4 redis-server /etc/redis/redis.conf
    ````
* 构建ledger的docker镜像
    + 将ledger/web/src/main/docker文件夹中的Dockerfile文件和ledger的jar上传到服务器的/home/www/ap/ledger-system
    + cd进入上传文件的文件夹中，执行ledger/web/src/main/docker文件夹中的build-run.sh脚本创建镜像并运行应用
    + 也可以使用docker插件创建镜像文件，执行Maven窗口的docker:build操作
* 部署前端应用
    + 上传view.conf到/home/www/ap/nginx/conf/conf.d
    + 上传nginx.conf到/home/www/ap/nginx/conf
    + 创建nginx容器，执行以下命令
    ````
    docker run -d --name nginx --restart always -p 80:80 -v /etc/localtime:/etc/localtime:ro -v /home/www/ap/nginx/root:/usr/share/nginx/html -v /home/www/ap/nginx/conf/nginx.conf:/etc/nginx/nginx.conf -v /home/www/ap/nginx/conf/conf.d/:/etc/nginx/conf.d/ docker.io/nginx
    ````
    + view项目执行npm run build-prod 后得到dist文件夹，将dist文件夹上传到/home/www/ap/nginx/root后重启nginx或者重新加载nginx
* 安装内网穿透软件
    + 参考 <https://www.cpolar.com/blog/linux-system-installation-cpolar>
