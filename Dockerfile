FROM ubuntu:latest
RUN DEBIAN_FRONTEND=noninteractive apt-get update && apt-get install openjdk-17-jre -y
COPY in*.jar /
CMD mkdir /batch && java -jar /in*.jar
EXPOSE 8080
