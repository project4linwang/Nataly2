package jaist.info.aspectj.nataly2.syntax;

import jaist.info.aspectj.nataly2.metamodel.ISignaturePattern;
import jaist.info.aspectj.nataly2.metamodel.RelationElements;
import jaist.info.aspectj.nataly2.metamodel.SElement;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.ui.activities.ICategory;

import model.ICategories;


/**
 * Class of arguments signature in signature pattern.
 * @author suse-wl
 *
 */
public class PParameter implements ISignaturePattern {

	RelationElements relation_elems=RelationElements.getRelationElements();
	List<SElement> selems=relation_elems.getSelementlist();
	private String data=null;
	@Override
	public boolean matches(SElement se) {
		// TODO Auto-generated method stub
		if(data==null){
			return true;
		}
		else if(data.equals("")){
			if(se.getArglists()!=null){
				if(se.getArglists().size()==0){
					return true;
				}
				else{
					return false;
				}
			}
			else{
				return true;
			}
			
		}
		else{
			
				if(data.contains(",")){
					boolean flag=false;
					//not only one argument
					String sep=",";
					String[] chars= data.split(sep);
					List<String> args=new LinkedList<String>();
					List<String> arglist_copy=new LinkedList<String>();
					arglist_copy.addAll(se.getArglists());
					//get the simple name of the parameter
					String regrm="[^.]*$";
					Pattern patternrm=Pattern.compile(regrm);
					if(arglist_copy.size()>0){
						for(int i=0;i<arglist_copy.size();i++){
							Matcher matcherrm=patternrm.matcher(arglist_copy.get(i));							
							if(matcherrm.find()){
								String replace_str=matcherrm.group();
								arglist_copy.set(i,replace_str );
							}
						}
						
					}
					//
					for(String str:chars){
					   if(str.equals("..")){
						   flag=true;
					   }
					   else{
						   args.add(str);
					   }
					}
					if(flag){
						//the expression include ".."
						List<String> parentTypeList=new LinkedList<String>();
						List<String> normalTypeList=new LinkedList<String>();
						for(String arg:args){
							//check arg include "+"
							//
							String regstr="\\+";
							Pattern pattern=Pattern.compile(regstr);
							Matcher matcher=pattern.matcher(arg);
							
							if(matcher.find()){
								String parentClass=matcher.replaceFirst("");
								parentTypeList.add(parentClass);
								
							}
							else{
								normalTypeList.add(arg);
							}
						}
							if(normalTypeList.size()>0){
								
								for(String nstr:normalTypeList){
									if(arglist_copy.size()>0){
										if(arglist_copy.contains(nstr)){
											for(String candidate:arglist_copy){
												if(nstr.equals(candidate)){
													arglist_copy.remove(candidate);
													break;
												}
											}
										}
										else{
											return false;
										}
									}
									else{
										return false;
									}
									
								}
							}
							if(parentTypeList.size()>0){
								for(String pstr:parentTypeList){
									if(arglist_copy.contains(pstr)){
										for(String candidate:arglist_copy){
											if(pstr.equals(candidate)){
												arglist_copy.remove(candidate);
												break;
											}
										}
									}
									else{
										SElement el=findElement(pstr);
										boolean ismatch=false;
										if(el!=null){
											List<String> subType=el.getSubClass();
											for(String stype:subType){
												if(arglist_copy.contains(stype)){
													for(String candidate:arglist_copy){
														if(stype.equals(candidate)){
															arglist_copy.remove(candidate);
															ismatch=true;
															break;
														}
													}
												}
											}
											
										}
										if(!ismatch){
											return false;
										}
									}
									
								}
								
							}
						
						return true;
					}
					else{
						//the expression does not include "..", it should match every argument element.
						if(args.size()!=arglist_copy.size()){
							return false;
						}
						else{
							List<String> parentTypeList=new LinkedList<String>();
							List<String> normalTypeList=new LinkedList<String>();
							for(String arg:args){
								//check arg include "+"
								//
								String regstr="\\+";
								Pattern pattern=Pattern.compile(regstr);
								Matcher matcher=pattern.matcher(arg);
								
								if(matcher.find()){
									String parentClass=matcher.replaceFirst("");
									parentTypeList.add(parentClass);
									
								}
								else{
									normalTypeList.add(arg);
								}
							}
								if(normalTypeList.size()>0){
									
									for(String nstr:normalTypeList){
										if(arglist_copy.size()>0){
											if(arglist_copy.contains(nstr)){
												for(String candidate:arglist_copy){
													if(nstr.equals(candidate)){
														arglist_copy.remove(candidate);
														break;
													}
												}
											}
											else{
												return false;
											}
										}
										else{
											return false;
										}
										
									}
								}
								if(parentTypeList.size()>0){
									for(String pstr:parentTypeList){
										if(arglist_copy.contains(pstr)){
											for(String candidate:arglist_copy){
												if(pstr.equals(candidate)){
													arglist_copy.remove(candidate);
													break;
												}
											}
										}
										else{
											SElement el=findElement(pstr);
											boolean ismatch=false;
											if(el!=null){
												List<String> subType=el.getSubClass();
												for(String stype:subType){
													if(arglist_copy.contains(stype)){
														for(String candidate:arglist_copy){
															if(stype.equals(candidate)){
																arglist_copy.remove(candidate);
																ismatch=true;
																break;
															}
														}
													}
												}
												
											}
											if(!ismatch){
												return false;
											}
										}
										
									}
									
								}
							
							return true;
						}
					}
						
				}
				else{
					//only one element.
					String regvar="\\.\\.\\.{1}";
					Pattern patternvar=Pattern.compile(regvar);
					Matcher matchervar=patternvar.matcher(data);
					if(matchervar.find()){
						//including this pattern: Objcet...args
						String regstr="^[^\\.\\.\\.{1}]\\w*";
						Pattern pattern=Pattern.compile(regstr);
						Matcher matcher=pattern.matcher(data);
						String m_data="";
						if(matcher.find()){
							m_data=matcher.group()+"[]";
						}
						if(se.getArglists().contains(m_data)){
							return true;
						}
						else{
							return false;
						}
						
					}
					else{
						if(data.equals("..")){
							return true;
						}
						else{
							//single element
							//check +
							//implement in the future
							
							String regstr="\\+";
							Pattern pattern=Pattern.compile(regstr);
							Matcher matcher=pattern.matcher(data);
							if(matcher.find()){
								//need to check subclass
								String parentClass=matcher.replaceFirst("");
								SElement el=findElement(parentClass);
								if(el!=null){
									for(String str:el.getSubClass()){
								    	if(se.getArglists().size()==1){
											 if(se.getArglists().contains(str)){
												  return true;
											  }
										}
								    }
								}
								else{
									return false;
								}
							    
							}
							else
							{
								if(se.getArglists().size()==1){
									if(data.contains("\\.")){
										 if(se.getArglists().contains(data)){
											  return true;
										  }
									}
									else{
										if(removePackage(se.getArglists().get(0)).equals(data)){
											return true;
										}
									}
									
								}
							 
							}
							
						}
					}
					
				}
			
		}
		return false;
	}
	private String removePackage(String str){
		String res=str;
		String regstr="[^.]*$";
		Pattern pattern=Pattern.compile(regstr);
		Matcher matcher=pattern.matcher(str);
		if(matcher.find()){
			res=matcher.group();
		}
		return res;
	}
	public void setData(String data,Object...args) {
		this.data = data.replaceAll("\\s", "");
		
	}
	public String getData() {
		return data;
	}
	private SElement findElement(String name){
		if(selems!=null){
			for(SElement se:selems){
				if(se.getCategory().equals(ICategories.CLASS.name())){
					if(se.getSimpleName().equals(name)|| se.getTraditionalFullName().equals(name)){
						return se;
					}
				}
			}
		}
		return null;
		
	}

}
