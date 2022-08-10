package chat;

import org.jivesoftware.smack.XMPPException;

import chat.client.Client;

public class Test {
    public static void main(String[] args) throws XMPPException {
        Client client;
        client = new Client("orlandotest","123456789","alumchat.fun");
        client.connect();
        client.login();
        client.sendMessage("message", "orlandocabrera");
        client.dissconect();
    }
}
