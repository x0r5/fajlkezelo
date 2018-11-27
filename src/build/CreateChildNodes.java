package build;



import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class CreateChildNodes implements Runnable{

    private DefaultMutableTreeNode rootNode;

    public CreateChildNodes( DefaultMutableTreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public void run() {
        createChildren(rootNode);
    }

    private void createChildren(DefaultMutableTreeNode node) {
        File file = ((FileNode)node.getUserObject()).getFile();
        File[] files = file.listFiles();
        for(File f: files){
            if(f.isDirectory() && !f.getName().startsWith(".")){
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FileNode(f));
                node.add(childNode);
                File[] childFiles = f.listFiles();
                for(File f2: childFiles){
                    if(f2.isDirectory() && !f2.getName().startsWith(".")){
                        childNode.add(new DefaultMutableTreeNode(new FileNode(f2)));
                    }
                }
            }
        }
    }

}