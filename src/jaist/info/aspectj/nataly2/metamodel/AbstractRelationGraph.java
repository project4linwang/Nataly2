package jaist.info.aspectj.nataly2.metamodel;

/**
 * Abstract class for relationship graph.
 * @author suse-wl
 *
 */
public class AbstractRelationGraph {
	private PatternNode<String> wildcard_root;
	
	
	public AbstractRelationGraph(){
		super();
	
	}
	public void setWildCardRoot(PatternNode<String> root){
		this.wildcard_root=root;
	}
	public PatternNode<String> getWildCare_Root(){
		return this.wildcard_root;
	}

}
