package jaist.info.aspectj.nataly2.metamodel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The element class represents the elements in the program.
 * @author suse-wl
 *
 */
public class Element {
	private String category;
	private String name;
	private String fullName;
	private String returnType;
	private List<String> arglists=new LinkedList<String>();
	private Map<String,Relationships> relationMaps=new HashMap<String, Relationships>();
	/**
	 * 
	 * @return The full name of the element. Including package, receiver class , method name and arguments..
	 */
	public String getTraditionalFullName(){
		StringBuilder sb=new StringBuilder();
		String reg="\\(.*\\)$";
		Pattern pattern=Pattern.compile(reg);
		Matcher matcher=pattern.matcher(this.fullName);
		if(matcher.find()){
			String tmpname= matcher.replaceFirst("");
			sb.append(tmpname+"(");
			if(arglists!=null){
				
				  for(String arg : arglists){
					 //String para=analyzeParametrs(arg);
					  if(arg!=null){
						  sb.append(arg+",");
					  }
					  else{
						  sb.append(",");
					  }
					 
				  }
				  sb.deleteCharAt(sb.length()-1);
				  sb.append(")");
			    }
			else{
				sb.append(")");
			}
		}
		else{
			sb.append(this.fullName);
		}
		return sb.toString();
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategory() {
		return category;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setFullName(String fullname) {
		this.fullName = fullname;
	}
	public String getFullName() {
		return fullName;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setArglists(List<String> arglists) {
		this.arglists = arglists;
	}
	public List<String> getArglists() {
		return arglists;
	}
	public void setRelationMaps(Map<String,Relationships> relationMaps) {
		this.relationMaps = relationMaps;
	}
	public Map<String,Relationships> getRelationMaps() {
		return relationMaps;
	}
	

}
