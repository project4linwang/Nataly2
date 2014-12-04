package jaist.info.aspectj.nataly2.syntax;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import jaist.info.aspectj.nataly2.metamodel.ISignaturePattern;
import jaist.info.aspectj.nataly2.metamodel.SElement;

/**
 * class of method signature in signature pattern.
 * @author suse-wl
 *
 */
public class PMethod implements ISignaturePattern {

	private String data=null;
	@Override
	public boolean matches(SElement se) {
		// TODO Auto-generated method stub
		if(data==null || data.equals("")){
			return true;
		}
		else if(data.equals("*")){
			return true;
		}
		else{
			String regstr="\\*";
			Pattern pattern=Pattern.compile(regstr);
			Matcher matcher=pattern.matcher(data);
			if(matcher.find()){
				String reg="\\*";
				String[] strs=data.split(reg);
				StringBuilder regmethod=new StringBuilder();
				for(int i=0;i<strs.length;i++){
					regmethod.append(strs[i]);
					if(i<strs.length-1){
						regmethod.append(".*");
					}
					
				}
				String regmethodstr=regmethod.toString();
				Pattern patternmeth=Pattern.compile(regmethodstr);
				Matcher matchermeth=patternmeth.matcher(se.getSimpleName());
				if(matchermeth.find()){
					return true;
				}
				else{
					return false;
				}
			
			}
			else{
				if(data.equals(se.getSimpleName())){
					return true;
				}
			}
			
		}
		
		return false;
	}
	public void setData(String data) {
		this.data = data.replaceAll("\\s", "");
//		if(this.data.equals("new")){
//			this.data="<init>";
//		}
	}
	public String getData() {
		return data;
	}




}
