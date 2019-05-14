FROM java
MAINTAINER docker_maven docker_maven@email.com
WORKDIR /root
CMD ["java", "-version"]
CMD ["mkdir", "upload"]
VOLUME ["/root/upload"]
ADD api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
