# 该镜像需要依赖的基础镜像
FROM openjdk:8u141-jdk-slim
# 将当前目录下的jar包复制到docker容器的/目录下
ADD target/account-book-1.0-SNAPSHOT.jar accountBook.jar
# 指定docker容器启动时运行jar包
ENTRYPOINT ["java", "-jar","accountBook.jar"]
# 指定维护者的名字
MAINTAINER lin.zl