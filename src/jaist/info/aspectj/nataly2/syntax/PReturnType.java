package jaist.info.aspectj.nataly2.syntax;

import jaist.info.aspectj.nataly2.metamodel.ISignaturePattern;
import jaist.info.aspectj.nataly2.metamodel.SElement;


/**
 * Class of return signature in signature pattern.
 * @author suse-wl
 *
 */
public class PReturnType implements ISignaturePattern {

	private String data=null;
	@Override
	public boolean matches(SElement se) {
		// TODO Auto-generated method stub
		if(data==null || data.equals("")){
			return true;
		}
		else{
			if(data.equals("*")){
				return true;
			}
			else{
				if(data.equals("Object+")){
					if(se.getReturnType().equals("void")){
						return false;
					}
					else{
						return true;
					}
				}
				else{
					if(data.equals(se.getReturnType())){
						return true;
					}
					else{
						return false;
					}
				}
				
			}
		}
		
	}
	public void setData(String data) {
		this.data = data.replaceAll("\\s", "");
	}
	public String getData() {
		return data;
	}
	

}
