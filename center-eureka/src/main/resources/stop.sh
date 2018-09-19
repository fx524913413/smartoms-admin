#!/bin/bash
processname="center-eureka.jar"
for pid in $(ps aux |grep $processname |grep -v grep|awk '{print $2}'); do
kill -9 $pid

done
