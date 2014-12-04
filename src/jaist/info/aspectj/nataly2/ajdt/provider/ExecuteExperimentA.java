package jaist.info.aspectj.nataly2.ajdt.provider;


import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.aspectj.ajdt.internal.compiler.ast.PointcutDeclaration;
import org.aspectj.ajdt.internal.core.builder.AjBuildManager;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.Argument;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.patterns.Pointcut;
import org.eclipse.ajdt.core.builder.AJBuilder;
import org.eclipse.ajdt.core.builder.IAJBuildListener;
import org.eclipse.core.runtime.Assert;

public class ExecuteExperimentA {

	
	private AspectJShadows ajsh=AspectJShadows.getAspectJShadowsInstance();
	
	public List<String> getCallJoinPointShadows(){
		List<PointcutDeclaration> pointuctList=ajsh.getPoincutList();
		List<Shadow> shadows=ajsh.getShadowList();
		List<String> candidates=new LinkedList<String>();
		for(PointcutDeclaration pd : pointuctList){         
			Pointcut pc=pd.pointcutDesignator.getPointcut();
			Argument[] pcarg= pd.arguments;
			
			MethodBinding mb= pd.binding; 
			String method= mb.toString();
			String declarationname=null;
			StringTokenizer st=new StringTokenizer(method,"$");
			String oldpart="";
			while(st.hasMoreElements()){
				
				String decl= st.nextToken();
				if(oldpart.equals("pointcut")){
					declarationname=decl;
					break;
				}
				oldpart=decl;
			}

		    if(declarationname.equals("analysispccalltest")){
		    	//get the join point, which is matched analysispccalltest
		    	//shadows
				for(Shadow sh: shadows){
				//String fuzzy=	pc.match(sh).toString();
				//System.out.println("Fuzzy : "+sh.getSignature().getName()+" "+fuzzy);
				  if(pc.match(sh)==FuzzyBoolean.YES || pc.match(sh)==FuzzyBoolean.MAYBE){
					String joinpoint=ShadowToString(sh);
					candidates.add(joinpoint);
				  }					
				}
		    }					
		}
		return candidates;
	}
	public List<String> getExeJoinPointShadows(){
		List<PointcutDeclaration> pointuctList=ajsh.getPoincutList();
		List<Shadow> shadows=ajsh.getShadowList();
		List<String> candidates=new LinkedList<String>();
		for(PointcutDeclaration pd : pointuctList){         
			Pointcut pc=pd.pointcutDesignator.getPointcut();
			Argument[] pcarg= pd.arguments;
			
			MethodBinding mb= pd.binding; 
			String method= mb.toString();
			String declarationname=null;
			StringTokenizer st=new StringTokenizer(method,"$");
			String oldpart="";
			while(st.hasMoreElements()){
				
				String decl= st.nextToken();
				if(oldpart.equals("pointcut")){
					declarationname=decl;
					break;
				}
				oldpart=decl;
			}

		    if(declarationname.equals("analysisexecutiontest")){
		    	//get the join point, which is matched analysispccalltest
		    	//shadows
				for(Shadow sh: shadows){
				//String fuzzy=	pc.match(sh).toString();
				//System.out.println("Fuzzy : "+sh.getSignature().getName()+" "+fuzzy);
				  if(pc.match(sh)==FuzzyBoolean.YES || pc.match(sh)==FuzzyBoolean.MAYBE){
					String joinpoint=ShadowToString(sh);					
					candidates.add(changeInitName(joinpoint));
				  }					
				}
		    }					
		}
		return candidates;
	}
	public List<String> getInitJoinPointShadows(){
		List<PointcutDeclaration> pointuctList=ajsh.getPoincutList();
		List<Shadow> shadows=ajsh.getShadowList();
		List<String> candidates=new LinkedList<String>();
		for(PointcutDeclaration pd : pointuctList){         
			Pointcut pc=pd.pointcutDesignator.getPointcut();
			Argument[] pcarg= pd.arguments;
			
			MethodBinding mb= pd.binding; 
			String method= mb.toString();
			String declarationname=null;
			StringTokenizer st=new StringTokenizer(method,"$");
			String oldpart="";
			while(st.hasMoreElements()){
				
				String decl= st.nextToken();
				if(oldpart.equals("pointcut")){
					declarationname=decl;
					break;
				}
				oldpart=decl;
			}

		    if(declarationname.equals("analysisinittest")){
		    	//get the join point, which is matched analysispccalltest
		    	//shadows
				for(Shadow sh: shadows){
				//String fuzzy=	pc.match(sh).toString();
				//System.out.println("Fuzzy : "+sh.getSignature().getName()+" "+fuzzy);
				  if(pc.match(sh)==FuzzyBoolean.YES || pc.match(sh)==FuzzyBoolean.MAYBE){
					String joinpoint=ShadowToString(sh);						
					candidates.add(changeInitName(joinpoint));
				  }					
				}
		    }					
		}
		return candidates;
	}
	public List<String> getPreInitJoinPointShadows(){
		List<PointcutDeclaration> pointuctList=ajsh.getPoincutList();
		List<Shadow> shadows=ajsh.getShadowList();
		List<String> candidates=new LinkedList<String>();
		for(PointcutDeclaration pd : pointuctList){         
			Pointcut pc=pd.pointcutDesignator.getPointcut();
			Argument[] pcarg= pd.arguments;
			
			MethodBinding mb= pd.binding; 
			String method= mb.toString();
			String declarationname=null;
			StringTokenizer st=new StringTokenizer(method,"$");
			String oldpart="";
			while(st.hasMoreElements()){
				
				String decl= st.nextToken();
				if(oldpart.equals("pointcut")){
					declarationname=decl;
					break;
				}
				oldpart=decl;
			}

		    if(declarationname.equals("analysispreinittest")){
		    	//get the join point, which is matched analysispccalltest
		    	//shadows
				for(Shadow sh: shadows){				
				  if(pc.match(sh)==FuzzyBoolean.YES || pc.match(sh)==FuzzyBoolean.MAYBE){
					String joinpoint=ShadowToString(sh);
					candidates.add(changeInitName(joinpoint));
				  }					
				}
		    }					
		}
		return candidates;
	}
	public List<String> getJoinPointShadows(String pointcut){
		//String reg="^.*\\:";
		//String pointcut=full_pointcut.replaceFirst(reg, "").trim();
		List<PointcutDeclaration> pointuctList=ajsh.getPoincutList();
		List<Shadow> shadows=ajsh.getShadowList();
		List<String> candidates=new LinkedList<String>();
		for(PointcutDeclaration pd : pointuctList){         
			Pointcut pc=pd.pointcutDesignator.getPointcut(); 
			Argument[] pcarg= pd.arguments;
			
			MethodBinding mb= pd.binding; 
			String method= mb.toString();
			String declarationname=null;
			StringTokenizer st=new StringTokenizer(method,"$");
			String oldpart="";
			while(st.hasMoreElements()){
				
				String decl= st.nextToken();
				if(oldpart.equals("pointcut")){
					declarationname=decl;
					break;
				}
				oldpart=decl;
			}
		    if(declarationname.equals(pointcut)){
		    	//get the join point, which is matched analysispccalltest
		    	//shadows
		    	
		    		for(Shadow sh: shadows){
						//String fuzzy=	pc.match(sh).toString();
						//System.out.println("Fuzzy : "+sh.getSignature().getName()+" "+fuzzy);
		    			try{
							if(sh!=null){
								if(pc.match(sh)==FuzzyBoolean.YES || pc.match(sh)==FuzzyBoolean.MAYBE){
									String joinpoint=ShadowToString(sh);									
									candidates.add(changeInitName(joinpoint));
								  }
							}
							
		    			}
				    	catch(Exception e){
				    		System.out.println(e.toString());
				    	}
		    		}  					
						
		    	
				
		    }					
		}
		return candidates;
	}
	private String ShadowToString(Shadow shadow){
		StringBuilder sb=new StringBuilder();
		String className=shadow.getSignature().getDeclaringType().getName();
		sb.append(className+".");
		String jpName=shadow.getSignature().getName();
		sb.append(jpName);
		
		UnresolvedType[] arglist= shadow.getSignature().getParameterTypes();
		if(arglist.length!=0){
		  //List<String> args=new LinkedList<String>();
		  StringBuilder argsb=new StringBuilder();
		  for(UnresolvedType arg:arglist){
			  argsb.append(arg.toString()+",");
		  }
		  argsb.deleteCharAt(argsb.length()-1);
		  sb.append("("+argsb.toString()+")");
		}
		else{
			sb.append("()");
		}
		return removeSchar(sb.toString());
	}
	private String changeInitName(String name){
		//change  the name of method <init> to new
	 String retName=name;
	 if(name.contains("<init>")){
		 retName=name.replaceFirst("<init>", "new");
	 }
	 return retName;
		
	}
	private String removeSchar(String input){
		String reg="\\$[^.]";
		return input.replaceFirst(reg, "");
	}
	
}
