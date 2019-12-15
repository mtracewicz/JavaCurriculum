import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.Vector;

public class Ls {

    private static int allFlag = 0;
    private static int listFlag = 0;
    private static int recursiveFlag = 0;
    private static int humanFlag = 0;
    private static char unit;

    public static void ls(String args,String currentDir){
        Vector<String> argsVector = getArguments(args,currentDir);
        for(String j: argsVector) {
           File file = new File(j);
           FilenameFilter filter = (dir, name) -> !name.startsWith(".");
           File[] files = (allFlag == 0) ? file.listFiles(filter):file.listFiles();
           Vector<File> directories = new Vector<>();
           if(files != null){
                Arrays.sort(files);
            }
            try {
                for (File i : files) {
                    if(listFlag == 0) {
                        System.out.println(i.getName());
                    }
                    else {
                        PosixFileAttributes attributes = Files.readAttributes(i.toPath(),PosixFileAttributes.class);
                        char d = (i.isDirectory())?'d':'-';
                        long size =(humanFlag == 0)?i.length():humanSize(i.length());
                        System.out.println(d+rwx(attributes)+" "+attributes.owner()+" "+attributes.group()+" "+size+unit+" "+ new Date(i.lastModified()) + " "+ i.getName());
                    }
                    if (i.isDirectory() && recursiveFlag == 1) {
                        directories.add(i);
                    }
                }
                for(File i:directories){
                    System.out.println();
                    System.out.println(i.getAbsolutePath());
                    ls(i.toString(),currentDir);
                }
            } catch (NullPointerException npe) {
                System.out.println("NullPointerException happened");
            }catch (IOException ioe){
                System.out.println("IOException happened");
            }
        }
    }
    public static void zeroFlags(){
        allFlag = 0;
        listFlag = 0;
        recursiveFlag = 0;
        humanFlag = 0;
    }
    private static Vector<String> getArguments(String line,String currentDir){
        Vector<String> returnedValue = new Vector<>();
        String tmp="";
        int count =0;
        for(Character i:line.toCharArray()){
            if(i.equals(' ')){
                returnedValue.add(tmp);
                tmp ="";
            }
            else {
                tmp+=i.toString();
            }
        }
        returnedValue.add(tmp);
        boolean[] helper = new boolean[returnedValue.size()];

        for(int j = 0;j <returnedValue.size();j++){
            if(returnedValue.elementAt(j).startsWith("-")){
                helper[j] = true;
                for (Character i: returnedValue.elementAt(j).toCharArray()) {
                    if(i.equals('a')){
                        allFlag = 1;
                    }
                    else if(i.equals('l')){
                        listFlag = 1;
                    }
                    else if(i.equals('h')){
                        humanFlag = 1;
                    }
                    else if(i.equals('R')){
                        recursiveFlag = 1;
                    }
                }
            }
            else{
                helper[j] = false;
            }
        }
        for(int i = 0;i < helper.length;i++){
            if(helper[i]){
                returnedValue.remove(i-count);
                count++;
            }
        }
        for(String i:returnedValue){
            if(!i.startsWith("/")){
                i = currentDir+"/"+i;
            }
        }
        if(returnedValue.isEmpty()){
            returnedValue.add(currentDir);
        }
        return returnedValue;
    }
    private static String rwx(PosixFileAttributes atr){
        char[] ret = new char[9];
        for(int i = 0; i < 9;i++){
            ret[i] = '-';
        }
        Set<PosixFilePermission> permissions = atr.permissions();
        for(PosixFilePermission i: permissions){
            switch (i){
                case OWNER_READ:
                    ret[0] = 'r';
                    break;
                case OWNER_WRITE:
                    ret[1] = 'w';
                    break;
                case OWNER_EXECUTE:
                    ret[2] = 'x';
                    break;
                case GROUP_READ:
                    ret[3] = 'r';
                    break;
                case GROUP_WRITE:
                    ret[4] = 'w';
                    break;
                case GROUP_EXECUTE:
                    ret[5] = 'x';
                    break;
                case OTHERS_READ:
                    ret[6] = 'r';
                    break;
                case OTHERS_WRITE:
                    ret[7] = 'w';
                    break;
                case OTHERS_EXECUTE:
                    ret[8] = 'x';
                    break;
            }
        }
        return String.valueOf(ret);
    }
    private static long humanSize(long length){
        int counter = 0;
        while (length>1024){
            length/=1024;
            counter++;
        }
        switch(counter)
        {
            case 1:
                unit = 'K';
                break;
            case 2:
                unit = 'M';
                break;
            case 3:
                unit = 'G';
                break;
            case 4:
                unit = 'T';
                break;
            default:
                unit = ' ';
                break;
        }
        return length;
    }
}
