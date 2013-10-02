#!/bin/bash
datepostfix=`date "+%d%m%y%H%M%s"`

echo $datepostfix
catalina_home=../xebia-spring-travel-environment-installer/apache-tomcat-7.0.22/
mv $catalina_home/webapps/ROOT.war $catalina_home/webapps/ROOT.war.$datepostfix
mv target/*.war $catalina_home/webapps/ROOT.war
rm -rf $catalina_home/webapps/ROOT/

