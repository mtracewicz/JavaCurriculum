import java.io.*;
import java.util.Vector;

public class History {
    private Vector<String> commandsHistory;

    public History(){
        commandsHistory = new Vector<>();
        loadHistory();
    }
    public void addToHistory(String cmd){
        this.commandsHistory.add(cmd);
    }
    public void printHistory(){
        for(String cmd:commandsHistory){
            System.out.println(cmd);
        }
    }
    public void saveHistory(){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("history.txt"));
            for (String line : commandsHistory) {
                out.write(line);
                out.newLine();
            }
            out.close();
        }
        catch (FileNotFoundException fnf) {
            System.out.println(fnf.getMessage());
        }
        catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
    private void loadHistory(){
        String line;
        try {
            BufferedReader in = new BufferedReader(new FileReader("history.txt")) ;
            while( (line = in.readLine()) != null){
                commandsHistory.add(line);
            }
            in.close();
        }
        catch (FileNotFoundException fnf) {
            System.out.println(fnf.getMessage());
        }
        catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
}
