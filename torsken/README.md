#KudoMessage - Torsken

##What is Torsken?
Torsken is one way for the end user to use KudoMessage, it's a web application.

##What does it do?
With Torsken the end user can write a message, choose a receiver, and send it away using their server application, 
for example their phone.
When Torsken sends the message it does so by getting it to Hustler which then do all the work, smart huh?

But Torsken is far more useful than that. When ever a server application receives a new message it gets delivered 
to Torsken right away, and to the end user, in real time.

###How does it do it?
####Gmail
For Torsken to know who you are without you giving it your Gmail account details it lets Google handle this part using a technology called OAuth.

The way KudoMessage is built is with access tokens using OAuth 2.0.

####Hustler
When ever Torsken speaks with Hustler it does so with the use of sockets.
