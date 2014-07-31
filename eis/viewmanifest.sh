#!/bin/sh

if [ $# = "0" ]
then
echo You must provide a path to a jar-file.
else
if [ -f $1 ]
then
unzip -c $1 META-INF/MANIFEST.MF
else
echo File $1 does not exist!
fi
fi
