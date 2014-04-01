#!/bin/sh

usage()
{
  echo ""
  echo "  Usage: Please execute script with non-root user."
  echo ""
}

id|grep root
if [ $? -eq 0 ];
then
  usage
  exit -5
fi

prj_dir=`pwd`

cat config.properties.sample |awk -F'=' -v pwd=$prj_dir '
/project.folder/{ $2=pwd; printf("%s=%s\n",$1,$2); next }
/data.folder/{ $2=pwd; printf("%s=%s/data\n",$1,$2); next }
{print}
' > config.properties

sbt assembly

cp $prj_dir/target/scala-2.10/*assembly*.jar $prj_dir/bin/

export DEVICE_INSPECTOR_DIR=$prj_dir
export PATH=$prj_dir/bin:$PATH

echo "export DEVICE_INSPECTOR_DIR=$prj_dir" >> $HOME/.bashrc
echo "PATH=$prj_dir/bin:\$PATH" >> $HOME/.bashrc
