package build;

import javax.swing.*;

public class Gui extends JFrame {

    FilesData data;

    public Gui(){
        super("Fájlkezelő");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JTree tree = new JTree(data);
    }

    public void initComponents(){

    }



    public static void main(String[] args){
        Gui gui = new Gui();
        gui.setVisible(true);

    }

}
