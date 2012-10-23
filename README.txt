********************************************************************************
    Overview
********************************************************************************
This project contains 11 classes, divided into three packages:

serialization
=============
This package contains the following classes:

- Cal, Tasks, Task, User, Envelope
    These classes represent XML-data, and are capable 
    of serializing/deserializing themselves.

taskmanager
===========
 This package contains the following classes:
    
- TaskManager
    An interface which another class must implement 
    to fulfill the role of a TaskManager.

- FileManager
    A TaskManager that manages it's tasks through the use of a local xml-file.

- GroupManager
    A TaskManager that acts as a proxy for the FileManager.
    By joining a JGroup with other GroupManagers, it keeps its 
    FileManager's state synchronized with the other members' FileManagers.

tcp
===
This package contains the following classes:

- Actor
    An abstract class which, if extended upon provides basic methods
    for communication using Strings via TCP.

- Server
    Acts as a TCP Server for the supplied TaskManager.

- Client
    Acts as a TCP Client to test out the Server.

********************************************************************************
    Execution
********************************************************************************

In order to execute and test this project, you should do the following.

1. Run Server.java.
2. Wait half a minute to make sure the Server is ready for a client.
3. Run Client.java.
4. You should now see some results in the terminal.

********************************************************************************
    Example test runs
********************************************************************************

In the files 'CLIENT_DUMP.txt' and 'SERVER_DUMP.txt', you can see
some terminal output from a test run.
This test run was done by following the execution instructions.

CLIENT_DUMP.txt
===============
In here, you can see what happens at the client side:

The client GETs all tasks related to our group, 'TheHitmen', 
before and after doing any changes to our tasks via POST/PUT/DELETE. 
This way, you can see the incremental changes in our list of tasks.

SERVER_DUMP.txt
===============
In here, you can see what happens at the server side:

Any "getter" command, e.g. GET, is handed over directly to the 
local FileManager, since there's no reason sending these messages to the group. 
This appears, for example like this:

LOCAL          : [GET] [DATA]

Any "setter" commands, e.g. POST/PUT/DELETE, are both handed to the local
FileManager, and sent to the GROUP.

The Server would show the following:

LOCAL -> GROUP : [SET] [DATA]

...meaning that a command has been applied locally and then sent to the group.

Any other Server connected to the group would then show:

LOCAL <- GROUP : [SET] [DATA]

Meaning that a setter command was received from the group and applied locally.

********************************************************************************
    Side-note
********************************************************************************

By initializing the Server with a FileManager, you get the same kind
of Server as en hand-in 1. :-)