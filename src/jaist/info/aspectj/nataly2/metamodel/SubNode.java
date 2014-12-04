package jaist.info.aspectj.nataly2.metamodel;

public class SubNode<T> extends Node<T> {


	private Node<T> parent;        

	private RootNode<T> root;
	
	public RootNode<T> getRoot(){
		return this.root;
	}
	public SubNode(T nodeData, RootNode<T> root){
		super();
		this.element=nodeData;
		this.root=root;
	}


    public void setParent(Node<T> parent){
    	this.parent=parent;
    	this.m_depth=parent.getM_depth()+1;
    	if(root.getDepth()< this.m_depth){
    		root.setDepth(this.m_depth);
    	}
    	
    }
    public Node<T> getParent(){
    	return this.parent;
    }



}
