package chat.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
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
import org.jivesoftware.smackx.filetransfer.IBBTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.jingle.JingleManager;
import org.jivesoftware.smackx.jingle.JingleSession;
import org.jivesoftware.smackx.jingle.nat.HttpServer;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;


/*
 * Client class
 */
public class Client {
    private String username;
    private String password;
    private String server;
    private XMPPConnection connection;
    private ChatManager chatManager;
    private PacketListener packetListener;
    private PacketListener presenceListener;
    private PacketListener groupPacketListener;
    private RosterListener roasterListener;
    private MessageListener messageListener;
    private FileTransferNegotiator fileTransferNegotiator;
    private OutgoingFileTransfer outgoingFileTransfer;
    private IncomingFileTransfer incomingFileTransfer;
    private ConnectionCreationListener connectionCreationListener;
    private FileTransferManager  fileManager;
    private FileTransferListener fileTransferListener;
    private boolean debug;

    /*
     * Constructor
     * @param username -> username to connect to the server
     * @param password -> passwordto connect to the server
     * @param server -> name of the server to connect to
     * @param debug -> true if debug mode is enabled, false otherwise
     */
    public Client(String username, String password, String host, boolean debug){
        this.username = username;
        this.password = password;
        this.server = host;
        this.debug = debug;
        this.packetListener = new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                if (message.getBody() != null) {
                    System.out.println(message.getFrom() + ": " + message.getBody());
                }
            }
        };
        this.presenceListener = new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Presence presence = (Presence) packet;
                System.out.println(presence.getFrom() + " is " + presence.getType());
            }
        };
        this.groupPacketListener = new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                if (message.getBody() != null) {
                    System.out.println(message.getFrom() + ": " + message.getBody());
                }
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
                /* if (presence.getType() == Presence.Type.available && presence.getType() != null) {
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
                } */
            }
        };
        this.messageListener = new MessageListener() {
            public void processMessage(Chat chat, Message message) {
            }
        };
        this.connectionCreationListener = new ConnectionCreationListener() {        
            @Override
            public void connectionCreated(XMPPConnection arg0) {
                System.out.println("Connection created " + connection);             
            }
        };
    }
    /*
     * getter for the username
     * @return username
     */
    public String getUsername(){
        return this.username;
    }
    /*
     * getter for the password
     * @return password
     */
    public String getPassword(){
        return this.password;
    }
    /*
     * getter for the server
     * @return server
     */
    public String getServer(){
        return this.server;
    }
    /*
     * setter for the username
     */
    public void setUsername(String username){
        this.username = username;
    }
    /*
     * setter for the password
     */
    public void setPassword(String password){
        this.password = password;
    }
    /*
     * setter for the server
     */
    public void setServer(String server){
        this.server = server;
    }

    /*
     * Establish connection to the server
     * @return true if connection is established, false otherwise
     */
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

    /*
     * Login to the server, also initialize the chat manager, roster listener, message listener and file transfer manager
     * @return true if login is successful, false otherwise
     */
    public boolean login(){
        try {
            connection.login(this.username, this.password);
            System.out.println("Logged in as " + this.username +"@" + connection.getServiceName());
            this.chatManager = connection.getChatManager();
            this.getRoaster();
            this.receiveMessage();
            this.notifications();
            this.fileManager = new FileTransferManager(connection);
            return true;
        } catch (XMPPException e) {
            System.out.println("Failed to log in as " + this.username);
            return false;
        }
    }
    /*
     * Create a new user account in the server connected to
     */
    public void createUser(){
        try {
            connection.getAccountManager().createAccount(this.username, this.password);
            System.out.println("Created account " + this.username);
        } catch (XMPPException e) {
            System.out.println("Failed to create account " + this.username);
        }
    }
    /*
     * Delete the actual user connected to the server
     */
    public void deleteUser(){
        try {
            if (connection.isAuthenticated()){
                connection.getAccountManager().deleteAccount();
                System.out.println("Deleted account " + this.username);
            } else {
                System.out.println("No se puede eliminar el usuario porque no esta autenticado");
            }
        } catch (XMPPException e) {
            System.out.println("Failed to delete account " + this.username);
        }
    }

    /* 
     * Add a listener to listen the presence of the users in the roster
     */
    public void getRoaster() throws XMPPException{
        connection.getRoster().addRosterListener(roasterListener);
        connection.getRoster().setSubscriptionMode(SubscriptionMode.manual);
    }
    /*
     * Find a user in the roster
     * @param user -> the user to find
     * @return a String with the name of the user, the type of the user and the status of the user (if status is not null)
     */
    public String findUser(String user){
        Collection<RosterEntry> friends = connection.getRoster().getEntries();
        ArrayList<String> friendsList = new ArrayList<String>();
        Presence presence;
        presence = connection.getRoster().getPresence(user);
        if (presence.getStatus() != null){
            return user + " -> " + presence.getType() + " -> " + presence.getStatus();
        } else {
            return user + " -> " + presence.getType();
        }
        //System.out.println(user + " -> "+ connection.getRoster().getPresence(user).getType());
    }

    /*
     * Change the status of the user connected to the server
     * @param type -> the type of the status (1 = unavailable, 2 = available)
     * @param msg -> the message of the status
     */
    public void changeStatus(int type, String msg){
        // 1 = unavailable, 2 = available (default)
        Presence presence = connection.getRoster().getPresence(this.username);
        if (type == 1){
            presence.setType(Presence.Type.unavailable);
        } else{
            presence.setType(Presence.Type.available);
        }
        presence.setStatus(msg);
        connection.sendPacket(presence);
    }

    /*
     * Send a suscription request to a user
     * @param user -> the user to send the suscription 
    */
    public void sendRequest(String user){
        try {
            connection.getRoster().createEntry(user, user, new String[]{"Friends"});
            System.out.println("Solicitud de amistad enviada a " + user);
        } catch (XMPPException e) {
            System.out.println("Failed to send request to " + user);
        }
    }

    /*
     * get the list of the users in the roster
     * @return Object[] -> a collection of the users in the roster, with the name, the type and the status of the user
     */
    public Object[] getFriends(){
        Collection<RosterEntry> friends = connection.getRoster().getEntries();
        ArrayList<String> friendsList = new ArrayList<String>();
        Presence presence;
        for (RosterEntry friend: friends){
            presence = connection.getRoster().getPresence(friend.getUser());
            if (presence.getStatus() != null){
                friendsList.add(friend.getUser() + " -> " + presence.getType() + " -> " + presence.getStatus());
            } else {
                friendsList.add(friend.getUser() + " -> " + presence.getType());
            }
        }
        return friendsList.toArray();
    }
    // TODO
    public void addFriend(String user){
        Presence presence = new Presence(Presence.Type.subscribe);
        presence.setTo(user);
        try {
            connection.getRoster().createEntry(user, user, null);
            System.out.println("Added user " + user);
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
    }

    /*
     * Join to a group chat
     * @param room -> the name of the group chat
     * @param username -> the name of the user in the group chat
     * @return true if the user is joined to the group chat, false otherwise
     */
    public boolean joinRoom(String room, String username){
        Presence joinPresence = new Presence(Presence.Type.available);
        joinPresence.setTo(room + "/" + username);
        connection.sendPacket(joinPresence);
        return true;
    }
    // TODO
    public void showUsers(){
        Connection.addConnectionCreationListener(connectionCreationListener);
    }

    /*
     * Send a message to a user
     * @param to -> the user to send the message
     * @param message -> message to send
     */
    public void sendMessage(String message, String to){
        Chat chat = this.chatManager.createChat(to, messageListener);
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
    /*
     * Send a message to a group chat
     * @param room -> the name of the room to send the message
     * @param message -> message to send
     */
    public void sendMessageGroup(String message, String room){
        MultiUserChat multiUserChat = new MultiUserChat(connection, room);
        try {
            multiUserChat.sendMessage(message);
            multiUserChat.addMessageListener(groupPacketListener);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
    // send file to a user
    public boolean sendFile(String to, String filename){
        FileTransferNegotiator.IBB_ONLY = true;
        try {
            OutgoingFileTransfer stream = fileManager.createOutgoingFileTransfer(to);
            File file = new File(filename);
            if (!file.canRead()){
                return false;
            } 
            stream.sendFile(file, "file");
            return true;
        } catch (XMPPException e) {
            return false;
        }

    }
    // TODO: modificar esta funcion
    public void receiveFile(){
        FileTransferListener listener = new FileTransferListener() {
            @Override
            public void fileTransferRequest(FileTransferRequest fileTransferRequest) {
                System.out.printf(fileTransferRequest.getRequestor()+" Wants to send a file -> "+ fileTransferRequest.getFileName());
                // path to save the receipt the file
                File file = new File(System.getProperty("user.dir"), fileTransferRequest.getFileName());
                try {
                    System.out.println(fileTransferRequest.getFileName());
                    IncomingFileTransfer fileTransfer = fileTransferRequest.accept();
                    fileTransfer.recieveFile(file);
                    fileTransfer.cancel();
                    System.out.printf("File saved in -> " + file.getAbsolutePath());

                } catch (XMPPException e) {
                    System.out.println("It was an error while receiving the file");
                    fileTransferRequest.reject();
                }
            }
        };
        fileManager.addFileTransferListener(listener);
    }
    /*
     * Show the suscription requests that have not been accepted or rejected
     */
    public void notifications(){
        connection.addPacketListener(presenceListener, new PacketTypeFilter(Presence.class));
        //System.out.println("Unfiled entries: " + connection.getRoster().getEntryCount());
    }
    // TODO
    public void showNotifications(){
        // show presence
        Collection<RosterEntry> unfiledEntries = connection.getRoster().getUnfiledEntries();
        for (RosterEntry unfiledEntry: unfiledEntries){
            System.out.println("Unfiled entry: " + unfiledEntry.getUser());
        }
    }
    // TODO
    public void acceptRequest(String user){
        try {
            Jid jid = JidCreate.from(user);
            connection.getRoster().createEntry(user, user, new String[]{"Friends"});
        } catch (XMPPException |XmppStringprepException e) {
            System.out.println("Failed to accept request from " + user);
        }
    }

    /* 
    * Add a packet listener to listen for new messages
    */
    public void receiveMessage(){
        connection.addPacketListener(packetListener, new PacketTypeFilter(Message.class));
    }

    /*
     * Disconect from the server
     */
    public void disconect() throws XMPPException{
        connection.disconnect();
        System.out.println("Disconnected from " + this.server);
    }
}