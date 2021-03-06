@echo off

echo Starting HSQLDB
cd hsqldb/data/
start java -Xmx124m -classpath ../hsqldb-${hsqldb.version}.jar org.hsqldb.server.Server --database.0 file:bookings --dbname.0 bookings --database.1 file:fraud --dbname.1 fraud

cd ../..

echo Starting Tomcat
cd apache-tomcat-${tomcat.version}/bin
start catalina.bat run

cd ../..

echo Starting JMeter
cd apache-jmeter-${jmeter.version}/bin
start jmeter.bat -t ../../apache-jmeter-${jmeter.version}/data/xebia-spring-travel.jmx

cd ../..

echo Starting JConsole
start jconsole
