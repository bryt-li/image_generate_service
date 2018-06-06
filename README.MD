# Local Run
```
#startup local mysql & init lollipop_db & user by executing all .sql files in ./init_local_db
docker-compose up -d

#start up application
mvn spring-boot:run
./startup local
```
# Local Test API
```
./test-local.sh
```

# Deployment

### Develop Deploy
```
#migrate db in AWS RDS

#start up application
mvn spring-boot:run -Dspring-boot.run.profiles=develop
#or
nohup java -jar target/lollipop-VERSION-SNAPSHOT.jar --spring.profiles.active=develop > /dev/null 2>&1 &
```

### Product Deploy
```
#migrate db in AWS RDS

#start up application
nohup java -jar target/lollipop-VERSION-SNAPSHOT.jar --spring.profiles.active=product > /dev/null 2>&1 &
```
