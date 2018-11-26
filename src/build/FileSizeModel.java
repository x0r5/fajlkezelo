package build;

import java.util.Comparator;

public class FileSizeModel implements Comparable<FileSizeModel>, Comparator<FileSizeModel> {
    Long bytes;
    long value;
    String unit;

    public FileSizeModel(long bytes){
        this.bytes = bytes; //összehasonlításhoz jó lesz
        if(bytes < 1024){
            value = bytes;
            unit = "bytes";
        }
        else if(bytes < 1024*1024){
            value = Math.round(bytes/1024);
            unit = "KB";
        }
        else if(bytes < Math.pow(1024, 3)){
            value = Math.round(bytes/(1024*1024));
            unit = "MB";
        }
        else if(bytes < Math.pow(1024, 4)){
            value = Math.round(bytes/Math.pow(1024, 3));
            unit = "GB";
        }
    }

    public Long getBytes(){
        return bytes;
    }

    public String toString(){
        return value + unit;
    }


    @Override
    public int compareTo(FileSizeModel o) {
        return bytes.compareTo(o.getBytes());
    }

    @Override
    public int compare(FileSizeModel o1, FileSizeModel o2) {
        return o1.compareTo(o2);
    }
}
