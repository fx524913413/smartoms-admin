#!/bin/bash
processname="center-admin-v2.jar"
for pid in $(ps aux |grep $processname |grep -v grep|awk '{print $2}'); do
kill -9 $pid

done
