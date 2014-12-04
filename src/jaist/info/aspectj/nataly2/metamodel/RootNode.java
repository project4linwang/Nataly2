package jaist.info.aspectj.nataly2.metamodel;

public class RootNode<T> extends Node<T>{

	private int depth;
	
	public RootNode(T nodeData) {
		super();
		// TODO Auto-generated constructor stub
		this.element=nodeData;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
	}

	
	
}
