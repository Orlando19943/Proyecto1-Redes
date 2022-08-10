package chat.client;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

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
        try {
            connection.login(this.username, this.password);
            System.out.println("Logged in as " + "orlandotest");
        } catch (XMPPException e) {
            e.printStackTrace();
            System.out.println("Failed to log in as " + "orlandotest");
        }
    }

    public void dissconect() throws XMPPException{
        Connection connection = new XMPPConnection(this.server);
        connection.disconnect();
        System.out.println("Disconnected from " + this.server);
    }
}
