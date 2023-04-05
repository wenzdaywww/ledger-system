FROM java:8
# 设置时区，保证时间准确
ENV TZ="Asia/Shanghai"
COPY *.jar /ledger-system-1.0.0-exec.jar
CMD ["--server.port=8090"]
# 设置日志输出路径
VOLUME "/home/www/ap/ledger-system/logs/"
# 设置文当保存路径
VOLUME "/home/www/ap/ledger-system/doc/"
EXPOSE 8090
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+08","/ledger-system-1.0.0-exec.jar"]