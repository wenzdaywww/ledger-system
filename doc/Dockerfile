FROM java:8
COPY *.jar /ledger-system-1.0.0-exec.jar
CMD ["--server.port=8090"]
VOLUME "/home/www/ap/ledger-system/logs/"
VOLUME "/home/www/ap/ledger-system/doc/"
EXPOSE 8090
ENTRYPOINT ["java","-jar","/ledger-system-1.0.0-exec.jar"]