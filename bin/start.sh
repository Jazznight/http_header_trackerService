#!/bin/sh

mkdir -p run logs

nohup java -Dconfig=$DEVICE_INSPECTOR_DIR/config.properties -jar $DEVICE_INSPECTOR_DIR/bin/*jar > $DEVICE_INSPECTOR_DIR/logs/server.log 2>&1 &
echo $! > $DEVICE_INSPECTOR_DIR/run/run.pid
