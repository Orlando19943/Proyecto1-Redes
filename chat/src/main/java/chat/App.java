package chat;
import org.jivesoftware.smack.XMPPException;

import chat.client.Client;

public class App 
{
    static Client client;
    /*
     * Options to be used at the beggining of the program
     */
    public static String init(){
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        String option = System.console().readLine();
        return option;
    }
    /*
     * Options to be used to get the username, password and server name
     */
    public static String[] userInformation(){
        String host = "";
        String username = "";
        char[] password = {};
        while (host.length() == 0) {
            System.out.print("\nServer: ");
            host = System.console().readLine();
        }
        while (username.length() == 0) {
            System.out.print("\nUsername: ");
            username = System.console().readLine();
        }
        while (password.length == 0) {
            System.out.print("\nPassword: ");
            password = System.console().readPassword();
        }
        return new String []{host, username, String.valueOf(password)};
    }

    /*
     * Options to be used after the user has logged in
     */
    public static void online(){
        String message,to,group, username, status, type, delete, option, user, room = "";
        Object[] friends;
        client.notifications();
        do {
            System.out.println("\n1. Send a message");
            System.out.println("2. Send a group message");
            System.out.println("3. Find user");
            System.out.println("4. Show friends");
            System.out.println("5. Add friend");
            System.out.println("6. Join a group chat");
            System.out.println("7. Change status");
            System.out.println("8. Accept friend request");
            System.out.println("9. Send a file");
            System.out.println("10. Delete account");
            System.out.println("11. Logout");
            System.out.print("-> ");
            option = System.console().readLine();
            try {
                switch (option) {
                    case "1":
                        System.out.print("\nTo: ");
                        to = System.console().readLine();
                        System.out.print("\nMessage: ");
                        message = System.console().readLine();
                        client.sendMessage(message, to);
                        break;
                    case "2":
                        System.out.print("\nGroup: ");
                        room = System.console().readLine();
                        System.out.print("\nMessage: ");
                        message = System.console().readLine();
                        client.sendMessageGroup(message, room);
                        break;
                    case "3":
                        System.out.print("\nUsername: ");
                        to = System.console().readLine();
                        user = client.findUser(to);
                        System.out.println(user);
                        break;
                    case "4":
                        friends = client.getFriends();
                        System.out.println("\nFriends:");
                        for(int i = 0; i < friends.length; i++){
                            System.out.println(friends[i]);
                        }
                        break;
                    case "5":
                        System.out.print("\nEnter the name of the user: ");
                        to = System.console().readLine();
                        client.addFriend(to);
                        break;
                    case "6":
                        System.out.print("\nEnter the name of the group chat: ");
                        group = System.console().readLine();
                        System.out.print("\nEnter a username: ");
                        username = System.console().readLine();
                        client.joinRoom(group, username);
                        break;
                    case "7":
                        System.out.print("\n1. Unavailable\n2. Available\n-> ");
                        type = System.console().readLine();
                        System.out.print("\nEnter a status message: ");
                        status = System.console().readLine();
                        try{
                            client.changeStatus(Integer.parseInt(type), status);
                        } catch (NumberFormatException e){
                            System.out.println("Invalid option");
                        }
                        break;
                    case "8":
                        System.out.print("\nEnter the name of the user: ");
                        to = System.console().readLine();
                        System.out.print("\n1.Accept\n2.Reject\n-> ");
                        type = System.console().readLine();
                        client.acceptRequest(to, Integer.parseInt(type));
                        break;
                    case "9":
                        break;
                        
                    case "10":
                        System.out.print("\nAre you sure about delete your account?\n1. Yes\n2.No: \n-> ");
                        delete = System.console().readLine();
                        if (delete.equals("1")){
                            System.out.print("\nDeleting your account...");
                            client.deleteUser();
                            option = "11";
                        }
                        break;
                    case "11":
                        client.disconect();
                        break;
                    default:
                        System.out.println("\nInvalid option");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid option");
            }
        }while (!option.equals("11"));
    }
    public static void main(String[] args) throws XMPPException {
        System.out.println("Welcome to chat");
        String username,host,password,option, optionOnline = "";
        String[] userInfo;
        client = new Client("","","", false);
        do{
            option = init();
            try {
                switch (Integer.parseInt(option)) {
                    case 1:
                        userInfo = userInformation();
                        client.setServer(userInfo[0]);
                        client.setUsername(userInfo[1]);
                        client.setPassword(userInfo[2]);
                        client.connect();
                        client.login();
                        online();
                        System.out.println("\nLogged in");
                        break;
                    case 2:
                        userInfo = userInformation();
                        client.setServer(userInfo[0]);
                        client.setUsername(userInfo[1]);
                        client.setPassword(userInfo[2]);
                        client.connect();
                        client.createUser();
                        client.login();
                        online();
                        break;
                    case 3:
                        System.out.println("Bye");
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid option");
            }
        }while(!option.equals("3"));
    }
}
