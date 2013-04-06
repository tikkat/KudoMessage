#KudoMessage - Torsken

##What is Torsken?
Torsken is one way for the end user to use KudoMessage, it's a web application. Torsken will later on be integrated to a desktop application (Erland) portable to all the major operating systems (OSX, Windows and Linux). The application itself will just be a webkit that holds a website(read on about Erland). The website will be built with Java EE.

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
When ever Torsken speaks with Hustler it does so with the use of sockets. Torsken will send JSON objects to Hustler. The object will consist of several strings such as: Access Token, Message (the SMS) and the Number to the receiving device.

####OAuth
When you first start the KudoMessage application you will be asked to login with your Google account and accept some user consent. After this you will be redirected to a new home page (hosted on Torsken). In the URL google will provide a Access Code for us and through some HTTPClient services we can connect to Google and exchange that code for the actuall AccessToken.

####Java EE
This application will be developed in Java EE with Glassfish as server host.
