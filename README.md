# Proyecto1-Redes

Simple implementation of a Chat using the Xmpp protocol. This proyect was made in Java 17.

## How to run
⚠ Important, in order to execute the program, you should have maven and java 
Compile the program using maven
```sh
   mvn clean compile assembly:single
```
Move to the directory target
```sh
   cd .\target
```
Run the program
```sh
   java -jar .\chat-chat.jar
```

## Features

- [X] Register a new account
- [X] Log in
- [X] Log out
- [X] Delete an account
- [X] Show all users and their states
- [X] Add a user to youy contacts
- [X] Find a user and show their state
- [X] Chat
  - [X] Send Message
  - [X] Receive Messega
  - [X] Send Message to a group chat
  - [X] Receive Messages from a group chat
- [X] Send a Pressence Message
- [X] Notifications
  - [X] Send
  - [X] Receive
- [ ] Files
  - [ ] Send
  - [ ] Receive

## Built With

- Smack 3.2.1
- Smackx 3.1.0
- Maven 3.8.6

## Author

Orlando Osberto Cabrera Mejía
