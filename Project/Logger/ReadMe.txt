This example is for MongoDB, but can be applied to other database backends. In that example we will use a Frontend and a Backend manager. The Frontend manager will collect the values from WinCC OA by simple dpQueryConnects (which can be configured in the frontend.config file, also multiple connects and be definied). It will pack single value changes to blocks and offer that blocks by ZeroMQ to Backend managers. The Backend manager(s) will get the blocks from ZeroMQ from the Frontend and will write the values to the database. Multiple Backend managers can be connected to one single Frontend manager. The Backend manager can buffer values and also store it on disk if there are too many blocks which cannot be written to the database (database to slow or down for some reason). 

                WinCC OA ==> Frontend Manager ==> Backend Manager ==> Database

Offering values to ZeroMQ in the Frontend manager can also be replaced by writing values directly to a database. ZeroMQ is one implementation of a "Data Sink", it can also be the Data Sink which is used by the Backend Manager to write values to a database. The advantage of having Frontend and Backend managers is that many Backends can be connected to one Frontend manager, WinCC OA will only be affected by one manager instead of multiple managers.

*) Copy the content of the example project to your project or include the example as a sub project
   Note: if you add it as a sub project you still have to copy the content of the bin directory, 
         because Java does not know/care about the WinCC OA project hirarchy. Or set the "userDir"
         in the WinCC OA config file to the example directory.

*) Configs:
   - config/config.level => see section java_10 (Frontend) and java_11 (Backend MongoDB)
   - config/frontend.config
   - config/backend-MongoDB.config => set the mongodb.server.0.url to your MongoDB instance

*) Start Frontend Manager
   WCCOAjava -num 10 -c at/rocworks/oa4j/logger/logger/Frontend -logger config/frontend.config

*) Create MongoDB Collection
    use pvss
    db.createCollection("events");
    db.events.createIndex({ts:1});
    db.events.createIndex({tag:1,ns:1},{unique: true});
    db.events.createIndex({tag:1,ts:1},{unique: false});  

   Note: ns ... nanoseconds since epoch
         ts ... json/bson date datatype
   ns is needed because ts can not store nano seconds

*) Start Backend Manager
   can be started from the console:
   WCCOAjava -num 11 -c at/rocworks/oa4j/logger/logger/Backend -logger config/backend-mongodb.config

   or the backend can be started as an OA independent program from command line
   CMD> Backend-MongoDB.bat
