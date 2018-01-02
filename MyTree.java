import java.util.ArrayList;
public class MyTree{
	private Pipe root; 
	private ArrayList<Pipe> leafNodes = new ArrayList<Pipe>();
	
	public MyTree(Pipe rootPipe){
		rootPipe.setInTree(true);
		root = rootPipe;
	}
	
	public Pipe getRoot(){
		return this.root;
	}
	
	
	public void addLeafNode(Pipe leafNode){
		this.leafNodes.add(leafNode);
	}
	
	public ArrayList<Pipe> getLeafNodes(){
		return this.leafNodes;
	}
}