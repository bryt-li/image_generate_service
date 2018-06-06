#!/bin/bash

VERSION=0.0.1
ACTUATOR_PORT=50001
APP_NAME=image_generate_service

PID="$(jps|grep $APP_NAME-$VERSION.jar|grep -v grep|awk '{print $2}')"

if [ -z "$PID" ];then
	echo "service is not running."
	exit 0
fi

echo "Existing process found, stop service."
while [ -n "$PID" ]; do
    #echo "killing..."
    #echo "$PID"
	#kill -9 $PID
    #sleep 1
    echo "shutdown....."
    curl -X POST 127.0.0.1:$ACTUATOR_PORT/actuator/shutdown
	sleep 5
	PID="$(jps|grep $APP_NAME-$VERSION.jar|grep -v grep|awk '{print $2}')"
done

echo "service is stopped."