package chat.client;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class Client {
    String username;
    String password;
    String server;
    Connection connection;

    public Client(String username, String password, String host){
        this.username = username;
        this.password = password;
        this.server = host;
    }

    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public String getServer(){
        return this.server;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setServer(String server){
        this.server = server;
    }

    public void connect(){
        ConnectionConfiguration config = new ConnectionConfiguration(this.server, 5222);
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        connection = new XMPPConnection(config);
        try {
            connection.connect();
            System.out.println("Connected to " + this.server);
        } catch (XMPPException e) {
            System.out.println("Failed to connect to " + this.server);
            e.printStackTrace();
        }
    }

    public void login(){
        try {
            connection.login(this.username, this.password);
            System.out.println("Logged in as " + this.username);
        } catch (XMPPException e) {
            e.printStackTrace();
            System.out.println("Failed to log in as " + this.username);
        }
    }

    public void sendMessage(String message, String to){
        // send a message to a user
        ChatManager chatmanager = connection.getChatManager();
        Chat chat = chatmanager.createChat(to, new MessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                System.out.println("Received message: " + message.getBody());
            }
        });
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    public void dissconect() throws XMPPException{
        connection = new XMPPConnection(this.server);
        connection.disconnect();
        System.out.println("Disconnected from " + this.server);
    }
}
