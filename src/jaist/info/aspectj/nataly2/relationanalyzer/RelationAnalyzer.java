package jaist.info.aspectj.nataly2.relationanalyzer;




import jaist.info.aspectj.nataly2.metamodel.Element;
import jaist.info.aspectj.nataly2.metamodel.RelationElements;
import jaist.info.aspectj.nataly2.metamodel.RelationType;
import jaist.info.aspectj.nataly2.metamodel.Relationships;
import jaist.info.aspectj.nataly2.metamodel.SElement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import model.FieldElement;

import model.ClassElement;
import model.MethodElement;

import model.ICategories;

import model.Relation;

import model.IElement;



import jayfx.JayFX;
import jayfx.JayFXException;




/**
 * Facade of Relationship analysis. 
 * @author suse-wl
 *
 */
public class RelationAnalyzer {

	private IStructuredSelection aSelection;
	RelationElements relaelem=RelationElements.getRelationElements();
	//
	/**
	 *
	 * Analyze the source code and generate the relationship maps,
	 * 
	 */
	public List<Element> AnalyzeSourceCode(IProgressMonitor lMonitor){
		List<Element> elementlist=new LinkedList<Element>();
		try
		{		
			IProject project=getSelectedProject();
			IPath path=project.getLocation();
			String filepath=path+"/nataly_files/";
			relaelem.setFilePath(filepath);
			//use JayFx tools
    		JayFX lDB = new JayFX();
    		lDB.initialize( project, lMonitor, true );
    		Set<IElement> lAllElements = lDB.getAllElements();
    		IElement[] lElements = (IElement[]) lAllElements.toArray( new IElement[lAllElements.size()]);
    		Map<String,Element> elements=RecElementList(lElements);
    		for( int i = 0; i < lElements.length; i++ )
    		{ 
    			
    			Element element=elements.get(changeInitName(lElements[i].toString()));
    		    Map<String,Relationships> relations=new HashMap<String, Relationships>();
    		    Set<IElement> lTargets_fset = lDB.getRange(lElements[i], Relation.FSETS);
    		    if(lTargets_fset.size()>0){
    		    	if(element.getCategory().equals(ICategories.METHOD.name())){
    					Relationships relationship=new Relationships();
        				List<Element> targetElements=new LinkedList<Element>();
        				relationship.setRelationType(RelationType.fset.name());
    					
        				for( Iterator j = lTargets_fset.iterator(); j.hasNext(); )
        				{    					
        					
        					String tarName=changeInitName(j.next().toString());
        					targetElements.add(elements.get(tarName));
        				
        				}
        		        relationship.setElements(targetElements);

        				relations.put(relationship.getRelationType(), relationship);
    				}
    		    	if(element.getCategory().equals(ICategories.FIELD.name())){
    					Relationships relationship=new Relationships();
    					List<Element> targetElements=new LinkedList<Element>();
        				relationship.setRelationType(RelationType.fset.name());
    					
        				for( Iterator j = lTargets_fset.iterator(); j.hasNext(); )
        				{    					
        					
        					String tarName=changeInitName(j.next().toString());
        					targetElements.add(elements.get(tarName));
        				
        				}
        				relationship.setElements(targetElements);
        				relations.put(relationship.getRelationType(), relationship);
    				}
    		    }
    		    //"FGETS"-fget
    		    Set<IElement> lTargets_fget = lDB.getRange(lElements[i], Relation.FGETS);
    		    if(lTargets_fget.size()>0){
    		    	if(element.getCategory().equals(ICategories.METHOD.name())){
    					Relationships relationship=new Relationships();
        				List<Element> targetElements=new LinkedList<Element>();
        				relationship.setRelationType(RelationType.fget.name());
    					
        				for( Iterator j = lTargets_fget.iterator(); j.hasNext(); )
        				{    					
        					
        					String tarName=changeInitName(j.next().toString());
        					targetElements.add(elements.get(tarName));
        				
        				}
        		        relationship.setElements(targetElements);
        				relations.put(relationship.getRelationType(), relationship);
    				}
    		    	if(element.getCategory().equals(ICategories.FIELD.name())){
    					Relationships relationship=new Relationships();
    					List<Element> targetElements=new LinkedList<Element>();
        				relationship.setRelationType(RelationType.fget.name());
    					
        				for( Iterator j = lTargets_fget.iterator(); j.hasNext(); )
        				{    					
        					
        					String tarName=changeInitName(j.next().toString());
        					targetElements.add(elements.get(tarName));
        				
        				}
        				relationship.setElements(targetElements);
        				relations.put(relationship.getRelationType(), relationship);
    				}
    		    }   			   		    
    			//"Calls"
    			Set<IElement> lTargets2 = lDB.getRange( lElements[i], Relation.CALLS );
    			if( lTargets2.size() > 0 )
    			{   				
    			    
    				Relationships relationship=new Relationships();
    				List<Element> targetElements=new LinkedList<Element>();
    				relationship.setRelationType(RelationType.mcall.name());
    				for( Iterator j = lTargets2.iterator(); j.hasNext(); )
    				{
    					String tarName=changeInitName(j.next().toString());   					
    					targetElements.add(elements.get(tarName));		
    				}
    				relationship.setElements(targetElements);
    				relations.put(relationship.getRelationType(), relationship);
    			}
    		
    			//"IMPLEMENTS_INTERFACE" and Extend Class
    			Set<IElement> lTargets7 = lDB.getRange( lElements[i], Relation.IMPLEMENTS_INTERFACE );
    			Set<IElement> lTargets_extend=lDB.getRange(lElements[i], Relation.EXTENDS_CLASS);
    			if( lTargets7.size() > 0 || lTargets_extend.size()>0)
    			{
    				Relationships relationship=new Relationships();
    				List<Element> targetElements=new LinkedList<Element>();
    				relationship.setRelationType(RelationType.tconcretize.name());
    				if(lTargets7.size() > 0){
    				for( Iterator j = lTargets7.iterator(); j.hasNext(); )
    				{
    					String tarName=changeInitName(j.next().toString());
    					targetElements.add(elements.get(tarName));
    				    			
    				}
    				}
    				if(lTargets_extend.size()>0){
    				//
    				for( Iterator j = lTargets_extend.iterator(); j.hasNext(); )
    				{
    					String tarName=changeInitName(j.next().toString());
    					targetElements.add(elements.get(tarName));
    				    
    				}
    				}
    				relationship.setElements(targetElements);
    				
    				relations.put(relationship.getRelationType(), relationship);
    			}
    	        
    			//"OVERRIDES"
    			Set<IElement> lTargets9 = lDB.getRange( lElements[i], Relation.OVERRIDES);
    			if( lTargets9.size() > 0 )
    			{
    				Relationships relationship=new Relationships();
    				List<Element> targetElements=new LinkedList<Element>();
    				relationship.setRelationType(RelationType.mconcretize.name());
    				for( Iterator j = lTargets9.iterator(); j.hasNext(); )
    				{
    					String tarName=changeInitName(j.next().toString());
    					targetElements.add(elements.get(tarName));
    				    
    				}
    				relationship.setElements(targetElements);
    				relations.put(relationship.getRelationType(), relationship);
    			}
    			//fdecl/mdecl
    			Set<IElement> lTargets10 = lDB.getRange( lElements[i], Relation.DECLARES);
    			if( lTargets10.size() > 0 )
    			{
    				Relationships relationship=new Relationships();
    				List<Element> targetElements=new LinkedList<Element>();
    				relationship.setRelationType(RelationType.declare.name());
    				for( Iterator j = lTargets10.iterator(); j.hasNext(); )
    				{
    					String tarName=changeInitName(j.next().toString());
    					targetElements.add(elements.get(tarName));
    				    
    				}
    				relationship.setElements(targetElements);
    				
    				relations.put(relationship.getRelationType(), relationship);
    			}
    			//declared
    			Set<IElement> lTargets_decled = lDB.getRange( lElements[i], Relation.T_DECLARES);
    			if( lTargets_decled.size() > 0 )
    			{
    				Relationships relationship=new Relationships();
    				List<Element> targetElements=new LinkedList<Element>();
    				relationship.setRelationType(RelationType.declared.name());
    				for( Iterator j = lTargets_decled.iterator(); j.hasNext(); )
    				{
    					String tarName=changeInitName(j.next().toString());
    					targetElements.add(elements.get(tarName));
    				    
    				}
    				relationship.setElements(targetElements);
    				
    				relations.put(relationship.getRelationType(), relationship);
    			}
    			//mcallee
    			Set<IElement> lTargets_called = lDB.getRange( lElements[i], Relation.T_CALLS);
    			if( lTargets_called.size() > 0 )
    			{
    				Relationships relationship=new Relationships();
    				List<Element> targetElements=new LinkedList<Element>();
    				relationship.setRelationType(RelationType.mcallee.name());
    				for( Iterator j = lTargets_called.iterator(); j.hasNext(); )
    				{
    					String tarName=changeInitName(j.next().toString());
    					targetElements.add(elements.get(tarName));
    				    
    				}
    				relationship.setElements(targetElements);
    				relations.put(relationship.getRelationType(), relationship);
    			}
    			//fget
    			Set<IElement> lTargets_get = lDB.getRange( lElements[i], Relation.T_ACCESSES);
    			if( lTargets_get.size() > 0 )
    			{
    				if(element.getCategory().equals(ICategories.METHOD.name())){
    					Relationships relationship=new Relationships();
    					List<Element> targetElements=new LinkedList<Element>();
        				relationship.setRelationType(RelationType.maccess.name());
        				for( Iterator j = lTargets_get.iterator(); j.hasNext(); )
        				{
        					String tarName=changeInitName(j.next().toString());
        					targetElements.add(elements.get(tarName));
        				    
        				}
        				relationship.setElements(targetElements);
        				
        				relations.put(relationship.getRelationType(), relationship);
    				}
    				else if(element.getCategory().equals(ICategories.FIELD.name())){
    					Relationships relationship=new Relationships();
    					List<Element> targetElements=new LinkedList<Element>();
        				relationship.setRelationType(RelationType.maccess.name());
        				for( Iterator j = lTargets_get.iterator(); j.hasNext(); )
        				{
        					String tarName=changeInitName(j.next().toString());
        					targetElements.add(elements.get(tarName));
        				    
        				}
        				relationship.setElements(targetElements);
        			
        				relations.put(relationship.getRelationType(), relationship);
    				}
    				
    			}

    			element.setRelationMaps(relations);
    			elementlist.add(element);
    		}
 
    		List<SElement> simpleElemList= SetSimpleElementList(lElements,lDB);
    		relaelem.setSelementlist(simpleElemList);
    		return elementlist;
		}
    	catch( JayFXException lException )
		{
    		lException.printStackTrace();
		}
    	catch( AssertionError lError )
		{
    		lError.printStackTrace();
		}
    	return null;
	}
	public void selectionChanged(IAction action, ISelection selection) {
        if( selection instanceof IStructuredSelection )
            aSelection = (IStructuredSelection)selection;

    }
    /*
     * Get the name of the file, to which save the relation maps.
     */
	public static String getRelativeXMLFullFileName(String advpath){
		StringBuilder fileNameBuilder = new StringBuilder(advpath);
		fileNameBuilder.append("#" + "sourcecode");
		fileNameBuilder.append("-relationmaps.xml");
		return fileNameBuilder.toString();
	}
	 private IProject getSelectedProject()
		{
			IProject lReturn = null;
			Iterator i = aSelection.iterator();
			if( i.hasNext() )
			{
				Object lNext = i.next();
				if( lNext instanceof IResource )
				{
					lReturn = ((IResource)lNext).getProject();
				}
				else if( lNext instanceof IJavaElement )
				{
					IJavaProject lProject = ((IJavaElement)lNext).getJavaProject();
					lReturn = lProject.getProject();
				}
			}
			return lReturn;
		}
	 /*
	  * Analyzer the parameters of the method, change its format to a traditional format.
	  */
	 private String analyzeParametrs(String signatureType){
			String arg=null;
			if(signatureType.length()==1){
				String firstStr=signatureType.substring(0, 1);
				if(firstStr.equals("Z")){
					arg="boolean";
				}
				else if(firstStr.equals("B")){
					arg="byte";
				}
				else if(firstStr.equals("C")){
					arg="char";
				}
				else if(firstStr.equals("S")){
					arg="short";
				}
				else if(firstStr.equals("I")){
					arg="int";
				}
				else if(firstStr.equals("J")){
					arg="long";
				}
				else if(firstStr.equals("F")){
					arg="float";
				}
				else if(firstStr.equals("D")){
					arg="double";
				}
			}
			else if(signatureType.length()>0){
				if(signatureType.startsWith("L") && signatureType.endsWith(";")){
					arg=signatureType.substring(1, signatureType.length()-1);
				}
				else if(signatureType.startsWith("[")){
					String tmparg=signatureType.substring(1,signatureType.length());
					arg = analyzeParametrs(tmparg)+"[]";
				}
			}
			return arg;
				
		}
	 /*
	  * Generate the List of elements from the source code
	  */
	 private Map<String,Element> RecElementList(IElement[] elements){
		 Map<String, Element> elementlist=new HashMap<String,Element>();
		 for(int i=0;i<elements.length;i++){
			 Element elem=RecElement( elements[i]);
			 elementlist.put(elem.getFullName(), elem);
		 }
		 return elementlist;
	 }
	 private Element RecElement(IElement ielt){
			Element element=new Element();
			if(ielt.getCategory().compareTo(ICategories.CLASS)==0){
				ClassElement ce=(ClassElement)ielt;
			    element.setCategory(ce.getCategory().name());
			    element.setFullName(ce.toString());
			    element.setName(ce.getShortName());
			}
			else if(ielt.getCategory().compareTo(ICategories.METHOD)==0){
				MethodElement me=(MethodElement)ielt;
			    String fullName=ielt.toString();	
			    String category=me.getCategory().name();
			    String rtype=me.getReturnType();
			    String paralist=me.getParameters();
			    String tmpparalist=paralist.substring(1,paralist.length()-1);
			    String[] args = null;
			    if(tmpparalist!=""){
				   args=tmpparalist.split("[,]");
			    }
			
			   //String[] args= Signature.getParameterTypes(fullName); 
			   //<- we can not use this method to get the parameters,because the "fullname" is not correct Signature.
			   List<String> arglists=new LinkedList<String>();
			   if(args!=null){
				  for(String arg : args){
					 String para=analyzeParametrs(arg);
					 arglists.add(para);
				  }
			    }
			
			   String methodName= me.getName();
			   element.setCategory(category);
			   element.setFullName(changeInitName(fullName));
			   element.setName(changeInitName(methodName));
			   element.setReturnType(rtype);
			   element.setArglists(arglists);
			}
			else if(ielt.getCategory().compareTo(ICategories.FIELD)==0){
				FieldElement fe=(FieldElement)ielt;
			    element.setCategory(fe.getCategory().name());
			    element.setFullName(fe.toString());
			    element.setName(fe.getSimpleName());
			}
			
			return element;
		}
	 /*
	  * Generate a simple elements list, it is used to match the pointcut signature pattern.
	  */
	 private List<SElement> SetSimpleElementList(IElement[] elements,JayFX ldb){
		 List<SElement> elemlist=new LinkedList<SElement>();
		 
			 for(int i=0;i<elements.length;i++){
				 
				 IElement elem=elements[i];
				 SElement selem=new SElement();
				 if(elem.getCategory().compareTo(ICategories.CLASS)==0){
					 ClassElement c_elem=(ClassElement)elem;
					 selem.setCategory(c_elem.getCategory().name());
					 selem.setFullName(c_elem.toString());
					 selem.setSimpleName(c_elem.getShortName());
					 selem.setPackagename(c_elem.getPackageName());
					 List<String> targetElements=new LinkedList<String>();
					 try{
					 Set<IElement> lTargets_textend = ldb.getRange( c_elem, Relation.T_EXTENDS_CLASS );
		    			Set<IElement> lTargets_tiextend=ldb.getRange(c_elem, Relation.T_EXTENDS_INTERFACES);
		    			Set<IElement> lTargets_timpl=ldb.getRange(c_elem,Relation.T_IMPLEMENTS_INTERFACE);
		    			if( lTargets_textend.size() > 0 || lTargets_tiextend.size()>0 ||lTargets_timpl.size()>0)
		    			{

		    				if(lTargets_textend.size() > 0){
		    				for( Iterator j = lTargets_textend.iterator(); j.hasNext(); )
		    				{
		    					String tarName=changeInitName(j.next().toString());
		    					if(tarName.contains(".")){
									String reg="\\.(\\w*$)";
									Pattern pattern2=Pattern.compile(reg);
									Matcher matcher2=pattern2.matcher(tarName);
									if(matcher2.find()){
										tarName=matcher2.group(1);
									}
								}
		    					targetElements.add(tarName);
		    				    			
		    				}
		    				}
		    				if(lTargets_tiextend.size()>0){
		    				//
		    				for( Iterator j = lTargets_tiextend.iterator(); j.hasNext(); )
		    				{
		    					String tarName=changeInitName(j.next().toString());
		    					if(tarName.contains(".")){
									String reg="\\.(\\w*$)";
									Pattern pattern2=Pattern.compile(reg);
									Matcher matcher2=pattern2.matcher(tarName);
									if(matcher2.find()){
										tarName=matcher2.group(1);
									}
								}
		    					targetElements.add(tarName);
		    				    
		    				}
		    				}
		    				if(lTargets_timpl.size()>0){
		    					for(Iterator j=lTargets_timpl.iterator();j.hasNext();){
		    						String tarName=changeInitName(j.next().toString());
			    					if(tarName.contains(".")){
										String reg="\\.(\\w*$)";
										Pattern pattern2=Pattern.compile(reg);
										Matcher matcher2=pattern2.matcher(tarName);
										if(matcher2.find()){
											tarName=matcher2.group(1);
										}
									}
		    						targetElements.add(tarName);
		    					}
		    				}
			
		    			}
		    			
					 }
					 catch(Exception e){
							System.out.println("Error: "+e.getMessage());
						}
					 selem.setSubClass(targetElements);
		    			//
		    			List<String> targetElements2=new LinkedList<String>();
		    			try{
						 Set<IElement> lTargets_extend = ldb.getRange( c_elem, Relation.EXTENDS_CLASS );
			    			Set<IElement> lTargets_iextend=ldb.getRange(c_elem, Relation.EXTENDS_INTERFACES);
			    			Set<IElement> lTargets_impl=ldb.getRange(c_elem,Relation.IMPLEMENTS_INTERFACE);
			    			if( lTargets_extend.size() > 0 || lTargets_iextend.size()>0 ||lTargets_impl.size()>0)
			    			{

			    				if(lTargets_extend.size() > 0){
			    				for( Iterator j = lTargets_extend.iterator(); j.hasNext(); )
			    				{
			    					String tarName=changeInitName(j.next().toString());
			    					if(tarName.contains(".")){
										String reg="\\.(\\w*$)";
										Pattern pattern2=Pattern.compile(reg);
										Matcher matcher2=pattern2.matcher(tarName);
										if(matcher2.find()){
											tarName=matcher2.group(1);
										}
									}
			    					targetElements2.add(tarName);
			    				    			
			    				}
			    				}
			    				if(lTargets_iextend.size()>0){
			    				//
			    				for( Iterator j = lTargets_iextend.iterator(); j.hasNext(); )
			    				{
			    					String tarName=changeInitName(j.next().toString());
			    					if(tarName.contains(".")){
										String reg="\\.(\\w*$)";
										Pattern pattern2=Pattern.compile(reg);
										Matcher matcher2=pattern2.matcher(tarName);
										if(matcher2.find()){
											tarName=matcher2.group(1);
										}
									}
			    					targetElements2.add(tarName);
			    				    
			    				}
			    				}
			    				if(lTargets_impl.size()>0){
			    					for(Iterator j=lTargets_impl.iterator();j.hasNext();){
			    						String tarName=changeInitName(j.next().toString());
				    					if(tarName.contains(".")){
											String reg="\\.(\\w*$)";
											Pattern pattern2=Pattern.compile(reg);
											Matcher matcher2=pattern2.matcher(tarName);
											if(matcher2.find()){
												tarName=matcher2.group(1);
											}
										}
			    						targetElements2.add(tarName);
			    					}
			    				}
				
			    			}
			    			
		    			}
		    			catch(Exception e){
		    				System.out.println("Error: "+e.getMessage());
		    			}
		    			selem.setParentClass(targetElements2);
		    			//
		    			//if(isContainSelement(elemlist,selem)){
				        	 elemlist.add(selem);
				         //}
				 }
				 else if(elem.getCategory().compareTo(ICategories.METHOD)==0){
					 MethodElement m_elem=(MethodElement)elem;
					 selem.setCategory(m_elem.getCategory().name());
					 selem.setFullName(changeInitName(m_elem.toString()));
					 selem.setSimpleName(changeInitName(m_elem.getName()));
					 selem.setPackagename(m_elem.getPackageName());
					 selem.setDeclaredName(m_elem.getDeclaringClass().getShortName());
					 //find class property
					 ClassElement c_elem=m_elem.getDeclaringClass();
					 if(Filter(c_elem.toString())){
						 List<String> targetElements=new LinkedList<String>();
						 try{
						 Set<IElement> lTargets_textend = ldb.getRange( c_elem, Relation.T_EXTENDS_CLASS );
			    			Set<IElement> lTargets_tiextend=ldb.getRange(c_elem, Relation.T_EXTENDS_INTERFACES);
			    			Set<IElement> lTargets_timpl=ldb.getRange(c_elem,Relation.T_IMPLEMENTS_INTERFACE);
			    			if( lTargets_textend.size() > 0 || lTargets_tiextend.size()>0 ||lTargets_timpl.size()>0)
			    			{

			    				if(lTargets_textend.size() > 0){
			    				for( Iterator j = lTargets_textend.iterator(); j.hasNext(); )
			    				{
			    					String tarName=changeInitName(j.next().toString());
			    					if(tarName.contains(".")){
										String reg="\\.(\\w*$)";
										Pattern pattern2=Pattern.compile(reg);
										Matcher matcher2=pattern2.matcher(tarName);
										if(matcher2.find()){
											tarName=matcher2.group(1);
										}
									}
			    					targetElements.add(tarName);
			    				    			
			    				}
			    				}
			    				if(lTargets_tiextend.size()>0){
			    				//
			    				for( Iterator j = lTargets_tiextend.iterator(); j.hasNext(); )
			    				{
			    					String tarName=changeInitName(j.next().toString());
			    					if(tarName.contains(".")){
										String reg="\\.(\\w*$)";
										Pattern pattern2=Pattern.compile(reg);
										Matcher matcher2=pattern2.matcher(tarName);
										if(matcher2.find()){
											tarName=matcher2.group(1);
										}
									}
			    					targetElements.add(tarName);
			    				    
			    				}
			    				}
			    				if(lTargets_timpl.size()>0){
			    					for(Iterator j=lTargets_timpl.iterator();j.hasNext();){
			    						String tarName=changeInitName(j.next().toString());
			    						if(tarName.contains(".")){
											String reg="\\.(\\w*$)";
											Pattern pattern2=Pattern.compile(reg);
											Matcher matcher2=pattern2.matcher(tarName);
											if(matcher2.find()){
												tarName=matcher2.group(1);
											}
										}
			    						targetElements.add(tarName);
			    					}
			    				}
				
			    			}
						 }
						 catch(Exception e){
			    				System.out.println("Error: "+e.getMessage());
			    			}
			    			selem.setSubClass(targetElements);
			    			//
			    			List<String> targetElements2=new LinkedList<String>();
			    			try{
							 Set<IElement> lTargets_extend = ldb.getRange( c_elem, Relation.EXTENDS_CLASS );
				    			Set<IElement> lTargets_iextend=ldb.getRange(c_elem, Relation.EXTENDS_INTERFACES);
				    			Set<IElement> lTargets_impl=ldb.getRange(c_elem,Relation.IMPLEMENTS_INTERFACE);
				    			if( lTargets_extend.size() > 0 || lTargets_iextend.size()>0 ||lTargets_impl.size()>0)
				    			{

				    				if(lTargets_extend.size() > 0){
				    				for( Iterator j = lTargets_extend.iterator(); j.hasNext(); )
				    				{
				    					String tarName=changeInitName(j.next().toString());
				    					if(tarName.contains(".")){
											String reg="\\.(\\w*$)";
											Pattern pattern2=Pattern.compile(reg);
											Matcher matcher2=pattern2.matcher(tarName);
											if(matcher2.find()){
												tarName=matcher2.group(1);
											}
										}
				    					targetElements2.add(tarName);
				    				    			
				    				}
				    				}
				    				if(lTargets_iextend.size()>0){
				    				//
				    				for( Iterator j = lTargets_iextend.iterator(); j.hasNext(); )
				    				{
				    					String tarName=changeInitName(j.next().toString());
				    					if(tarName.contains(".")){
											String reg="\\.(\\w*$)";
											Pattern pattern2=Pattern.compile(reg);
											Matcher matcher2=pattern2.matcher(tarName);
											if(matcher2.find()){
												tarName=matcher2.group(1);
											}
										}
				    					targetElements2.add(tarName);
				    				    
				    				}
				    				}
				    				if(lTargets_impl.size()>0){
				    					for(Iterator j=lTargets_impl.iterator();j.hasNext();){
				    						String tarName=changeInitName(j.next().toString());
				    						if(tarName.contains(".")){
												String reg="\\.(\\w*$)";
												Pattern pattern2=Pattern.compile(reg);
												Matcher matcher2=pattern2.matcher(tarName);
												if(matcher2.find()){
													tarName=matcher2.group(1);
												}
											}
				    						targetElements2.add(tarName);
				    					}
				    				}
					
				    			}
			    			}
			    			catch(Exception e){
			    				System.out.println("Error: "+e.getMessage());
			    			}
				    			selem.setParentClass(targetElements2);
					 }
					 
					 //
					 String paralist=m_elem.getParameters();
					    String tmpparalist=paralist.substring(1,paralist.length()-1);
					    String[] args = null;
					    if(!tmpparalist.equals("")){
						   args=tmpparalist.split(",");
					    }
					
					   //String[] args= Signature.getParameterTypes(fullName); 
					   //<- we can not use this method to get the parameters,because the "fullname" is not correct Signature.
					   List<String> arglists=new LinkedList<String>();
					   if(args!=null){
						   if(args.length>0){
							   for(String arg : args){
									 String para=analyzeParametrs(arg);
									 arglists.add(para);
								  }
						   }
						 
					    }
					 selem.setArglists(arglists);
					 selem.setReturnType(m_elem.getReturnType());
					 //if(isContainSelement(elemlist,selem)){
			        	 elemlist.add(selem);
			         //}
					 
				 }
				 else if(elem.getCategory().compareTo(ICategories.FIELD)==0){
					 FieldElement f_elem=(FieldElement)elem;
					 selem.setCategory(ICategories.FIELD.name());
					 selem.setFullName(f_elem.toString());
					 selem.setSimpleName(f_elem.getSimpleName()); 
					 selem.setPackagename(f_elem.getPackageName());
					 selem.setDeclaredName(f_elem.getDeclaringClass().getShortName());
					 selem.setType(f_elem.getType());
					 
					 //
					 List<String> targetElements2=new LinkedList<String>();
					 try{
						 Set<IElement> lTargets_extend = ldb.getRange( f_elem.getDeclaringClass(), Relation.EXTENDS_CLASS );
			    			Set<IElement> lTargets_iextend=ldb.getRange(f_elem.getDeclaringClass(), Relation.EXTENDS_INTERFACES);
			    			Set<IElement> lTargets_impl=ldb.getRange(f_elem.getDeclaringClass(),Relation.IMPLEMENTS_INTERFACE);
			    			if( lTargets_extend.size() > 0 || lTargets_iextend.size()>0 ||lTargets_impl.size()>0)
			    			{

			    				if(lTargets_extend.size() > 0){
			    				for( Iterator j = lTargets_extend.iterator(); j.hasNext(); )
			    				{
			    					String tarName=changeInitName(j.next().toString());
			    					if(tarName.contains(".")){
										String reg="\\.(\\w*$)";
										Pattern pattern2=Pattern.compile(reg);
										Matcher matcher2=pattern2.matcher(tarName);
										if(matcher2.find()){
											tarName=matcher2.group(1);
										}
									}
			    					targetElements2.add(tarName);
			    				    			
			    				}
			    				}
			    				if(lTargets_iextend.size()>0){
			    				//
			    				for( Iterator j = lTargets_iextend.iterator(); j.hasNext(); )
			    				{
			    					String tarName=changeInitName(j.next().toString());
			    					if(tarName.contains(".")){
										String reg="\\.(\\w*$)";
										Pattern pattern2=Pattern.compile(reg);
										Matcher matcher2=pattern2.matcher(tarName);
										if(matcher2.find()){
											tarName=matcher2.group(1);
										}
									}
			    					targetElements2.add(tarName);
			    				    
			    				}
			    				}
			    				if(lTargets_impl.size()>0){
			    					for(Iterator j=lTargets_impl.iterator();j.hasNext();){
			    						String tarName=changeInitName(j.next().toString());
			    						if(tarName.contains(".")){
											String reg="\\.(\\w*$)";
											Pattern pattern2=Pattern.compile(reg);
											Matcher matcher2=pattern2.matcher(tarName);
											if(matcher2.find()){
												tarName=matcher2.group(1);
											}
										}
			    						targetElements2.add(tarName);
			    					}
			    				}
				
			    			}
		    			}
		    			catch(Exception e){
		    				System.out.println("Error: "+e.getMessage());
		    			}
			    		selem.setParentClass(targetElements2);
					 
			        	 elemlist.add(selem);

					 
				 }
				 
			 
			
		 }
		 
		 
		 return elemlist;
	 }

	 private boolean Filter(String elem){
			String fullname=elem;
			if(fullname!=null)
			{
			String[] names=fullname.split("[.]");
			if(names!=null){
				if(names.length>=1){
				  if(names[0].equals("java")||names[0].equals("javax")){
					return false;
				  }
				}
			}
			
			return true;
			}
			else{
				return false;
			}

		}
	 private String changeInitName(String name){
			//change  the name of method <init> to new
		 String retName=name;
		 if(name.contains("<init>")){
			 retName=name.replaceFirst("<init>", "new");
		 }
		 return retName;
			
		}
	 
}
