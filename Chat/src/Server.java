import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private Map<String, Socket> users;

    private Server() {
        try {
            this.serverSocket = new ServerSocket(4444);
            this.users = new HashMap<>();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    private String stripNullChars(String toFormat){
        StringBuilder builder = new StringBuilder();
        if (toFormat == null){
            return " ";
        }

        for (Character c:toFormat.toCharArray()) {
            if((int)c != 0){
                builder.append(c);
            }
        }
        toFormat = builder.toString();
        return toFormat;
    }

    private String getUsername(Socket socket){
        String username = "";
        boolean registered = false;

        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            do {
                username = this.stripNullChars(input.readLine());
                if(username != null) {
                    if (users.containsKey(username)) {
                        outputStream.writeChars("Taken\n");
                    } else {
                        outputStream.writeChars(username+"\n");
                        registered = true;
                    }
                }
            }while(!registered);

        }catch(Exception e){
            e.printStackTrace(System.out);
        }

        return username;
    }

    private void sendMessages(String username,String message) {
        for (Socket s : users.values()) {
            if (s.equals(users.get(username))) {
                continue;
            } else {
                try {
                    DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
                    dataOutputStream.writeChars(username+": "+message+"\n");
                }catch (Exception e){
                    e.printStackTrace(System.out);
                }
            }
        }
    }

    private void sendUserlist(String username) {
        try {
            DataOutputStream outputStream = new DataOutputStream(users.get(this.stripNullChars(username)).getOutputStream());
            if (users.size() > 1) {
                for (String user : users.keySet()) {
                    outputStream.writeChars(user+"\n");
                }
            } else {
                outputStream.writeChars("You are alone here :(\n");
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
    private void runServer(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(true) {
            try {
                Socket newConnection = serverSocket.accept();

                Runnable listen = () -> {
                    String username = getUsername(newConnection);
                    String message;
                    boolean leave = false;

                    if(!users.isEmpty()){
                        sendMessages(username," has joined the chat");
                    }
                    users.put(this.stripNullChars(username),newConnection);
                    try {
                        BufferedReader input = new BufferedReader(new InputStreamReader(users.get(username).getInputStream()));
                        do {
                            message = this.stripNullChars(input.readLine());
                            if (message.equals("@exit")) {
                                sendMessages(username, "hes left the chat");
                                leave = true;
                            }else if(message.equals("@users")){
                                sendUserlist(username);
                            } else {
                                sendMessages(username, message);
                            }
                        }while(!leave);

                        users.get(username).close();
                        users.remove(username);

                    }catch (Exception e){
                        e.printStackTrace(System.out);
                    }
                };

                Thread listenThread = new Thread(listen);
                executorService.execute(listenThread);

            }catch (Exception e){
                e.printStackTrace(System.out);
            }
        }
    }

    public static void main(String[] args){
        Server server = new Server();
        server.runServer();
    }
}
