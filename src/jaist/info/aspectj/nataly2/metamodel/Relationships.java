package jaist.info.aspectj.nataly2.metamodel;

import java.util.LinkedList;
import java.util.List;

public class Relationships {

	private String relationType;
	private List<Element> elements=new LinkedList<Element>();
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	public String getRelationType() {
		return relationType;
	}
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
	public List<Element> getElements() {
		return elements;
	}
	
}
