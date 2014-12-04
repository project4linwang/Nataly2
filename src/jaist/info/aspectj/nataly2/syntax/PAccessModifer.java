package jaist.info.aspectj.nataly2.syntax;

import jaist.info.aspectj.nataly2.metamodel.ISignaturePattern;
import jaist.info.aspectj.nataly2.metamodel.SElement;


/**
 * Class of AccessModifer in the signature pattern
 * @author suse-wl
 *
 */
public class PAccessModifer implements ISignaturePattern {

	private String data=null;
	@Override
	public boolean matches(SElement se) {
		// TODO Auto-generated method stub
		if(data==null || data.equals("")){
			//it means no access restricked
			return true;
		}
		else{
			//check the access modifer of the method
			//e.g. if(data.equals(se.getAccessModifer()))
			//this function will be implemented in the future.
			return true;
		}
	}
	public void setData(String data) {
		this.data = data.replaceAll("\\s", "");
	}
	public String getData() {
		return data;
	}


}
