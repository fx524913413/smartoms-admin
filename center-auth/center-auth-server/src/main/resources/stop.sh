#!/bin/bash
processname="center-auth-server.jar"
for pid in $(ps aux |grep $processname |grep -v grep|awk '{print $2}'); do
kill -9 $pid

done
