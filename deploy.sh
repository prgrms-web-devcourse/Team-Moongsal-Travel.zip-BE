#!/bin/bash

APP_NAME=travel
REPOSITORY=/home/ubuntu
RELEASE=production

# .jar 파일 타겟팅
JAR_NAME=$(ls $REPOSITORY | grep jar)
echo $JAR_NAME

# 현재 실행중인 서버가 있으면 잡아서 종료 시킵니다.
CURRENT_PID=$(pgrep -f $APP_NAME)
if [ -z $CURRENT_PID ]
then
  echo ">>>> java process not found."
else
  echo ">>>> PID: $CURRENT_PID kill."
  kill -15 $CURRENT_PID
  sleep 45
fi

# .jar 파일 java 실행합니다.
echo ">>>> $JAR_NAME java execute."
echo "nohup java -jar $JAR_NAME &"
nohup java -jar $JAR_NAME > /dev/null 2>&1 &