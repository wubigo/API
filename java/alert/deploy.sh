#!/usr/bin/env bash

git pull
mvn -Dmaven.test.skip=true package
cp target/timon-alert-0.1.war /opt/apache-tomcat-8.5.42/webapps