package jaist.info.aspectj.nataly2.syntax;

import jaist.info.aspectj.nataly2.metamodel.ISignaturePattern;
import jaist.info.aspectj.nataly2.metamodel.SElement;


/**
 * Class of field signature in signature pattern
 * @author suse-wl
 *
 */
public class PField implements ISignaturePattern {

	private String data;
	@Override
	public boolean matches(SElement se) {
		// TODO Auto-generated method stub
		if(data==null || data.equals("")){
			return false;
		}
		else{
			if(data.equals("*")){
				return true;
			}
			else if(data.equals(se.getSimpleName())){
				return true;
			}
		}
		return false;
	}
	
	public void setField(String field) {
		this.data = field.replaceAll("\\s", "");
	}
	public String getField() {
		return data;
	}

	

}
