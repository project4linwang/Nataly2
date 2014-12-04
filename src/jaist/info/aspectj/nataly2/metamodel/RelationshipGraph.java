package jaist.info.aspectj.nataly2.metamodel;

import java.util.LinkedList;
import java.util.Queue;


/*
 * Relationship Tree
 */
public class RelationshipGraph  {

    private RootNode<Element> root;
    public RelationshipGraph(){
    	super();
    }
    public RootNode<Element> getRoot(){
    	return this.root;
    }
    public void setRoot(RootNode<Element> m_root){
    	this.root=m_root;
    	this.root.setM_depth(0);
    	
    }
    /*
     * Breadth first search for the tree,
     * Return the list, which is sorted by the bfs algorithm.
     */
	public Queue<Node<Element>> breadthSearch(){
		Queue<Node<Element>> q = new LinkedList<Node<Element>>();
		Queue<Node<Element>> queue=new LinkedList<Node<Element>>();
	 	Node<Element> item;
	 	if( root != null)
	 	{
	 		//System.out.println("Print tree by breadth first search!");
	 		q.add(root);
	 		while(!q.isEmpty() )
	 		{
	 			item = (Node<Element>)q.remove();	 			
	 			//System.out.println(item.getData().getFullName());
	 			queue.add(item);
	 			if(item != null)
	 			{
	 				if(item.getChildrend()!=null){
	 					for(Node<Element> child:item.getChildrend()){
		 					if(child!=null){
		 						q.add(child);
		 					}
		 				}
	 				}
	 				
	 			}
	 		}
	 	}
		return queue;
	}
	/*
	 * Get the depth of the tree
	 */
	public int depth(){
		return getRoot().getDepth();
		
	}
	
}
