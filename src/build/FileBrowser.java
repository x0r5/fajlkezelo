package build;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

public class FileBrowser{

    private JFrame frame;
    private DefaultMutableTreeNode root;

    private DefaultTreeModel treeModel;
    private FileSystemView fileSystemView;
    private Desktop desktop;

    private JTree tree;
    private JTable table;
    private TableModel tableModel;
    private JSplitPane splitPane;
    private JPanel lowerPanel;

    private File selectedFile;

    private JLabel fileName;
    private JTextField path;
    private JLabel date;
    private JLabel size;

    private JMenuBar menuBar;
    private JMenu menuFile;

    public void create() {
        frame = new JFrame("File Browser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setMinimumSize(new Dimension(800, 1000));
        fileSystemView = FileSystemView.getFileSystemView();
        desktop = Desktop.getDesktop();

        File rootFile = new File("/");
        root = new DefaultMutableTreeNode(new FileNode(rootFile));
        new Thread(new CreateChildNodes(root)).start();
        treeModel = new DefaultTreeModel(root);

        //tree
        tree = new JTree(treeModel);
        tree.setShowsRootHandles(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(new MyTreeSelectionListener());
        //tree.addTreeExpansionListener(new MyTreeExpansionListener());
        JScrollPane scrollPaneTree = new JScrollPane(tree);
        tree.setMinimumSize(new Dimension(600, 600));




        //table
        tableModel = new TableModel(rootFile.listFiles());
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setShowVerticalLines(false);
        table.getSelectionModel().addListSelectionListener(new MyListSelectionListener());
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(5);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);

        table.setMinimumSize(new Dimension(400, 600));
        JScrollPane scrollPaneTable = new JScrollPane(table);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPaneTree, scrollPaneTable);

        //menu
        menuBar = new JMenuBar();
        menuFile = new JMenu("Fájl");
        menuBar.add(menuFile);
        JMenuItem newFile = new JMenuItem("Új fájl...");
        menuFile.add(newFile);
        newFile.addActionListener(new NewFileActionListener());
        JMenuItem newDir = new JMenuItem("Új mappa");
        menuFile.add(newDir);
        newDir.addActionListener(new NewDirActionListener());
        frame.setJMenuBar(menuBar);
        JMenuItem delFile = new JMenuItem("Törlés...");
        menuFile.add(delFile);
        delFile.addActionListener(new DeleteFileActionListener());



        //lowerPanel for file info
        lowerPanel = new JPanel(new BorderLayout(4, 2));
        JPanel fileDetails = new JPanel(new GridLayout(0, 1, 2, 2));
        JPanel fileLabels = new JPanel(new GridLayout(0, 1, 2, 2));
        lowerPanel.add(fileLabels, BorderLayout.WEST);
        lowerPanel.add(fileDetails, BorderLayout.CENTER);

        fileName = new JLabel();
        fileDetails.add(fileName);
        fileLabels.add(new JLabel("Fájlnév", JLabel.TRAILING));

        path = new JTextField();
        path.setEditable(false);
        fileDetails.add(path);
        fileLabels.add(new JLabel("Hely", JLabel.TRAILING));

        size = new JLabel();
        fileDetails.add(size);
        fileLabels.add(new JLabel("Méret", JLabel.TRAILING));

        date = new JLabel();
        fileDetails.add(date);
        fileLabels.add(new JLabel("Módosítás dáuma", JLabel.TRAILING));


        JPanel buttonPanel = new JPanel(new GridLayout(0,1,2,2));
        lowerPanel.add(buttonPanel, BorderLayout.EAST);

        JButton open = new JButton("Megnyit");
        buttonPanel.add(open);
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    desktop.open(selectedFile);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        JButton show = new JButton("Mutat");
        buttonPanel.add(show);
        show.addActionListener(new OpenFileActionListener());
        JButton deleteButton = new JButton("Törlés");
        buttonPanel.add(deleteButton);
        deleteButton.addActionListener(new DeleteFileActionListener());




        frame.setLayout(new BorderLayout());
        frame.add(splitPane, BorderLayout.CENTER);
        frame.add(lowerPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);


    }

    class OpenFileActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                desktop.open(selectedFile.getParentFile());
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    class NewDirActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                String s = (String)JOptionPane.showInputDialog(
                        frame,
                        "Az új mappa neve:",
                        "Új mappa",
                        JOptionPane.PLAIN_MESSAGE);
                if (s.length() == 0){
                    s = "newFolder";
                }

                if(selectedFile.isDirectory()){
                    new File(selectedFile, s).mkdir();
                }
                else if(selectedFile.isFile()){
                    new File(selectedFile.getParentFile(), s).mkdir();
                }
            }catch(Exception ex){ex.printStackTrace();}
            tableModel.addNewList(selectedFile.listFiles());
            tableModel.fireTableDataChanged();
        }
    }

    class DeleteFileActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {


            int ans = JOptionPane.showConfirmDialog(
                    frame,
                    "Biztosan törölni szeretnéd a " + selectedFile.getName() + " fájlt?",
                    "Fájl törlése",
                    JOptionPane.YES_NO_OPTION);
            if (ans == 0){
                selectedFile.delete();
                tableModel.addNewList(selectedFile.getParentFile().listFiles());
                tableModel.fireTableDataChanged();
            }

        }
    }

    class NewFileActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            String s = (String)JOptionPane.showInputDialog(
                    frame,
                    "Az új fájl neve:",
                    "Új fájl",
                    JOptionPane.PLAIN_MESSAGE);
            if (s.length() == 0){
                s = "newFile";
            }

            try{
                File dir;
                if(selectedFile.isDirectory()){
                    File f = new File(selectedFile, s);
                    f.createNewFile();
                }
                else if(selectedFile.isFile()){
                    File f = new File(selectedFile.getParentFile(), s);
                    f.createNewFile();
                }
            }catch(IOException ex){ex.printStackTrace();}
            tableModel.addNewList(selectedFile.listFiles());
            tableModel.fireTableDataChanged();
        }
    }

    class MyTreeSelectionListener implements TreeSelectionListener{

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            new Thread(new CreateChildNodes(node)).start();
            tableModel.addNewList(((FileNode)node.getUserObject()).getFile().listFiles());
            tableModel.fireTableDataChanged();
            selectedFile = ((FileNode) node.getUserObject()).getFile();
        }
    }

    class MyListSelectionListener implements ListSelectionListener{
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int row = table.getSelectionModel().getLeadSelectionIndex();
            setFileInfo(tableModel.getFile(row));
        }
    }


    private void setFileInfo(File f){
        selectedFile = f;
        Icon icon = fileSystemView.getSystemIcon(f);
        fileName.setIcon(icon);
        fileName.setText(fileSystemView.getSystemDisplayName(f));
        path.setText(f.getPath());
        date.setText(new Date(f.lastModified()).toString());
        size.setText(f.length() + " bytes");
    }
}