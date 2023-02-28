#!/bin/bash
BUILD_WAR=$(ls /home/ec2-user/app/*.war)
WAR_NAME=$(basename $BUILD_WAR)
echo "## build 파일명: $WAR_NAME"

echo "## build 파일 복사"
DEPLOY_PATH=/home/ec2-user/
cp $BUILD_WAR $DEPLOY_PATH

CURRENT_PID=$(pgrep -f $WAR_NAME)
echo "## 현재 실행중인 애플리케이션 pid: $CURRENT_PID"

if [ -z $CURRENT_PID ]
then
  echo "## 현재 구동중인 애플리케이션 없음"
else
  echo "## kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_WAR=$DEPLOY_PATH$WAR_NAME
echo "## DEPLOY_WAR: $DEPLOY_WAR"
nohup java -jar $DEPLOY_WAR  > $DEPLOY_PATH/nohup.out 2>&1 &