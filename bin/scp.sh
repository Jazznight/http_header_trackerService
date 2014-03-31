#!/bin/sh

usage()
{
   echo ""
   echo "  Usage:"
   echo "    Please put a config.properties config file under directory."
   echo ""
}


CONF="./config.properties"
if [ ! -f $CONF ]; then
  usage
  exit 5
fi

dir=`cat config.properties           |grep "run.folder"|cut -d= -f2`
user=`cat config.properties          |grep "backend.user"|cut -d= -f2`
password=`cat config.properties      |grep "backend.password"|cut -d= -f2`
ip=`cat config.properties            |grep "backend.ip"|cut -d= -f2`
target_folder=`cat config.properties |grep "backend.folder"|cut -d= -f2`

today=`date +"%Y%m%d"`
yesterday=`date -d "1 days ago" +"%Y%m%d"`

cd $dir
rm -f *.tar *.gz > /dev/null 2>&1
host=`ls |grep "^[0-9].*\.dat"|xargs -n 1 basename |awk '{str=substr($0,12);gsub(/\.dat$/,"",str ); print str}'`

for d in $today $yesterday
do
	archive="${d}_${host}.tar"

	tar cvf $archive ${d}*.dat
        echo "y" | gzip $archive 

        echo "scp -o stricthostkeychecking=no ${archive}.gz $user:$password@$ip:$target_folder"
        scp -o stricthostkeychecking=no ${archive}.gz $user@$ip:$target_folder

done
