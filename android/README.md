#KudoMessage - Jessica

##What is Jessica?
Jessica one way for KudoMessage to reach out to the world, by SMS on an Android smartphone.

##What does it do?
When Hustler has uploaded a new message to Gmail it delivers the message to Jessica via Google Cloud Messaging.
It is then Jessicas job to send it as an SMS to who ever is supposed to get it.

But Jessicas job doesn't stop there. When the phone receives a new text it gets it to Hustler, which then upload the message to Gmail and gets it to all the clients.

###How does it do it?
####Hustler
For Hustler to be able to reach out to Jessica it uses a service provided by Google called Google Cloud Message, it's a push service.

When ever Jessica wants to say something to Hustler it does so with the use of sockets.
