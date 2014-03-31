#!/bin/sh

pid=`cat run/run.pid`
if [ $? -eq 0 ]; then
  kill $pid
fi
