#KudoMessage - The web application
This will be a long readme file, bear with me now.

The KudoMessage web application is really a two part thing. First it's a server backend, called Hustler. Second it's a user front end, called Torsken. You can read more about the two below.

####Techniques
The application is developed with Java EE using Glassfish as application server. It uses tons of other cool things like ICEfaces aswell, more on that below.


#KudoMessage - Hustler

##What is Hustler?
Hustler is the spider in the web that is KudoMessage.

##What does it do?
When a server application, i.g. Jessica, receives a new message and upload it to Gmail it notifies Hustler about it. 
It is then Hustles job to tell all the clients about it and help them get the message from Gmail.

But Hustler does more than that. When a client sends a message it does so by getting it to Hustler who then upload it to Gmail. 
After this is done it notifies the server application about it, which then sends it.

###How does it do it?
####Gmail
Hustler upload and read new messages under a specific label in your Gmail account. 
For it to be abel to do so without you giving it your Gmail account details it lets Google handle this part, 
or more specifically, it lets the clients and the server application let Google handle this part using a technology called OAuth.

The way KudoMessage is built is with access tokens using OAuth 2.0.

####Jessica
To nofify the Android server application Hustler uses a service provided by Google called Google Cloud Message, it's a push service.

#KudoMessage - Torsken

##What is Torsken?
Torsken is one way for the end user to use KudoMessage, it's a web application. Torsken will later on be integrated to a desktop application (Erland) portable to all the major operating systems (OSX, Windows and Linux).

##What does it do?
With Torsken the end user can write a message, choose a receiver, and send it away using their server application, for example their phone. When Torsken sends the message it does so by getting it to Hustler which then do all the work, smart huh?

But Torsken is far more useful than that. When ever a server application receives a new message it gets delivered 
to Torsken right away, and to the end user, in real time. This is done with the help of Hustler.

###How does it do it?
####Gmail
For Torsken to know who you are without you giving it your Gmail account details it lets Google handle this part using a technology called OAuth.

The way KudoMessage is built is with access tokens using OAuth 2.0.

####Hustler
When ever Torsken speaks with Hustler it does so by using a way called RESTful, sending JSON objects with all the information.

####OAuth
When you first start the KudoMessage application you will be asked to login with your Google account and accept some user consent. After this you will be redirected back to Torsken and will be able to start using the service.