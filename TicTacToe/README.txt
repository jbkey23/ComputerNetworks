The protocol for my Tic Tac Toe game works as follows:

When the app is opened, the client automatically registers with the server and a clientID is returned to the client.

When the new game button is pressed, the client is prompted to enter the name of the group that he wants to play in. This uses the server's JOIN command.

After this, whenever a button is clicked on the client, the position and current letter is sent to the group so that the other player will also see the move that was just made.