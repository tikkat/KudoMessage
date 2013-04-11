#KudoMessage - Jessica

##What is Jessica?
Jessica one way for KudoMessage to reach out to the world, by SMS on an Android smartphone.

##What does it do?
When Hustler has uploaded a new message to Gmail it notifies Jessica about it.
It is then Jessicas job to get the message from Gmail and send it as an SMS to who ever is supposed to get it.

But Jessicas job doesn't stop there. When the phone receives a new text or the user sends a text Jessica uploads it to Gmail and notifies Hustler about it, 
which then gets it to all the clients.

###How does it do it?
####Gmail
Jessica upload and read new messages under a specific label in your Gmail account. 
For it to be abel to do so without you giving it your Gmail account details it lets Google handle this part using a technology called OAuth.

The way KudoMessage is built is with access tokens using OAuth 2.0.

####Hustler
For Hustler to be able to reach out to Jessica it uses a service provided by Google called Google Cloud Message, it's a push service.

When ever Jessica wants to say something to Hustler it does so by executing urls on Hustler, with certain variables in it.
