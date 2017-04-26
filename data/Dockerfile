FROM sashiaeec/flyway:latest
WORKDIR /opt/flyway 
COPY flyway.conf conf/
COPY sql sql
CMD ["./wait-for-it.sh", "cs-db:3306", "--", "./flyway","migrate"]