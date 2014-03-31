#!/bin/sh

usage()
{
  echo ""
  echo "  Usage:"
  echo "    Please execute script by [root] permission."
  echo ""
}


id|grep root
if [ $? -ne 0 ];
then
  usage
  exit -5
fi

dpkg -i deb/scala-2.10.3.deb
dpkg -i deb/sbt.deb
