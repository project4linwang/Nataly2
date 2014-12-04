package jaist.info.aspectj.nataly2.seed;



import jaist.info.aspectj.nataly2.metamodel.PCSignature;
import jaist.info.aspectj.nataly2.metamodel.RelationElements;
import jaist.info.aspectj.nataly2.metamodel.SElement;
import jaist.info.aspectj.nataly2.tools.NameWildCardPatternUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.JavaModelException;

import model.ICategories;

/**
 * Class for analyze the pointcut signature
 * @author suse-wl
 *
 */
public class PointcutSignatureAnalyzer {

	private String pc_name;
	private String pc_type;
	private boolean isConstructor=false;
	private List<List<Signature>> pointcuts=new LinkedList<List<Signature>>();
	private RelationElements relation_elems=RelationElements.getRelationElements();
	public String getPC_Type(){
		return pc_type;
	}
	/**
	 * Analyze the code of pointcut signature
	 * @param pc_name is the name of pointcut
	 * @param pc_fullname is the contents of a given pointcut
	 * @return
	 */
	public List<Seed> AnalyzePointcut(String pc_name,String pc_fullname){
		this.pc_name=pc_name;
		
		List<Seed> matchedList=new LinkedList<Seed>();
		String regcall="(\\:|\\|{2}|\\&{2})\\s*\\bcall\\b\\s*\\(([^)]*)\\){2}";
		String regexe="(\\:|\\|{2}|\\&{2})\\s*\\bexecution\\b\\s*\\(([^)]*)\\){2}";
		String reginit="(\\:\\|{2}|\\&{2})\\s*\\binitialization\\b\\s*\\(([^)]*)\\){2}";
		String regpreinit="(\\:|\\|{2}|\\&{2})\\s*\\bpreinitialization\\b\\s*\\(([^)]*)\\){2}";
		List<Signature> tmpcallsig=analyzeSignature(pc_fullname,regcall,"call");
		if(tmpcallsig.size()>0){
			pointcuts.add(tmpcallsig);
		}
		List<Signature> tmpexesig=analyzeSignature(pc_fullname,regexe,"execution");
		if(tmpexesig.size()>0){
			pointcuts.add(tmpexesig);
		}
		List<Signature> tmpinit=analyzeSignature(pc_fullname,reginit,"initialization");
		if(tmpinit.size()>0){
			pointcuts.add(tmpinit);
		}
		List<Signature> tmpreinit=analyzeSignature(pc_fullname,regpreinit,"preinitialization");
		if(tmpreinit.size()>0){
			pointcuts.add(tmpreinit);
		}

		//System.out.println("Pointcut: "+pc_name);
		if(pointcuts!=null){
			for(List<Signature> pcsigs:pointcuts){
				//
				PCSignature pcs=new PCSignature();
				pcs.setPcname(this.pc_name);
				pcs.setSignatures(pcsigs);
				NameWildCardPatternUtil.writeNWPattern(pcs, relation_elems.getFilePath(), pc_name);
				//
				Seed seed=new Seed();
				String category=null;
				for(Signature pcsig:pcsigs){
					 category=pcsig.getCategory();
				 }
				 List<String> matched= CatchRelativeElement(pcsigs);
				 seed.setSeedList(matched);
				 seed.setCategory(category);
				 matchedList.add(seed);
			}
		}
		return matchedList;
		//
	}
	private List<Signature> analyzeSignature(String pointcut,String reg,String category) {
		List<Signature> pc_signature=new LinkedList<Signature>();
		List<Signature> conjunction_sigs=new LinkedList<Signature>();
		List<Signature> disjunction_sigs=new LinkedList<Signature>();
		
		Pattern pattern=Pattern.compile(reg);
		Matcher matcher=pattern.matcher(pointcut);
		while(matcher.find()){
			String semisig=matcher.group();			
			String regstr="\\:|\\|{2}|\\&{2}";
			Pattern logic_pattern=Pattern.compile(regstr);
			Matcher logic_matcher=logic_pattern.matcher(semisig);
			if(logic_matcher.find()){
				String logic=logic_matcher.group();
				if(logic.equals("||")){
					String sig= logic_matcher.replaceFirst("");
					/*
					 * check it is the methodsignature or constractorsignature
					 */
					if(sig!=null){
						
						
						String regcon="new\\([^)]*\\)";
						Pattern patterncon=Pattern.compile(regcon);
						Matcher matchercon=patterncon.matcher(sig);
						if(matchercon.find()){	
							Signature tmpsig=getMethodSignature(sig,category);
							tmpsig.setCategory(category);
							if(tmpsig!=null){
								disjunction_sigs.add(tmpsig);
							}												
							this.pc_type=ICategories.METHOD.name();
							this.isConstructor=true;
						}
						else{
							if(category.equals("execution")){
								Signature tmpsig=getMethodExeSignature(sig,category);
								tmpsig.setCategory(category);
								if(tmpsig!=null){
									disjunction_sigs.add(tmpsig);
								}
							}
							else{
								Signature tmpsig=getMethodSignature(sig,category);
								tmpsig.setCategory(category);
								if(tmpsig!=null){
									disjunction_sigs.add(tmpsig);
								}
							}
							
											
							this.pc_type=ICategories.METHOD.name();
						}
						
					}
				}
				else if(logic.equals("&&")){
					String sig= logic_matcher.replaceFirst("");
					/*
					 * check it is the methodsignature or constractorsignature
					 */
					if(sig!=null){
						
						String regcon="new\\([^)]*\\)";
						Pattern patterncon=Pattern.compile(regcon);
						Matcher matchercon=patterncon.matcher(sig);
						if(matchercon.find()){
							if(disjunction_sigs.size()!=0){
								Signature tmpsig=getMethodSignature(sig,category);
								tmpsig.setCategory(category);
								List<Signature> tmpconsigs=new LinkedList<Signature>();
								if(tmpsig!=null){
									if(conjunction_sigs.size()>0){
										for(int i=0;i<conjunction_sigs.size();i++){
											if(conjunction_sigs.get(i).getSig().equals(tmpsig.getSig())){
												tmpconsigs.add(tmpsig);
											}
										}
									}	
								}
								conjunction_sigs=tmpconsigs;
							}
							else{
								
								Signature tmpsig=getMethodSignature(sig,category);
								tmpsig.setCategory(category);
								if(tmpsig!=null){
									pc_signature.add(tmpsig);
								}
							}
							
							this.pc_type=ICategories.METHOD.name();
							this.isConstructor=true;
						}
						else{
							if(disjunction_sigs.size()!=0){
								if(category.equals("execution")){
									Signature tmpsig=getMethodExeSignature(sig,category);
									tmpsig.setCategory(category);
									List<Signature> tmpconsigs=new LinkedList<Signature>();
									if(tmpsig!=null){
										if(conjunction_sigs.size()>0){
											for(int i=0;i<conjunction_sigs.size();i++){
												if(conjunction_sigs.get(i).getSig().equals(tmpsig.getSig())){
													tmpconsigs.add(tmpsig);
												}
											}
										}	
									}
									conjunction_sigs=tmpconsigs;
								}
								else{
									Signature tmpsig=getMethodSignature(sig,category);
									tmpsig.setCategory(category);
									List<Signature> tmpconsigs=new LinkedList<Signature>();
									if(tmpsig!=null){
										if(conjunction_sigs.size()>0){
											for(int i=0;i<conjunction_sigs.size();i++){
												if(conjunction_sigs.get(i).getSig().equals(tmpsig.getSig())){
													tmpconsigs.add(tmpsig);
												}
											}
										}	
									}
									conjunction_sigs=tmpconsigs;
								}
																
							}
							else{
								if(category.equals("execution")){
									Signature tmpsig=getMethodExeSignature(sig,category);
									tmpsig.setCategory(category);
									if(tmpsig!=null){
										pc_signature.add(tmpsig);
									}
								}
								else{
									Signature tmpsig=getMethodSignature(sig,category);
									tmpsig.setCategory(category);
									if(tmpsig!=null){
										pc_signature.add(tmpsig);
									}
								}
								
								
							}
							
							this.pc_type=ICategories.METHOD.name();
						}
						
					}
				}
				else if(logic.equals(":")){
					String sig= logic_matcher.replaceFirst("");
					/*
					 * check it is the methodsignature or constractorsignature
					 */
					if(sig!=null){
						
						String regcon="new\\([^)]*\\)";
						Pattern patterncon=Pattern.compile(regcon);
						Matcher matchercon=patterncon.matcher(sig);
						if(matchercon.find()){
							if(category.equals("execution")){
								Signature tmpsig=getMethodExeSignature(sig,category);
								tmpsig.setCategory(category);
								if(tmpsig!=null){
									disjunction_sigs.add(tmpsig);
								}
							}
							else{
								Signature tmpsig=getMethodSignature(sig,category);
								tmpsig.setCategory(category);
								if(tmpsig!=null){
									disjunction_sigs.add(tmpsig);
								}
							}
							
							this.pc_type=ICategories.METHOD.name();
							this.isConstructor=true;
						}
						else{
							if(category.equals("execution")){
								Signature tmpsig=getMethodExeSignature(sig,category);
								tmpsig.setCategory(category);
								if(tmpsig!=null){
									disjunction_sigs.add(tmpsig);
								}
							}
							else{
								Signature tmpsig=getMethodSignature(sig,category);
								tmpsig.setCategory(category);
								if(tmpsig!=null){
									disjunction_sigs.add(tmpsig);
								}
							}
							
							this.pc_type=ICategories.METHOD.name();
						}
						
					}
				}
		
			}
		}
		//
		if(disjunction_sigs.size()>0 && conjunction_sigs.size()>0){
			for(Signature consig: conjunction_sigs){
				   for(Signature disconsig:disjunction_sigs){
					   if(disconsig.getSig().equals(consig.getSig())){
						   pc_signature.add(disconsig);
					   }
				   }
			}
		}
		else{
			for(Signature disconsig: disjunction_sigs){				   					   
				pc_signature.add(disconsig);					   				   
			}
		}
		
		return pc_signature;
	}
	private MethodExeSignature getMethodExeSignature(String expression,String category){
		MethodExeSignature m_sig=new MethodExeSignature();
		
		String sig=null;
		String regStr=category+"\\s*\\(([^)]*\\).*?)\\)";
		Pattern pattern=Pattern.compile(regStr);
		Matcher matcher=pattern.matcher(expression);
		if(matcher.find()){
			sig=matcher.group(1);
			m_sig.setSig(sig);
		}
		else{
			m_sig=null;
		}
		
		return m_sig;
	}
	private MethodSignature getMethodSignature(String expression,String category){
		MethodSignature m_sig=new MethodSignature();
		
		String sig=null;
		String regStr=category+"\\s*\\(([^)]*\\).*?)\\)";
		Pattern pattern=Pattern.compile(regStr);
		Matcher matcher=pattern.matcher(expression);
		if(matcher.find()){
			sig=matcher.group(1);
			m_sig.setSig(sig);
		}
		else{
			m_sig=null;
		}
		
		return m_sig;
	}

	/*
	 * catch the list of elements by using the signature
	 * 1. 
	 */
	private List<String> CatchRelativeElement(List<Signature> signatures){
		RelationElements relation_elems=RelationElements.getRelationElements();
		//List<Element> elements=relation_elems.getElementList();
		List<String> matchedList=new LinkedList<String>();
		List<SElement> selems=relation_elems.getSelementlist();
		if(signatures!=null){
			for(Signature sig: signatures){
				matchedList.addAll( sig.CatchRelativeElements(selems));
			}
		}
		return matchedList;
	}
	
	public boolean isConstructor() {
		return isConstructor;
	}
}
