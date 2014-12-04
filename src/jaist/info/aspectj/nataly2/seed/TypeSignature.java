package jaist.info.aspectj.nataly2.seed;



import jaist.info.aspectj.nataly2.metamodel.PSignature;
import jaist.info.aspectj.nataly2.metamodel.SElement;
import jaist.info.aspectj.nataly2.syntax.PClass;
import jaist.info.aspectj.nataly2.syntax.PPackage;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ICategories;

/*
 * currently this signature does not match (typepattern && typepattern), (typepattern || typattern) and (!typepattern).
 */
public class TypeSignature extends Signature {

	
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
					if(se.getCategory().equals(ICategories.CLASS.name())){
						if(p_signature.getPpackage().matches(se) && p_signature.getPclass().matches(se) ){
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

	private void printPSignature() {
		// TODO Auto-generated method stub	
		System.out.println("Package: "+p_signature.getPpackage().getData());
		System.out.println("Class: "+p_signature.getPclass().getData());
	}

	private void AnalyzeSignature() {
		// TODO Auto-generated method stub
		checkPC();
	}

	private void checkPC() {
		// TODO Auto-generated method stub
		PPackage pk=new PPackage();
		String regpack="[^.]*$";  //extract expression like this: *.*
		Pattern pattern2=Pattern.compile(regpack);
		Matcher matcher2=pattern2.matcher(tmp_sig);
		if(matcher2.find()){
			String sig=matcher2.group();
			String packagesig=matcher2.replaceFirst("");
			pk.setData(packagesig);
			checkClass(sig);
			//
		}
		else{
			
			pk.setData("");
			checkClass(sig);
		}
		p_signature.setPpackage(pk);
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

	

}
