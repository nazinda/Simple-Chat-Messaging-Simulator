# Simple-Chat-Messaging-Simulator
## Project Description
Chat Simulator is a Java-based chat application that simulates sending and receiving messages between multiple contacts. It demonstrates the use of design patterns (Singleton, Observer, Builder, and Decorator) and provides a simple GUI interface built with Swing. Messages can be decorated with timestamps and emojis, and the system tracks unread messages per contact.

## How to Run the System
1. Clone the repository:
bash
git clone https://github.com/username/chat-simulator.git
2. Open the project in a Java IDE such as IntelliJ IDEA or Eclipse or VSCode.
3. Compile and run the `Main.java` file.
4. The GUI window will open, showing the contacts on the left and the chat area on the right. Select a contact, type a message, and click **Send**.

## Dependencies
* Java JDK 8+ (or any version supporting Swing)
* No external libraries are required.

## Folder Structure
```
src/
├─ engine/
│  ├─ ChatEngine.java      
│  └─ Observer.java
├─ patterns/
│  ├─ ChatSessionBuilder.java
│  ├─ Message.java        
│  ├─ Decorators.java       
│  ├─ MessageFactory.java
├─ ui/
│  └─ ChatWindow.java
└─ Main.java
```

## Known Issues
* Only basic greetings and responses are handled by the `ChatEngine`.
* GUI styling is minimal and may not scale well with very long messages.
* Messages from multiple contacts arriving at the same time are not queued distinctly.
* Multi-language support is limited to a few hardcoded phrases.

