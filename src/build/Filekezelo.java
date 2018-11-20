package build;

import java.io.File;

public class Filekezelo {

    public class fajlkezelo {
        //kiindulas a root konyvtar
        //idokozben valtozik
        File konyvtar = new File("/");


        //listazas File mutatokba
        //for debugging and extras
        File[] listazFajlokba(){
            File[] fajlok = konyvtar.listFiles();
            return fajlok;
        }

        //listaz, csak megadott helyről
        //visszadja: fajlnev, meret, konyvtar (T/F)
        String[][] listaz2(String hely){
            File root = new File(hely);
            File exKonyvtar = konyvtar;
            konyvtar = root;
            String[][] out = listaz();
            konyvtar = exKonyvtar;  //visszaallitjuk az eredeti konyvtarat
            return out;
        }

        //visszadja: fajlnev, meret, konyvtar (+/-)
        String[][] listaz(){

            String[] fajlok = konyvtar.list();
            // fajlev, meret, dir
            String[][] output = new String[fajlok.length][3];
            for(int i = 0; i< fajlok.length; i++){
                if(new File(konyvtar,fajlok[i]).isDirectory()){
                    output[i][2] = "";
                    output[i][1] = "?"; //ennek nem tudjuk a meretet
                }
                else{
                    output[i][2] = "-";
                    output[i][1] = String.valueOf((new File(konyvtar, fajlok[i]).length()));
                }
                output[i][0] = fajlok[i];
            }
            return output;
        }

        void changeDir(String dir){
            if(new File(konyvtar, dir).isDirectory()){
                konyvtar = new File(konyvtar, dir);
            }
            //ha nem konyvtar azzal nem foglalkozunk... -> esetleg fájl megjelenítése? ->TODO
        }

        //recurse into directories
        //@TODO
        //mivel terjen vissza?
        String[][] reclist(){
            return null;

        }



    }
}
