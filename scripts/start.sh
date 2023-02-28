#!/usr/bin/env bash

TIME_NOW=$(date +%c)

PROJECT_ROOT="/home/ec2-user/travel-zip-deploy"
JAR_FILE="$PROJECT_ROOT/build/libs/travel-0.0.1-SNAPSHOT.jar"

CURRENT_PID=$(pgrep -f $JAR_FILE)

APP_LOG="${PROJECT_ROOT}/application.log"
ERROR_LOG="${PROJECT_ROOT}/error.log"
DEPLOY_LOG="${PROJECT_ROOT}/deploy.log"

# build 파일 복사
echo "${TIME_NOW} > ${JAR_FILE} 파일 복사" >> ${DEPLOY_LOG}
cp ${PROJECT_ROOT}/build/libs/*.jar ${JAR_FILE}

# jar 파일 실행
echo "${TIME_NOW} > ${JAR_FILE} 파일 실행" >> ${DEPLOY_LOG}
nohup java -jar ${JAR_FILE} > ${APP_LOG} 2> ${ERROR_LOG} &

echo "${TIME_NOW} > 실행된 프로세스 아이디 ${CURRENT_PID} 입니다." >> ${DEPLOY_LOG}