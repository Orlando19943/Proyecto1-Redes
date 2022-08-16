package chat.client;

import java.io.File;
import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.filetransfer.StreamNegotiator;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smack.debugger.SmackDebugger;
import org.jxmpp.jid.Jid;

public class Client {
    String username;
    String password;
    String server;
    XMPPConnection connection;
    ChatManager chatManager;
    PacketListener packetListener;
    RosterListener roasterListener;
    MessageListener messageListener;
    ConnectionCreationListener connectionCreationListener;
    FileTransferManager  fileManager;
    private boolean debug;

    public Client(String username, String password, String host, boolean debug){
        this.username = username;
        this.password = password;
        this.server = host;
        this.debug = debug;
        this.packetListener = new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                System.out.println(message.getFrom() + ": " + message.getBody());
            }
        };
        this.roasterListener = new RosterListener() {
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
                if (presence.getType() == Presence.Type.available && presence.getType() != null) {
                    System.out.println(presence.getFrom() + " se ha conectado");
                } else if (presence.getType() == Presence.Type.unavailable && presence.getType() != null) {
                    System.out.println(presence.getFrom() + " se ha desconectado");
                } else if (presence.getType() == Presence.Type.subscribe && presence.getType() != null) {
                    System.out.println(presence.getFrom() + " se ha suscrito");
                } else if (presence.getType() == Presence.Type.unsubscribe && presence.getType() != null) {
                    System.out.println(presence.getFrom() + " se ha desuscripto");
                } else if (presence.getType() == Presence.Type.subscribed && presence.getType() != null) {
                    System.out.println(presence.getFrom() + " algo xd");
                } else if (presence.getType() == Presence.Type.unsubscribed && presence.getType() != null) {
                    System.out.println(presence.getFrom() + " algo x2 xd");
                }
            }
        };
        this.messageListener = new MessageListener() {
            public void processMessage(Chat chat, Message message) {
                System.out.println(message.getFrom() + ": " + message.getBody());
            }
        };
        this.connectionCreationListener = new ConnectionCreationListener() {        
            @Override
            public void connectionCreated(XMPPConnection arg0) {
                System.out.println("Connection created " + connection);             
            }
        };
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
            System.out.println("Logged in as " + this.username +"@" + connection.getServiceName());
            this.chatManager = connection.getChatManager();
            this.getRoaster();
            this.fileManager = new FileTransferManager(connection);
            return true;
        } catch (XMPPException e) {
            System.out.println("Failed to log in as " + this.username);
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
            if (connection.isAuthenticated()){
                //connection.removePacketListener(packetListener);
                connection.getAccountManager().deleteAccount();
                System.out.println("Deleted account " + this.username);
            } else {
                System.out.println("No se puede eliminar el usuario porque no esta autenticado");
            }
        } catch (XMPPException e) {
            System.out.println("Failed to delete account " + this.username);
        }
    }

    public void getRoaster() throws XMPPException{
        connection.getRoster().addRosterListener(roasterListener);
        connection.getRoster().setSubscriptionMode(SubscriptionMode.manual);
    }

    public String findUser(String user){
        // details of the user
        System.out.println(user + " -> "+ connection.getRoster().getPresence(user).getType());
        return String.valueOf(connection.getRoster().getPresence(user).getType());
    }

    public void changeStatus(int type){
        // 1 = unavailable, 2 = available (default)
        Presence presence = new Presence(Type.available);
        if (type == 1){
            presence = new Presence(Type.unavailable);
        } 
        presence.setStatus("status");
        connection.sendPacket(presence);
    }


    //send request to add user
    public void sendRequest(String user){
        try {
            connection.getRoster().createEntry(user, user, new String[]{"Friends"});
            System.out.println("Solicitud de amistad enviada a " + user);
        } catch (XMPPException e) {
            System.out.println("Failed to send request to " + user);
        }
    }

    public void getFriends(){
        System.out.println("Users: " + connection.getRoster().getEntries());
    }

    public void addFriend(String user){
        try {
            connection.getRoster().createEntry(user, user, null);
            System.out.println("Added user " + user);
        } catch (XMPPException e) {
            System.out.println("Failed to add user " + user);
        }
    }

    public boolean joinRoom(String room, String username){
        Presence joinPresence = new Presence(Presence.Type.available);
        joinPresence.setTo(room + "/" + username);
        connection.sendPacket(joinPresence);
        return true;
    }
    //show all users
    public void showUsers(){
        Connection.addConnectionCreationListener(connectionCreationListener);
    }

    // send file
    public boolean sendFile(String user, String path){
        FileTransferNegotiator.setServiceEnabled(connection, true);
        try {
                FileTransferNegotiator.IBB_ONLY = true;
                OutgoingFileTransfer stream = this.fileManager.createOutgoingFileTransfer(user);
                // when the stream negotiation is done know it can transfer files
                File file = new File(path);
                System.out.println(file.getAbsolutePath());
                // It has an error with the file
                if (!file.canRead()) return false;

                stream.sendFile(file, "file");
                
                while(!stream.isDone()){
                    System.out.println(stream.getProgress());
                    System.out.println(stream.getStatus());
                    System.out.println(stream.getBytesSent());
                }

                return true;
            } catch (XMPPException e) {
                return false;
            }
    }
    public void sendMessage(String message, String to){
        Chat chat = this.chatManager.createChat(to, messageListener);
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageGroup(String message, String room){
        MultiUserChat multiUserChat = new MultiUserChat(this.connection, room);
        try {
            multiUserChat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
    public void recieveMessage(){
        connection.addPacketListener(packetListener, new MessageTypeFilter(Message.Type.chat));
        connection.addPacketListener(packetListener, new MessageTypeFilter(Message.Type.groupchat));
    }


    public void disconect() throws XMPPException{
        connection.disconnect();
        System.out.println("Disconnected from " + this.server);
    }
}
