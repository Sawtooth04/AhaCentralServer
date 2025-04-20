FROM tomcat:10.1-jre21
LABEL authors="Sawtooth"
ARG AHA_CENTRAL_PORT
EXPOSE ${AHA_CENTRAL_PORT}
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./target/aha-central-server.war /usr/local/tomcat/webapps/ROOT.war
RUN sed -i "s/port=\"8080\"/port=\"${AHA_CENTRAL_PORT}\"/" /usr/local/tomcat/conf/server.xml
ENV CATALINA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000"
CMD ["catalina.sh", "run"]