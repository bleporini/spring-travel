echo Starting HSQLDB
cd hsqldb/data/
start java -Xmx124m -classpath ../hsqldb-${hsqldb.version}.jar org.hsqldb.server.Server --database.0 file:bookings --dbname.0 bookings --database.1 file:fraud --dbname.1 fraud

cd ../..

echo Starting Tomcat
cd apache-tomcat-${tomcat.version}/bin
start catalina.bat run

cd ../..

echo Starting JMeter
start apache-jmeter-${jmeter.version}/bin/jmeter -t apache-jmeter-${jmeter.version}/data/xebia-spring-travel.jmx

echo Starting JConsole
start jconsole
