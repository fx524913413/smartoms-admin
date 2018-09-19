#!/bin/bash
basepath=$(cd `dirname $0`; pwd)
pushd $basepath
JAVA_OPTS="-ms512m -mx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m"
nohup java $JAVA_OPTS  -jar center-gate.jar -Dspring.config.location=application.yml>log 2>&1 &
popd
