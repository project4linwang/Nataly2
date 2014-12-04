package jaist.info.aspectj.nataly2.seed;



import jaist.info.aspectj.nataly2.metamodel.PSignature;
import jaist.info.aspectj.nataly2.metamodel.SElement;
import jaist.info.aspectj.nataly2.syntax.PAccessModifer;
import jaist.info.aspectj.nataly2.syntax.PEClass;
import jaist.info.aspectj.nataly2.syntax.PMethod;
import jaist.info.aspectj.nataly2.syntax.PPackage;
import jaist.info.aspectj.nataly2.syntax.PParameter;
import jaist.info.aspectj.nataly2.syntax.PReturnType;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ICategories;

/**
 * Class of signature is corresponded to type of method.
 * @author suse-wl
 *
 */
public class MethodExeSignature extends Signature {

	private String tmp_sig;
	private PSignature p_signature=new PSignature();
	
	@Override
	public List<String> CatchRelativeElements(List<SElement> selems) {
		// TODO Auto-generated method stub
		List<String> candidatelist=new LinkedList<String>();
		tmp_sig=this.sig;
		AnalyzeSignature();
		if(selems!=null){
			if(selems.size()>0){
				for(SElement se:selems){
					if(se.getCategory().equals(ICategories.METHOD.name())){
						if(se.getTraditionalFullName().equals("model.Cell.toString()")){
							System.out.println();
						}
						if(p_signature.getAccessModifer().matches(se) && p_signature.getPpackage().matches(se) && p_signature.getPeclass().matches(se) &&
								p_signature.getReturntype().matches(se) && p_signature.getPmethod().matches(se) && p_signature.getPpara().matches(se)){
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

	private void AnalyzeSignature(){
		
	
		String regStr="\\bthrows\\b.*";
		boolean flag=false;
		Pattern pattern=Pattern.compile(regStr);
		Matcher matcher=pattern.matcher(sig);
		String execeptionstr="";
		while(matcher.find()){
			flag=true;
			execeptionstr=matcher.group();
			this.tmp_sig=matcher.replaceFirst("");
			break;
		}
		if(flag){
			// extract method signature without exception expression
			p_signature.setException(execeptionstr);
			System.out.println("Exception expression!!"+execeptionstr);
			checkAccessModifiers();
		}
		else{
			checkAccessModifiers();
		}
		
		checkReturnType();
		checkPCM();
		checkParaMeter();
	}
	/*
	 * first step check Access Modifiers: public private and protected
	 */
    private void checkAccessModifiers(){
    	PAccessModifer am=new PAccessModifer();
		String regstr="^!*\\bpublic\\b |\\bprivate\\b|\\bprotected\\b";
		Pattern pattern=Pattern.compile(regstr);
		Matcher matcher=pattern.matcher(tmp_sig);		
		if(matcher.find()){
			am.setData(matcher.group());			
			tmp_sig=matcher.replaceFirst("");
		}
		p_signature.setAccessModifer(am);
		
	}
    /*
     * Second step, check Return Type. Every signature pattern has a return type element.
     */
    private void checkReturnType(){
    	PReturnType rt=new PReturnType();
    	String regstr="^[\\w\\*\\+\\[\\]{0,2}\\<\\w\\>\\.\\w]*\\s";
    	Pattern pattern=Pattern.compile(regstr);
    	Matcher matcher=pattern.matcher(tmp_sig);
    	if(matcher.find()){
    		rt.setData(matcher.group());
    		tmp_sig=matcher.replaceFirst("");
    	}
        p_signature.setReturntype(rt);
    }
    /*
     * Third step, check package. But the package is not exist in every signature pattern. 
     * If the package data is null, it means that this pattern does not have pacakage element.
     */
    private void checkPCM(){
    	PPackage pk=new PPackage();
    	String regbracket="^[^(]*";            //extract expression without ()
    	Pattern pattern=Pattern.compile(regbracket);
    	Matcher matcher=pattern.matcher(tmp_sig);
    	if(matcher.find()){
    		String expr=matcher.group();
    		String regpack="[\\w\\*]*(|[+])\\.{1}[\\w\\*]*\\s*$";  //extract expression like this: *.*
    		Pattern pattern2=Pattern.compile(regpack);
    		Matcher matcher2=pattern2.matcher(expr);
    		if(matcher2.find()){
    			String sig=matcher2.group();
    			String packagesig=matcher2.replaceFirst("");
    			pk.setData(packagesig);
    			checkClass(sig);
    			checkMethod(sig);
    		}
    		else{
    			//the pattern is like this: * *(..), so do not need to match the package
    			pk.setData("");
    		}
    		p_signature.setPpackage(pk);
    	}
    	else{
    		System.out.println("It is not a correct method signature!!");
    	}
    }
    private void checkClass(String classsig){
    	PEClass pclass=new PEClass();
    	String regstr="^[^.]*";
    	Pattern pattern=Pattern.compile(regstr);
    	Matcher matcher=pattern.matcher(classsig);
    	if(matcher.find()){
    	   pclass.setData(matcher.group());
    	}
    	p_signature.setPeclass(pclass);
    }
    private void checkMethod(String methodsig){
    	PMethod pm=new PMethod();
    	String regstr="[^.]*$";
    	Pattern pattern=Pattern.compile(regstr);
    	Matcher matcher=pattern.matcher(methodsig);
    	if(matcher.find()){
    		pm.setData(matcher.group());
    	}
    	p_signature.setPmethod(pm);
    }
    private void checkParaMeter(){
    	PParameter pp=new PParameter();
    	String regstr="\\(([^)]*)\\)";
    	Pattern pattern=Pattern.compile(regstr);
    	Matcher matcher=pattern.matcher(tmp_sig);
    	if(matcher.find()){
    		pp.setData(matcher.group(1));
    	}
    	p_signature.setPpara(pp);
    }

	
}
