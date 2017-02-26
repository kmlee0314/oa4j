set LOGCONF=config/frontend.config

set LOGLIBS=../Logger/target/classes
set STDLIBS=./bin/gson-2.7.jar;./bin/json-simple-1.1.1.jar;./bin/jeromq-0.3.5.jar;./bin/protobuf-java-2.6.1.jar

java -Djava.library.path=C:/WinCC_OA_Proj/Example/bin -cp ./bin;./bin/WCCOAjava.jar;%STDLIBS%;%LOGLIBS% at.rocworks.oa4j.logger.logger.Frontend -proj Example -logger %LOGCONF% -num 10