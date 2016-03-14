package pt.tecnico.myDrive.domain;

public class Dir extends Dir_Base {
    
    public Dir() {
        super();
    }

    public File getFileByName(String name){
        for(File file : getFileSet())
            if(file.getName().equals(name))
                return file;
        return null;
    }
    
}
