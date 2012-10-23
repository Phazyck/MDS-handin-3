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