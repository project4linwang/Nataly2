package jaist.info.aspectj.nataly2.code.generator;





import jaist.info.aspectj.nataly2.metamodel.RelationElements;
import jaist.info.aspectj.nataly2.seed.PointcutSignatureAnalyzer;
import jaist.info.aspectj.nataly2.seed.Signature;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.eclipse.core.resources.IProject;

/**
 * The facade of the code generator component..
 */
public class CodeGenerator {

	public static String getRelativeXMLFullFileName(String advpath){
		StringBuilder fileNameBuilder = new StringBuilder(advpath);
		fileNameBuilder.append("#" + "sourcecode");
		fileNameBuilder.append("-ptpattern.xml");
		return fileNameBuilder.toString();
	}
	private List<OSignature> analyzePointcutDesignator(String pcDecl){
		String regexpr="(\\:|\\|{2}|\\&{2})\\s*\\b\\w*\\b\\s*\\(([^)]*)\\){2}";
		Pattern pattern=Pattern.compile(regexpr);
		Matcher matcher=pattern.matcher(pcDecl);
		List<OSignature> siglist=new LinkedList<OSignature>();
		while(matcher.find()){
			OSignature osig=new OSignature();
			String semisig=matcher.group();			
			String regstr="\\:|\\|{2}|\\&{2}";
			Pattern logic_pattern=Pattern.compile(regstr);
			Matcher logic_matcher=logic_pattern.matcher(semisig);
			if(logic_matcher.find()){
				String logic=logic_matcher.group();
				if(logic.equals("||")){
					String sig= logic_matcher.replaceFirst("");
					if(sig!=null){
						
					    String regcategory="\\call\\b|\\bexecution\\b";
					    Pattern patterncategory=Pattern.compile(regcategory);
					    Matcher matchercategory=patterncategory.matcher(sig);
					    if(matchercategory.find()){
					    	String category=matchercategory.group();
					    	String regcon="new\\([^)]*\\)";
							Pattern patterncon=Pattern.compile(regcon);
							Matcher matchercon=patterncon.matcher(sig);
							if(matchercon.find()){
								String expr=category+"(*.new(..))";
								osig.setRelation("||");
								osig.setSig(expr);
							}
							else{
								String expr=category+"(* *.*(..))";
								osig.setRelation("||");
								osig.setSig(expr);
							}
					    }
					    else{
					    	
						    osig.setRelation("||");
						    osig.setSig(sig);
					    }																					
					}													    
				}
				else if(logic.equals("&&")){
					String sig= logic_matcher.replaceFirst("");
					if(sig!=null){
						
					    String regcategory="\\call\\b|\\bexecution\\b";
					    Pattern patterncategory=Pattern.compile(regcategory);
					    Matcher matchercategory=patterncategory.matcher(sig);
					    if(matchercategory.find()){
					    	String category=matchercategory.group();
					    	String regcon="new\\([^)]*\\)";
							Pattern patterncon=Pattern.compile(regcon);
							Matcher matchercon=patterncon.matcher(sig);
							if(matchercon.find()){
								String expr=category+"(*.new(..))";
								osig.setRelation("&&");
								osig.setSig(expr);
							}
							else{
								String expr=category+"(* *.*(..))";
								osig.setRelation("&&");
								osig.setSig(expr);
							}
					    }
					    else{
					    	
						    osig.setRelation("&&");
						    osig.setSig(sig);
					    }																					
					}
				}
				else if(logic.equals(":")){
					String sig= logic_matcher.replaceFirst("");
					//
                    if(sig!=null){
						
					    String regcategory="\\call\\b|\\bexecution\\b";
					    Pattern patterncategory=Pattern.compile(regcategory);
					    Matcher matchercategory=patterncategory.matcher(sig);
					    if(matchercategory.find()){
					    	String category=matchercategory.group();
					    	String regcon="new\\([^)]*\\)";
							Pattern patterncon=Pattern.compile(regcon);
							Matcher matchercon=patterncon.matcher(sig);
							if(matchercon.find()){
								String expr=category+"(*.new(..))";
								osig.setRelation("");
								osig.setSig(expr);
							}
							else{
								String expr=category+"(* *.*(..))";
								osig.setRelation("");
								osig.setSig(expr);
							}
					    }
					    else{
					    	
					    	osig.setRelation("");
						    osig.setSig(sig);
					    }																					
					}
				}
			}
			if(!isContainSig(siglist,osig)){
				siglist.add(osig);
			}
			
		}
		return siglist;
	}
	private boolean isContainSig(List<OSignature> sigs, OSignature sig){
		if(sigs.size()>0){
			for(OSignature osig:sigs){
				if(osig.getSig().equals(sig.getSig())){
					return true;
				}
			}
		}
		return false;
	}
	private String getPointcutFullName(String pcDecl){
	   String splitstr="\\:";
	   String[] results= pcDecl.split(splitstr);
	   if(results.length >0){
		   return results[0];
	   }
	   return "";
	}
	/**
	 * Generator the code of analysis-based pointcut, the input is name-based pointcut.
	 */
	public void gereratePointcutRefactor(String unit, String pcname, String pcDecl,IProject aproject){
		RelationElements relem=RelationElements.getRelationElements();
		String filepath=relem.getFilePath();
		String filename=getRelativeXMLFullFileName(filepath);				
				
        List<OSignature> sigs=analyzePointcutDesignator(pcDecl);
        StringBuilder sigbuilder=new StringBuilder();
        if(sigs.size()>0){
        	for(OSignature sig:sigs){
        		if(sig.getRelation().equals("")){
        	        sigbuilder.append(sig.getSig());
        		}
        		else{
        			sigbuilder.append(" "+sig.getRelation()+" ");
        			sigbuilder.append(sig.getSig());
        		}
        	}
        }
        String statement=sigbuilder.toString();
        String pcfullname=getPointcutFullName(pcDecl);
        System.out.println(statement);

		StringTokenizer unitst=new StringTokenizer(unit,".",false);
		String className="";
		while(unitst.hasMoreTokens()){
			className=unitst.nextToken();
			System.out.println(className);
			break;
		}
		StringTemplateGroup group=new StringTemplateGroup("mygroup");
		StringTemplate st=group.getInstanceOf("jaist/info/aspectj/nataly2/template/pointcut-refactor");
		st.setAttribute("statement", statement);
		st.setAttribute("condition_name", "isNeed"+pcname);
		st.setAttribute("pt_name", pcfullname);
        st.setAttribute("npt_name", pcname);

		AddSourcePointcut addpc=new AddSourcePointcut();
		addpc.addCode(unit, st.toString(),aproject);

	}
}
