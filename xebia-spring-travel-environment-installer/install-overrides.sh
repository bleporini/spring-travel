#!/bin/bash

cp overrides/application.properties apache-tomcat-7.0.22/properties/
cp overrides/server.xml apache-tomcat-7.0.22/conf/
cp overrides/logging.properties apache-tomcat-7.0.22/conf/
cp overrides/setenv.sh apache-tomcat-7.0.22/bin/

cp overrides/xebia-spring-travel.jmx jakarta-jmeter-2.5.1/data/
cp overrides/hotelBookings.csv jakarta-jmeter-2.5.1/data
