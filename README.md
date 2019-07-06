#Log parse application

Small app to parse web server access log file, loads the log to MySQL and checks if a given IP 
makes more than a certain number of requests for the given duration. 

To build the jar run "mvn clean install" (you need Maven installed).

Before running, do the MySQL connection configuration in config/application.properties

To run use a command like this:

    java -jar parser-1.0.jar --accesslog="C:\Users\Remus\Desktop\access.log" --startDate=2017-01-01.15:00:00 --duration=hourly --threshold=200
    
The file "sql" contains simple sql queries agains the DB.