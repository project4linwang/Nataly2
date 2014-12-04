package jaist.info.aspectj.nataly2.seed;



import jaist.info.aspectj.nataly2.metamodel.PSignature;
import jaist.info.aspectj.nataly2.metamodel.SElement;
import jaist.info.aspectj.nataly2.syntax.PAccessModifer;
import jaist.info.aspectj.nataly2.syntax.PClass;
import jaist.info.aspectj.nataly2.syntax.PField;
import jaist.info.aspectj.nataly2.syntax.PPackage;
import jaist.info.aspectj.nataly2.syntax.PType;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ICategories;

/**
 * Class of  signature is corresponded to the type of field.
 * @author suse-wl
 *
 */
public class FieldSignature extends Signature {

	
	private String tmp_sig;
	private PSignature p_signature=new PSignature();
	@Override
	public List<String> CatchRelativeElements(List<SElement> selems) {
		// TODO Auto-generated method stub
		List<String> candidatelist=new LinkedList<String>();
		tmp_sig=this.sig;
		AnalyzeSignature();
		//printPSignature();
		if(selems!=null){
			if(selems.size()>0){
				for(SElement se:selems){
					if(se.getCategory().equals(ICategories.FIELD.name())){
						if(p_signature.getAccessModifer().matches(se) && p_signature.getPpackage().matches(se) && p_signature.getPclass().matches(se) &&
								p_signature.getPtype().matches(se) && p_signature.getPfield().matches(se)){
								//We do not implement the function to check the throw exception,in the future we need to check this
							    //in this case, this element is the join point candidate
								candidatelist.add(se.getTraditionalFullName());
							}
					}
				}
			}
		}
		return candidatelist;
	}
	private void checkPCF() {
		// TODO Auto-generated method stub
		PPackage pk=new PPackage();
    		String regpack="[\\w\\*]*(|[+])\\.{1}[\\w\\*]*\\s*$";  //extract expression like this: *.*
    		Pattern pattern2=Pattern.compile(regpack);
    		Matcher matcher2=pattern2.matcher(tmp_sig);
    		if(matcher2.find()){
    			String sig=matcher2.group();
    			String packagesig=matcher2.replaceFirst("");
    			pk.setData(packagesig);
    			checkClass(sig);
    			checkField(sig);
    			//
    		}
    		else{
    			
    			pk.setData("");
    		}
    		p_signature.setPpackage(pk);

	}
	private void checkField(String methodsig) {
		// TODO Auto-generated method stub
		PField pf=new PField();
    	String regstr="[^.]*$";
    	Pattern pattern=Pattern.compile(regstr);
    	Matcher matcher=pattern.matcher(methodsig);
    	if(matcher.find()){
    		pf.setField(matcher.group());
    	}
    	p_signature.setPfield(pf);
		
	}
	private void checkClass(String classsig) {
		// TODO Auto-generated method stub
		PClass pclass=new PClass();
    	String regstr="^[^.]*";
    	Pattern pattern=Pattern.compile(regstr);
    	Matcher matcher=pattern.matcher(classsig);
    	if(matcher.find()){
    	   pclass.setData(matcher.group());
    	}
    	p_signature.setPclass(pclass);
	}
	private void AnalyzeSignature() {
		// TODO Auto-generated method stub
		checkAccessModifiers();
		checkType();
		checkPCF();
	}
	private void checkType() {
		// TODO Auto-generated method stub
		String regspace="^\\s*";
		tmp_sig= tmp_sig.replaceFirst(regspace, "");
		
		PType rt=new PType();
    	String regstr="^[\\w\\*\\[\\]{0,2}\\<\\w\\>\\.\\w]*\\s";
    	Pattern pattern=Pattern.compile(regstr);
    	Matcher matcher=pattern.matcher(tmp_sig);
    	if(matcher.find()){
    		rt.setData(matcher.group());
    		//System.out.println("Type: "+rt.getData());
    		tmp_sig=matcher.replaceFirst("");
    	}
        p_signature.setPtype(rt);
	}
	private void checkAccessModifiers() {
		// TODO Auto-generated method stub
		PAccessModifer am=new PAccessModifer();
		String regstr="^!*\\bpublic\\b |\\bprivate\\b|\\bprotected\\b";
		Pattern pattern=Pattern.compile(regstr);
		Matcher matcher=pattern.matcher(tmp_sig);		
		if(matcher.find()){
			am.setData(matcher.group());
			//System.out.println("Find: "+matcher.group()); 
			tmp_sig=matcher.replaceFirst("");
		}
		p_signature.setAccessModifer(am);
	}
	private void printPSignature(){
		System.out.println("AccessModifer: "+p_signature.getAccessModifer().getData());
		System.out.println("Type: "+ p_signature.getPtype().getData());
		System.out.println("Package: "+p_signature.getPpackage().getData());
		System.out.println("Class: "+p_signature.getPclass().getData());
		System.out.println("Field: "+p_signature.getPfield().getField());
		
	}
	

}
