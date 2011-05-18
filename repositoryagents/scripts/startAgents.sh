#! /bin/sh

conf=conf/

echo "Please choose a number and then press enter:"
count=0
for i in $( ls $conf )
do
  if [ $conf/$i ]
  then
    echo $count: $i
    count=`expr $count + 1`
  fi
done

read number

count=0
for i in $( ls $conf )
do
  if [ $conf/$i ]
  then
    if [ $number -eq $count ]
    then
      conf+=$i
    fi
    count=`expr $count + 1`
  fi
done

echo "Starting agents: $conf"

#java -ea -Dcom.sun.management.jmxremote -Xss10000k -Xmx600M  -#DentityExpansionLimit=1000000 -DelementAttributeLimit=1000000 -#Djava.rmi.server.hostname=$hostname -jar $server --conf $conf | tee #backup/$date-$hostname.log

cd $conf
java -ea -jar ../../../target/repositoryagents-0.3.jar
