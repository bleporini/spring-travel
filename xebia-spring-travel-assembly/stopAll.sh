#!/bin/bash

echo Shutting down Tomcat
kill -9 `jps -v | grep apache-tomcat| awk '{print $1}'`

echo Shutting down HSQLDB
kill `jps -v | grep " Server"| awk '{print $1}'`

echo Shutting down JConsole
kill `jps -v | grep "JConsole"| awk '{print $1}'`

echo Shutting down JMeter
kill `jps -v | grep "ApacheJMeter.jar"| awk '{print $1}'`

