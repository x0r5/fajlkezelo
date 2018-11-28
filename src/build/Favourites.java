package build;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Favourites{

    List<File> list = new ArrayList<>();

    public Favourites(){
        try{
            FileInputStream fis = new FileInputStream("kedvencek.txt");
            ObjectInputStream is = new ObjectInputStream(fis);
            while(true){
                Object in = new Object();
                try {
                    in = is.readObject();
                }catch(ClassNotFoundException e){e.printStackTrace();}
                if(in == null){
                    break;
                }
                list.add((File)in);

            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }


    public boolean saveFavs(){
        try{
            FileOutputStream fos = new FileOutputStream("kedvencek.txt");
            ObjectOutputStream os = new ObjectOutputStream(fos);
            for(File obj : list){
                os.writeObject(obj);
            }
            os.close();


            return true;


        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public List<File> getList() {
        return list;
    }

    public void addFav(File f){
        list.add(f);
    }
}
