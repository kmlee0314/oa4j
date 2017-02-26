set BACKEND=NoSQLMongoDB
set DBSCONF=config/backend-mongodb.config
set DBSLIBS=./bin/mongo-java-driver-3.2.2.jar

set LOGLIBS=../Logger/target/classes;../Backends/%BACKEND%/target/classes
set STDLIBS=./bin/gson-2.7.jar;./bin/json-simple-1.1.1.jar;./bin/jeromq-0.3.5.jar;./bin/protobuf-java-2.6.1.jar

java -cp ./bin;./bin/WCCOAjava.jar;%STDLIBS%;%LOGLIBS%;%DBSLIBS% at/rocworks/oa4j/logger/logger/Backend -logger %DBSCONF%