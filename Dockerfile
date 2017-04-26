FROM tomcat:8-jre8
WORKDIR /usr/local/tomcat
RUN rm -rf webapps
RUN mkdir webapps
COPY cs-rest/target/cs-rest.war webapps/
COPY context.xml conf