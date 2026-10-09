FROM openjdk:27-rc-oraclelinux9

WORKDIR /srv

COPY iam-service/target/*.jar .
COPY /infra/iam-service/iam-service.sh .

RUN chmod +x iam-service.sh

EXPOSE 8189

ENTRYPOINT ["./iam-service.sh"]