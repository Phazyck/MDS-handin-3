TODO:

=JGroupClient=
This class will handle all JGroup business.

JGroupClient should supply whatever the TcpServer needs.
In other words, we still need to figure out how we want this guy and the TcpServer to play together.

=TcpServer=

The TcpServer should have the following constructor:
 TcpServer(String xmlPath)
  - The path to the XML file which it will read/write from/to in the same manner the CalSerializer used to.

This way, any TcpServer can manage its own XML database.
The TcpServer should use the JGroup client to connect to and communicate with a group.


DONE:

=Serializer=
Changes:
 - Turned into an all-purpose generic serializer.

=TcpClient=
Changes:
 - Can now take input through terminal. No more hardcoding! =D
 - Documented.
 - Other minor changes.

=Cal=
No need to edit.

=Task=
No need to edit.

=User=
No need to edit.


