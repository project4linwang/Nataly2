package jaist.info.aspectj.nataly2.metamodel;


import java.util.LinkedList;
import java.util.List;

public class RelationElements {

	private static RelationElements relements;
	private List<SElement> selementlist=new LinkedList<SElement>();
	private List<Element> elementlist=new LinkedList<Element>();
	private List<RelationshipGraph> treeList=new LinkedList<RelationshipGraph>();
	private String filePath;
	private RelationElements(){
		//
	}
	public static RelationElements getRelationElements(){
		if(relements==null){
			relements=new RelationElements();
		}
		return relements;
	}
	
	public void setElementList(List<Element> elist){
		this.elementlist=elist;
	}
	public List<Element> getElementList(){ 
		return this.elementlist;
	}
	public void setSelementlist(List<SElement> selementlist) {
		this.selementlist = selementlist;
	}
	public List<SElement> getSelementlist() {
		return selementlist;
	}
	public void setTreeList(List<RelationshipGraph> treeList) {
		this.treeList = treeList;
	}
	public List<RelationshipGraph> getTreeList() {
		return treeList;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFilePath() {
		return filePath;
	}
	
}
