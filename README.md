# Java_Training

run.sh

#!/bin/sh
echo "build command****"
mvn clean install
echo "run command *****"
java -jar /Users/unthinkable/.m2/repository/com/get_user_service/get-service/0.0.1-SNAPSHOT/get-service-0.0.1-SNAPSHOT.jar

