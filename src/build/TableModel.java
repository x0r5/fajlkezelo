package build;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TableModel extends AbstractTableModel {

    List<File> filesList = new ArrayList<>();
    FileSystemView fileSystemView;



    /*
    *        0   |   1   |   2   |   3
    *      icon | name | size | last modified
    * */

    //konstruktor
    public TableModel(File[] files){
        filesList = Arrays.asList(files);
        fileSystemView = FileSystemView.getFileSystemView();
    }

    public void addNewList(File[] files){
        filesList = Arrays.asList(files);
    }

    @Override
    public int getRowCount() {
        return filesList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case(0):
                return fileSystemView.getSystemIcon(filesList.get(rowIndex));
            case(1):
                return filesList.get(rowIndex).getName();
            case(2):
                return new FileSizeModel(filesList.get(rowIndex).length());
            case(3):
                return filesList.get(rowIndex).lastModified();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex){
            case 0:
                return Icon.class;
            case 1:
                return String.class;
            case 2:
                return FileSizeModel.class;
            default:
                return Date.class;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case(0):
                return "Ikon";
            case(1):
                return "Fájlnév";
            case(2):
                return "Méret";
            default:
                return "Utolsó módosítás";
        }
    }

    public File getFile(int row){
        return filesList.get(row);
    }
}
