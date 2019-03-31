import java.io.File;

public class Cd {
    private String workingDir;
    private final String homeDir;

    public String getWorkingDir() {
        return workingDir;
    }
    public Cd(String wd) {
        this.workingDir = wd;
        this.homeDir =System.getProperty("user.home");
    }
    public void pwd(){
        System.out.println(workingDir);
    }
    public boolean setWorkingDirectory(String directory_name)
    {
        boolean result = false;
        File directory;
        if(directory_name.equals("")){
            workingDir = homeDir;
            return true;
        }
        else if(directory_name.equals("..")){
            workingDir = workingDirPrev();
            return true;
        }
        else if(!directory_name.startsWith("/")){
            directory_name = workingDir + "/"+directory_name;
        }
        if(directory_name.endsWith("/")){
            directory_name = directory_name.substring(0,directory_name.length());
        }
        directory = new File(directory_name);
        if (directory.isDirectory())
        {
            result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
            workingDir = directory_name;
        }
        else{
            System.out.println("Directory does not exist");
        }

        return result;
    }
    private String workingDirPrev(){
        if(workingDir.equals("/")){
            return workingDir;
        }
        else{
            int counter = 0;
            String returnValue="";
            for(Character i:workingDir.toCharArray()){
               if(i.equals('/')){
                   counter++;
               }
            }
            if(counter <= 2){
                returnValue = "/";
            }
            else{
                for(Character i:workingDir.toCharArray()) {
                    if(counter == 0){
                        break;
                    }
                    if (i.equals('/')) {
                        counter--;
                    }
                    returnValue+= i.toString();

                }
            }
            return returnValue;
        }
    }
}
