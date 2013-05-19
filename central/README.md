#KudoMessage - Hustler

##What is Hustler?
Hustler is the spider in the web that is KudoMessage.

##What does it do?
When a server application receives a new message it gets it to Hustler, which then upload it to Gmail. 
When this is done it is Hustles job to get the message to all the clients, fast, right?

But Hustler does more than that. When a client sends a message it does so by getting it to Hustler who then upload it to Gmail. 
After this is done it notifies the server application about it, and delivers the message, which then sends it.

###How does it do it?
####Gmail
Hustler upload and read new messages under a specific label in your Gmail account. 
For it to be abel to do so without you giving it your Gmail account details it lets Google handle this part, 
or more specifically, it lets the clients and the server application let Google handle this part using a technology called OAuth.

The way KudoMessage is built is with access tokens using OAuth 2.0.

####Android server application
To nofify the Android server application Hustler uses a service provided by Google called Google Cloud Message, it's a push service.
