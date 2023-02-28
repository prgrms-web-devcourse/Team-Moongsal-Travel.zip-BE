#!/bin/bash

CURRENT_PORT=$(cat /home/ec2-user/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0
JAR_FILE=$(travel-zip-deploy/build/libs/*.jar)

echo "> Current port of running JAR is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8080 ]
then
  TARGET_PORT=8081
elif [ ${CURRENT_PORT} -eq 8081 ]
then
  TARGET_PORT=8080
else
  echo "> No JAR is connected to nginx"
fi

#TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

#if [ ! -z ${TARGET_PID} ]
#then
#  echo "> Kill JAR running at ${TARGET_PORT}."
#  sudo kill -9 ${TARGET_PID}
#fi

nohup java -jar -Dserver.port=${TARGET_PORT} $JAR_FILE –Dspring.profiles.active=dev > /home/ec2-user/nohup.out 2>&1 &
echo "> Now new JAR runs at ${TARGET_PORT}."

echo "> Start health check of JAR at 'localhost:${TARGET_PORT}' ..."

for RETRY_COUNT in 1 2 3 4 5 6 7 8 9 10
do
    echo "> #${RETRY_COUNT} trying..."
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}"  localhost:${TARGET_PORT}/api/healths)

    if [ ${RESPONSE_CODE} -eq 200 ]
    then
        echo "> New JAR successfully running"
        exit 0
    elif [ ${RETRY_COUNT} -eq 10 ]
    then
        echo "> Health check failed."
        exit 1
    fi
    sleep 12
done

echo "> Nginx currently proxies to ${CURRENT_PORT}."

# Change proxying port into target port
echo "set \$service_url localhost:${TARGET_PORT};" | tee /home/ec2-user/service_url.inc

echo "> Now Nginx proxies to ${TARGET_PORT}."

# Reload nginx
sudo service nginx reload

echo "> Nginx reloaded."

TARGET_PID=$(lsof -Fp -i TCP:${CURRENT_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

echo "$TARGET_PID"

if [ ${TARGET_PID} -gt 0 ]
then
  echo "> Kill JAR running at ${CURRENT_PORT}."
  sudo kill -9 ${TARGET_PID}
fi

exit 0

