#!/bin/sh

mkdir -p run logs

cp target/scala-2.10/*assembly*.jar bin/ > /dev/null 2>&1

nohup java -jar bin/*jar > logs/server.log 2>&1 &
echo $! > run/run.pid
