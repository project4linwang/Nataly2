package jaist.info.aspectj.nataly2.metamodel;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SElement {

	private String fullname;
	private String simplename;
	private String packagename;
	private String category;
	private String returnType;
	private String declaredName;
	private String type;
	private List<String> arglists=new LinkedList<String>();
	private List<String> subClass=new LinkedList<String>();
	private List<String> parentClass=new LinkedList<String>();
	
	public String getTraditionalFullName(){
		StringBuilder sb=new StringBuilder();
		String reg="\\(.*\\)$";
		Pattern pattern=Pattern.compile(reg);
		Matcher matcher=pattern.matcher(this.fullname);
		if(matcher.find()){
			String tmpname= matcher.replaceFirst("");
			sb.append(tmpname+"(");
			if(arglists!=null &&arglists.size()>0){
				  for(String arg : arglists){
					 //String para=analyzeParametrs(arg);
					 sb.append(arg+",");
				  }
				  sb.deleteCharAt(sb.length()-1);
				  sb.append(")");
		   }
			else{
				sb.append(")");
			}
		}
		else{
			sb.append(this.fullname);
		}
		return sb.toString();
	}
	
	public void setFullName(String fullname) {
		this.fullname = fullname;
	}
	public String getFullName() {
		return fullname;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategory() {
		return category;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setDeclaredName(String declaredName) {
		this.declaredName = declaredName;
	}
	public String getDeclaredName() {
		return declaredName;
	}
	public void setArglists(List<String> arglists) {
		this.arglists = arglists;
	}
	public List<String> getArglists() {
		return arglists;
	}
	public void setSubClass(List<String> subClass) {
		this.subClass = subClass;
	}
	public List<String> getSubClass() {
		return subClass;
	}
	public void setSimpleName(String simplename) {
		this.simplename = simplename;
	}
	public String getSimpleName() {
		return simplename;
	}
	public void setParentClass(List<String> parentClass) {
		this.parentClass = parentClass;
	}
	public List<String> getParentClass() {
		return parentClass;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	
}
