import java.io.*;
public class Cat {
        public static void cat(String fileName){
            File file = new File(fileName);
            String line;
            try {
                BufferedReader in = new BufferedReader(new FileReader(file)) ;
                while( (line = in.readLine()) != null){
                    System.out.println(line);
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

