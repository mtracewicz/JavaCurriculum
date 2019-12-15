import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    private Socket server;
    private boolean exit;
    private String username;

    private Client(Socket server) {
        this.server = server;
        this.exit = false;
    }

    private String stripNullChars(String toFormat){
        StringBuilder builder = new StringBuilder();
        for (Character c:toFormat.toCharArray()) {
            if((int)c != 0){
                builder.append(c);
            }
        }
        toFormat = builder.toString();
        return toFormat;
    }

    private String register(){
        String preferredUsername;

        do{
            System.out.print("Username: ");
            Scanner scanner = new Scanner(System.in);
            preferredUsername = scanner.next();

            try {
                DataOutputStream outputStream = new DataOutputStream(server.getOutputStream());
                BufferedReader input = new BufferedReader(new InputStreamReader(server.getInputStream()));

                outputStream.writeChars(preferredUsername+"\n");
                preferredUsername = input.readLine();

                preferredUsername = this.stripNullChars(preferredUsername);

                if(preferredUsername.equals("Taken")){
                    System.out.println("Username already in use.");
                    preferredUsername = null;
                }

            }catch (IOException ioe){
                ioe.printStackTrace(System.out);
            }

        }while(preferredUsername == null);

        return preferredUsername;
    }

    private void startClient(){
        this.username = this.register();
        System.out.println("Welcome to chat " + this.username + "!");
        System.out.println("To exit type \"@exit\" ");
        System.out.println("To see active users  type \"@users\" \n");
        Runnable write = () -> {
            try {
                String message;
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                DataOutputStream outputStream = new DataOutputStream(server.getOutputStream());
                while(!exit) {
                    message = input.readLine();

                    if (message.equals("@exit")) {
                        exit = true;
                    }

                    outputStream.writeChars(message+"\n");
                }
            }catch (IOException ioe){
                ioe.printStackTrace(System.out);
            }

        };

        Runnable read = () -> {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(server.getInputStream()));
                String received;

                while (!exit){
                    received = input.readLine();
                    if(received != null){
                        System.out.println(received);
                    }
                }
            }catch (IOException ioe){
                ioe.printStackTrace(System.out);
            }
        };

        Thread writeThread = new Thread(write);
        Thread readThread = new Thread(read);

        writeThread.start();
        readThread.start();
    }

    public static void main(String[] args){
        try {
            Socket socket = new Socket("localhost", 4444);
            Client client = new Client(socket);
            client.startClient();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
}