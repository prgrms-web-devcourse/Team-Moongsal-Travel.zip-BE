#!/bin/bash

TARGET_PID=0
TARGET_PID=$(lsof -Fp -i TCP:8080 | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

echo "$TARGET_PID"

if [ ${TARGET_PID} -gt 0 ]
then
  echo "> Kill JAR running at ${CURRENT_PORT}."
  sudo kill -9 ${TARGET_PID}
fi