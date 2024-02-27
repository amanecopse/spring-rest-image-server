FROM tomcat:10.1.18

WORKDIR /usr/local/tomcat
RUN mkdir "project"
COPY . ./project
WORKDIR ./project

RUN apt-get update
RUN apt-get install dos2unix
RUN dos2unix ./gradlew # unix 와 윈도우 간 개행 문제 해결
RUN chmod 777 ./gradlew

RUN ./gradlew build
RUN cp ./build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080