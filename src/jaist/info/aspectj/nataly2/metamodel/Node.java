package jaist.info.aspectj.nataly2.metamodel;

import java.util.LinkedList;
import java.util.List;
/**
 * The Vertex in the relationship graph.
 * @author suse-wl
 *
 * @param <T>
 */
public abstract class Node<T> {

	protected List<Node<T>> children;  //the list of children nodes      
	protected T element;
	protected int m_depth;
	protected String relation; //the relation between this node and its parent
    protected boolean iskeypoint;
    
    public void setIsKeyPoint(boolean kp){
    	this.iskeypoint=kp;
    }
    public boolean getIsKeyPoint()
    {
    	return this.iskeypoint;
    }
	public T getData(){
		return element;
	}

    public void setChildren(List<Node<T>> m_children){
    	this.children=m_children;
    }
    public List<Node<T>> getChildrend(){
    	return this.children;
    }


	public void setM_depth(int m_depth) {
		this.m_depth = m_depth;
	}

	public int getM_depth() {
		return m_depth;
	}
	
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getRelation() {
		return relation;
	}
	
}
