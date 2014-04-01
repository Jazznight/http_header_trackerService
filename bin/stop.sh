#!/bin/sh

pid=`cat $DEVICE_INSPECTOR_DIR/run/run.pid`
if [ $? -eq 0 ]; then
  kill $pid
fi

ps -ef|grep Dconfig.*assembly.*jar|grep -v grep|awk '{print $2}'|xargs -n 1 kill
