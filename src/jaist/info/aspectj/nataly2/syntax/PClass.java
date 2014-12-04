package jaist.info.aspectj.nataly2.syntax;

import jaist.info.aspectj.nataly2.metamodel.ISignaturePattern;
import jaist.info.aspectj.nataly2.metamodel.SElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ICategories;

/**
 * Class of class in the signature pattern.
 * @author suse-wl
 *
 */
public class PClass implements ISignaturePattern {

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
								if(parentSimpleName.equals(parentClass)){
									return true;
								}
							}
						}
						else{
							if(parent.equals(parentClass)){
								return true;
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
	}
	public String getData() {
		return data;
	}
	private String removeSchar(String input){
		String reg="\\$[^.]";
		return input.replaceFirst(reg, "");
	}


}
