package jaist.info.aspectj.nataly2.syntax;

import jaist.info.aspectj.nataly2.metamodel.ISignaturePattern;
import jaist.info.aspectj.nataly2.metamodel.SElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class of package signature in signature pattern.
 * @author suse-wl
 *
 */
public class PPackage implements ISignaturePattern {

	private String data=null;
	@Override
	public boolean matches(SElement se) {
		// TODO Auto-generated method stub
		if(data==null ||data.equals("")){
			return true;
		}
		else{
			if(data.equals("*")){
				return true;
			}
			else{
				String regstr="\\.\\.{1}$";
				Pattern pattern=Pattern.compile(regstr);
				Matcher matcher=pattern.matcher(data);
				if(matcher.find()){
					//javax..
					String regstr2="^"+matcher.replaceFirst("")+".*";
					Pattern pattern2=Pattern.compile(regstr2);
					Matcher matcher2=pattern2.matcher(se.getPackagename());
					if(matcher2.find()){
						return true;
					}
					else{
						return false;
					}
				}
				else{
					//java.io.
					//remove the last .
					String m_package=data.replaceFirst("\\.{1}$", "");
					if(m_package.equals(se.getPackagename())){
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
