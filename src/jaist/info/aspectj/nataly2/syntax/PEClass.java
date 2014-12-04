package jaist.info.aspectj.nataly2.syntax;

import jaist.info.aspectj.nataly2.metamodel.ISignaturePattern;
import jaist.info.aspectj.nataly2.metamodel.RelationElements;
import jaist.info.aspectj.nataly2.metamodel.SElement;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ICategories;

/**
 * Class of class signature in signature pattern
 * @author suse-wl
 *
 */
public class PEClass implements ISignaturePattern {

	RelationElements relation_elems=RelationElements.getRelationElements();
	List<SElement> selems=relation_elems.getSelementlist();
	private String data=null;
	@Override
	public boolean matches(SElement se) {
		// TODO Auto-generated method stub
		if(data==null || data.equals("")){
			return true;
		}
		else if(data.equals("*") || data.equals("Object+") ){
			
			return true;
		}
		else{
			String className="";
			if(se.getCategory().equals(ICategories.CLASS)){
				className=removeSchar(se.getSimpleName());
			}
			else{
				className=removeSchar(se.getDeclaredName());
			}
			String regstr="\\+";
			Pattern pattern=Pattern.compile(regstr);
			Matcher matcher=pattern.matcher(data);
			if(matcher.find()){
				//need to check subclass
				String parentClass=matcher.replaceFirst("");
				if(parentClass.equals(className)){
					return true;
				}
				else{
					for(String parent:se.getParentClass()){
						if(parent.contains(".")){
							String reg="\\.(\\w*$)";
							Pattern pattern2=Pattern.compile(reg);
							Matcher matcher2=pattern2.matcher(parent);
							if(matcher2.find()){
								String parentSimpleName=matcher2.group(1);
								List<String> parents=new LinkedList<String>();
								parents.add(parentSimpleName);
								findParents(parents,parentSimpleName);
								for(String p:parents){
									if(p.equals(parentClass)){
										return true;
									}
								}
								
							}
						}
						else{
							List<String> parents=new LinkedList<String>();
							parents.add(parent);
							findParents(parents,parent);
							for(String p:parents){
								if(p.equals(parentClass)){
									return true;
								}
							}
							
						}
						
					}
				}
				
			}
			else{
				
				if(data.equals(className)){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return false;
		
	}
	public void setData(String data) {
		this.data = data.replaceAll("\\s", "");
		StringBuilder sb=new StringBuilder();
		sb.append(this.data);
		String regstr="\\+";
		Pattern pattern=Pattern.compile(regstr);
		Matcher matcher=pattern.matcher(data);
		if(!matcher.find()){
		  sb.append("+");
		}
		this.data=removeSchar(sb.toString());
	}
	public String getData() {
		return data;
	}
	private void findParents(List<String> parents, String child){
		
		SElement child_elem=findElement(child);
		if(child_elem!=null){
			if(!child_elem.getParentClass().isEmpty()){
				parents.addAll(child_elem.getParentClass());
				for(String c:child_elem.getParentClass()){
					findParents(parents,c);
				}
			}
		}
		
	
	}
	private SElement findElement(String name){
		if(selems!=null){
			for(SElement se:selems){
				if(se.getCategory().equals(ICategories.CLASS.name())){
					if(se.getSimpleName().equals(name)|| se.getFullName().equals(name)){
						return se;
					}
				}
			}
		}
		return null;
		
	}
	/*
	 * remove $ char and the content, which follows this char
	 */
	private String removeSchar(String input){
		String reg="\\$[^.]";
		return input.replaceFirst(reg, "");
	}
}
