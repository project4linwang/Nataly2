package jaist.info.aspectj.nataly2.seed;



import jaist.info.aspectj.nataly2.metamodel.SElement;

import java.util.List;

public abstract class Signature {

	private String category;
	
	protected String sig;
	/**
	 * Catch a set of relevant elements
	 * @param selems
	 * @return is a set of seed's name.
	 */
	public abstract List<String> CatchRelativeElements(List<SElement> selems);
	public void setSig(String sig) {
		this.sig = sig;
	}
	public String getSig() {
		return sig;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategory() {
		return category;
	} 
}
