#FROM openjdk:17
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]



FROM openjdk:17
EXPOSE 8080
ADD target/electronic-store.jar electronic-store.jar
ENTRYPOINT ["java","-jar","/electronic-store.jar"]