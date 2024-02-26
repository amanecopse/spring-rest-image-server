# 스프링 프레임워크 REST 이미지 업로드 서버

스프링 프레임워크 기반 이미지 업로드 REST API 서버

## 1. 프로젝트 구성

- Java 17
- Spring Framework 6.0.16
- Gradle

## 2. 서버 실행
### (1) src/main/resources/config 에 생성
```properties
# application.properties
file-upload-path=/usr/local/tomcat/image
```
```properties
# swagger.properties
springdoc.swagger-ui.path=/index.html
springdoc.api-docs.enabled=true
```
### (2) 이미지 빌드
```shell
 docker buildx build -t tomcat .
```
### (3) 컨테이너 실행 (윈도우)
C:\image 이외의 경로로 이미지를 저장하고 싶을 경우 원하는 경로로 변경할 것
```shell
 docker run --name tomcat -p 8190:8080 -v C:\image:/usr/local/tomcat/image tomcat
```