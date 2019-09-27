FROM 172.29.0.13:8082/tomcat-centit:v1
MAINTAINER hzf "hzf@centit.com"
ADD ./meta-form-web/target/*.war /usr/local/tomcat/webapps/centit-meta-form.war
EXPOSE 8080
CMD /usr/local/tomcat/bin/startup.sh && tail -f /usr/local/tomcat/logs/catalina.out