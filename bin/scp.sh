#!/bin/sh

usage()
{
   echo ""
   echo "  Usage:"
   echo "    Please put a config.properties config file under directory."
   echo ""
}


CONF="$DEVICE_INSPECTOR_DIR/config.properties"
if [ ! -f $CONF ]; then
  usage
  exit 5
fi

dir=`cat $CONF           |grep -v "\ *#"|grep "data.folder" |cut -d= -f2`
user=`cat $CONF          |grep -v "\ *#"|grep "backend.user"|cut -d= -f2`
password=`cat $CONF      |grep -v "\ *#"|grep "backend.password"|cut -d= -f2`
ip=`cat $CONF            |grep -v "\ *#"|grep "backend.ip"|cut -d= -f2`
target_folder=`cat $CONF |grep -v "\ *#"|grep "backend.folder"|cut -d= -f2`

today=`date +"%Y%m%d"`
yesterday=`date -d "1 days ago" +"%Y%m%d"`

cd $dir
rm -f *.tar *.gz > /dev/null 2>&1
host=`ls |grep "^[0-9].*\.dat"|head -n 1|xargs -n 1 basename |awk '{str=substr($0,12);gsub(/\.dat$/,"",str ); print str}'`

for d in $today $yesterday
do
        ls ${d}*.dat > /dev/null 2>&1
        if [ $? -ne 0 ]; then continue; fi

	archive="${d}_${host}.tar"

	tar cf $archive ${d}*.dat
        echo "y" | gzip $archive 

        echo "scp -o stricthostkeychecking=no ${archive}.gz $user:$password@$ip:$target_folder"
        scp -o stricthostkeychecking=no ${archive}.gz $user@$ip:$target_folder

done
