package jaist.info.aspectj.nataly2.syntax;

import jaist.info.aspectj.nataly2.metamodel.ISignaturePattern;
import jaist.info.aspectj.nataly2.metamodel.SElement;



public class PType implements ISignaturePattern {

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
				if(data.equals(se.getType())){
					return true;
				}
				else{
					return false;
				}
			}
		}
		
	}
	private String data=null;

	public void setData(String data) {
		this.data = data.replaceAll("\\s", "");
	}
	public String getData() {
		return data;
	}
	
}
