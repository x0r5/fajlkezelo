package build;

import javax.swing.*;
import java.io.File;

public class FileNode {


    private File file;
    private Icon icon;

    public FileNode(File file){
        this.file = file;
    }

    public File getFile(){
        return file;
    }

    public void setIcon(Icon icon){
        this.icon = icon;
    }


    @Override
    public String toString() {
        return file.getName();
    }
}