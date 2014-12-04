package jaist.info.aspectj.nataly2.metamodel;


import java.util.List;

public class PatternNode<T> {

	private List<PatternNode<T>> children;  //the list of children nodes      
	private T element;
    private PatternRelation relation; //the relation between this node and its parent
    private String name;
    
    
	public T getData(){
		return element;
	}
	public void setData(T elem){
		this.element=elem;
	}

    public void setChildren(List<PatternNode<T>> m_children){
    	this.children=m_children;
    }
    public List<PatternNode<T>> getChildrend(){
    	return this.children;
    }
	
	public void setRelation(PatternRelation relation) {
		this.relation = relation;
	}
	public PatternRelation getRelation() {
		return relation;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
}
