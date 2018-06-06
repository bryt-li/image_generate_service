#!/bin/bash
VERSION=0.0.1
ENV=$1
APP_NAME=image_generate_service

if [ -z "$ENV" ];then
	echo "must specify the deployment environment."
	echo "./startup [local/develop/staging/product]"
	exit -1
fi

nohup target/$APP_NAME-$VERSION.jar --spring.profiles.active=$ENV > /dev/null 2>&1 &