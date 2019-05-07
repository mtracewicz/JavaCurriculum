import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
        private boolean close;
        private String username;

        private void getUserName(){
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Username:");
                username = in.readLine();
            }catch(Exception e){
                e.printStackTrace(System.out);
            }
        }

        private void doTheWork() {
            try {
                Socket socket = new Socket("localhost", 4444);
                BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                boolean go;

                do{
                    getUserName();
                    out.writeBytes(username);
                    String checker = socketIn.readLine();
                    System.out.println(checker);
                    go = !checker.equals("Username already in use!");
                }while(!go);


                Runnable send = () -> {
                    try {
                        String line = "";
                        while (!line.equals("\\exit")) {
                            System.out.println(username+": ");
                            line = consoleIn.readLine();
                            line += '\n';
                            out.writeBytes(line);
                            out.flush();
                        }
                        this.close = true;

                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                };

                Runnable receive = () -> {
                    try {
                        String line;
                        while (true) {
                            if(!this.close){
                                line = socketIn.readLine();
                                if (line != null) {
                                    System.out.println(line);
                                }
                            }else{
                                break;
                            }
                        }
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }

                };

                Thread sendThread = new Thread(send);
                Thread receiveThread = new Thread(receive);
                sendThread.start();
                receiveThread.start();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        public static void main(String[] args){
            Client program = new Client();
            program.close = false;
            program.doTheWork();
        }
}
