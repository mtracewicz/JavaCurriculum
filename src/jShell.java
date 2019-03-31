import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

public class jShell {
    private boolean exit;
    private History history;
    private Cd currentDir;
    private jShell() {
        history = new History();
        exit = false;
        currentDir = new Cd(System.getProperty("user.dir"));
    }
    private String getArgs(String line){
        String args = "";
        int current, record = 0;
        StringReader in = new StringReader(line);
        try {
            while ( (current = in.read()) != -1) {
                if(record > 0){
                    args+=String.valueOf(Character.toChars(current));
                }
                if(String.valueOf(Character.toChars(current)).equals(" ")) {
                    record++;
                }

            }
            in.close();
        }
        catch(IOException ioe){
            ioe.printStackTrace(System.out);
        }
        return args;
    }
    private String getCommand(String line){
        String cmd="";
        int current;
        StringReader in = new StringReader(line);
        try {
            while ( (current = in.read()) != -1) {
                if(String.valueOf(Character.toChars(current)).equals(" ")) {
                    break;
                }
                cmd += String.valueOf(Character.toChars(current));
            }
            in.close();
        }
        catch(IOException ioe){
            ioe.printStackTrace(System.out);
        }
        return cmd;
    }
    private void session(){
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (!exit){
            try{
                System.out.print(System.getProperty("user.name")+": ");
                String line = in.readLine();
                String command = getCommand(line);
                String args = getArgs(line);
                history.addToHistory(command+" "+args);
                switch (command){
                    case "exit":
                        exit = true;
                        break;
                    case "history":
                        history.printHistory();
                        break;
                    case "pwd":
                        currentDir.pwd();
                        break;
                    case "cd":
                        currentDir.setWorkingDirectory(args);
                        break;
                    case "cat":
                        if(!args.startsWith("/")){
                            args = currentDir.getWorkingDir()+"/"+args;
                        }
                        Cat.cat(args);
                        break;
                    case "echo":
                        Echo.echo(args);
                        break;
                    case "ls":
                        if(args.equals("")){
                            args +=" " + currentDir.getWorkingDir();
                        }
                        Ls.ls(args,currentDir.getWorkingDir());
                        Ls.zeroFlags();
                        break;
                    default:
                        System.out.println(command +" command not found.");
                }
            }
            catch (IOException ioe){
                ioe.printStackTrace(System.out);
            }
        }
        history.saveHistory();
    }
    public static void main(String[] args){
        jShell  jshell = new jShell();
        jshell.session();
    }
}
