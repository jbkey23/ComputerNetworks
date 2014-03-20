Below are the commands to interact with the server (no quotes required):

REGISTER
-no parameters needed. joins the server. returns client ID

UNREGISTER "clientID"
-remove client from server

JOIN "clientID" "groupName"
-join a group

QUIT "clientID" groupName"
-quit a group

SHUTDOWN
-shutdown the server is on localhost

SEND "clientID" "groupname" "message"
-send a message from clientID to groupname.

POLL "clientID"
-check for queued messages. NOT CURRENTLY WORKING

ACK "clientID"
-respond to message sent from POLL request. NOT CURRENTLY WORKING