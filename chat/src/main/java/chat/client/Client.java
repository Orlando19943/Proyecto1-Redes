package chat.client;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.debugger.SmackDebugger;

public class Client {
    String username;
    String password;
    String server;
    Connection connection;
    private boolean debug;

    public Client(String username, String password, String host, boolean debug){
        this.username = username;
        this.password = password;
        this.server = host;
        this.debug = debug;
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

    public boolean connect(){
        ConnectionConfiguration config = new ConnectionConfiguration(this.server, 5222);
        config.setDebuggerEnabled(this.debug);
        config.setSendPresence(true);
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        connection = new XMPPConnection(config);
        try {
            connection.connect();
            return true;
        } catch (XMPPException e) {
            return false;
        }
    }

    public boolean login(){
        try {
            connection.login(this.username, this.password);
            System.out.println("Logged in as " + this.username);
            return true;
        } catch (XMPPException e) {
            //e.printStackTrace();
            System.out.println("Failed to log in as " + this.username + this.password);
            return false;
        }
    }
    public void createUser(){
        try {
            connection.getAccountManager().createAccount(this.username, this.password);
            System.out.println("Created account " + this.username);
        } catch (XMPPException e) {
            System.out.println("Failed to create account " + this.username);
        }
    }
    public void deleteUser(){
        try {
            connection.getAccountManager().deleteAccount();
            System.out.println("Deleted account " + this.username);
        } catch (XMPPException e) {
            System.out.println("Failed to delete account " + this.username);
        }
    }

    public void getAllUsers() throws XMPPException{
        connection.getRoster().addRosterListener(new RosterListener() {
            public void entriesAdded(java.util.Collection<String> addresses) {
                System.out.println("Entries added: " + addresses);
            }
            public void entriesDeleted(java.util.Collection<String> addresses) {
                System.out.println("Entries deleted: " + addresses);
            }
            public void entriesUpdated(java.util.Collection<String> addresses) {
                System.out.println("Entries updated: " + addresses);
            }
            public void presenceChanged(Presence presence) {
                System.out.println("Presence changed: " + presence);
            }
        });
    }

    public void addUser(String user){
        try {
            connection.getRoster().createEntry(user, user, null);
            System.out.println("Added user " + user);
        } catch (XMPPException e) {
            System.out.println("Failed to add user " + user);
        }
    }

    public void sendMessage(String message, String to){
        // send a message to a user
        ChatManager chatmanager = connection.getChatManager();
        Chat chat = chatmanager.createChat(to, new MessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                //System.out.println("Received message: " + message.getBody());
            }
        });
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
    public void recieveMessage(){
        connection.addPacketListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                System.out.println(message.getFrom() + ": " + message.getBody());
            }
        }, new MessageTypeFilter(Message.Type.chat));
        
    }

    public void disconect() throws XMPPException{
        connection.disconnect();
        System.out.println("Disconnected from " + this.server);
    }
}
