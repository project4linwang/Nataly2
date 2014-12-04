package jaist.info.aspectj.nataly2.relationanalyzer;



import jaist.info.aspectj.nataly2.metamodel.Element;
import jaist.info.aspectj.nataly2.metamodel.Node;
import jaist.info.aspectj.nataly2.metamodel.RelationshipGraph;
import jaist.info.aspectj.nataly2.metamodel.RelationType;
import jaist.info.aspectj.nataly2.metamodel.Relationships;
import jaist.info.aspectj.nataly2.metamodel.RootNode;
import jaist.info.aspectj.nataly2.metamodel.SubNode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelationshipGraphGenerator {

	/**
	 * Generate Relationship graph,
	 * each graph is with respect to a given joinpoint
	 * @return is a set of RGs
	 */
	public List<RelationshipGraph> generateRG(List<Element> joinpointSets){
	
		List<RelationshipGraph> graphList=new LinkedList<RelationshipGraph>();
		
		for(Element jp: joinpointSets){
			if(jp!=null){
				RelationshipGraph tree=new RelationshipGraph();
				RootNode<Element> root=new RootNode<Element>(jp);
				tree.setRoot(root);
				RelationType[] types= RelationType.values();
				List<Node<Element>> children=new LinkedList<Node<Element>>();
				List<String> recordNameList=new LinkedList<String>();
	        	for(int k=0; k<types.length;k++)
	        	{
	        		if(jp.getRelationMaps().containsKey(types[k].name())){
		        		
		        		if(types[k].name().equals(RelationType.mcall.name()) || types[k].name().equals(RelationType.fset.name()) || types[k].name().equals(RelationType.mconcretize.name())|| types[k].name().equals(RelationType.declared.name())){
		        			//System.out.println("RelationShip: "+types[k].name());
		        			Relationships relation=jp.getRelationMaps().get(types[k].name());
			        		List<Element> contents=relation.getElements();
			        		
		        		    for(Iterator<Element> j=contents.iterator(); j.hasNext();){
		        		    	Element el=j.next();
		        		    	if(el!=null)
		        		    	{
		        		    		if(Filter(el)){
			            		    	SubNode<Element> new_node=new SubNode<Element>(el,root);
			            		    	new_node.setRelation(relation.getRelationType());
			            		    	new_node.setParent(root);
			            		    	checkName(new_node);
			            		    	if(new_node.getRelation().equals(RelationType.fset.name())){
			            		    		children.add(new_node);
			            		    		recordNameList.add(new_node.getData().getTraditionalFullName());
			            		    		generateSubGraph(new_node,recordNameList);
			            		    	}
			            		    	else if(new_node.getRelation().equals(RelationType.mcall.name())){
			            		    		checkName(new_node);
			            		    		
			            		    		//now we do not add the cycle invoke in our graph
			            		    		boolean flag=false;
			            		    		if(recordNameList.size()>0){
			            		    			for(String child_node: recordNameList){
				            		    			if(child_node.equals(new_node.getData().getTraditionalFullName())){
				            		    				flag=true;
				            		    				break;
				            		    			}
				            		    		}
			            		    		}
			            		    		children.add(new_node);
			            		    		if(!flag){
			            		    			recordNameList.add(new_node.getData().getTraditionalFullName());
			            		    			generateSubGraph(new_node,recordNameList);
			            		    		}
			            		    		
			            		    	}
			        		    	}
		        		    		
		        		    	}
		        		    }
		        		}
		        		
		        	
		        	}
	        	}
	        	root.setChildren(children);
	        	
	        	if(!isContainGraph(graphList,tree)){
	        			graphList.add(tree); 

	        		
	        	}
	        	
			}
			
        	
		}
		return graphList;
		
	}
	private boolean isContainGraph(List<RelationshipGraph> treelist,RelationshipGraph tree){
		for(RelationshipGraph tr:treelist){
			if(tr.getRoot().getData().getTraditionalFullName().equals(tree.getRoot().getData().getTraditionalFullName())){
				return true;
			}
		}
		return false;
	}
	private void checkName(Node<Element> new_node){
		//change  the name of method <init> to new
		if(new_node.getData().getName().equals("<init>")){
			new_node.getData().setName("new");
			String tmpfullname=new_node.getData().getTraditionalFullName();
			new_node.getData().setFullName(tmpfullname.replaceFirst("<init>", "new"));
		}
		//<clinit>
		if(new_node.getData().getName().equals("<clinit>")){
			new_node.getData().setName("cnew");
			String tmpfullname=new_node.getData().getTraditionalFullName();
			new_node.getData().setFullName(tmpfullname.replaceFirst("<clinit>", "cnew"));
		}
		//change some self-invoked method names to the original name
		if(new_node.getData().getTraditionalFullName().contains("$")){
			
			String regstr="\\$\\w*[^\\.]";
			String tmpname=new_node.getData().getTraditionalFullName();
			Pattern pattern=Pattern.compile(regstr);
			Matcher matcher=pattern.matcher(tmpname);
			if(matcher.find()){
				String subname=matcher.group();
				String reg="\\$\\d+";
				Pattern subpattern=Pattern.compile(reg);
				Matcher submatcher=subpattern.matcher(subname);
				if(submatcher.find()){
					String name=tmpname.replaceFirst(reg, "");
					new_node.getData().setFullName(name);
				}
				else{
					String name=tmpname.replaceFirst("\\$", "-");
					new_node.getData().setFullName(name);
				}
			}
			
		}
	}
	/*
	 * Generate subgraph
	 */
	private void generateSubGraph(SubNode<Element> parent,List<String> recordchild){
		if(parent.getData().getRelationMaps()!=null){
			RelationType[] types= RelationType.values();
			RootNode<Element> root=parent.getRoot();
			List<Node<Element>> children=new LinkedList<Node<Element>>();

			for(int k=0; k<types.length;k++)
	    	{
	    		if(parent.getData().getRelationMaps().containsKey(types[k].name())){
	        		if(types[k].name().equals(RelationType.mcall.name()) || types[k].name().equals(RelationType.fset.name()) || types[k].name().equals(RelationType.mconcretize.name()) || types[k].name().equals(RelationType.declared.name())){
	        			Relationships relation=parent.getData().getRelationMaps().get(types[k].name());
		        		List<Element> contents=relation.getElements();
		        		
	        		    for(Iterator<Element> j=contents.iterator(); j.hasNext();){
	        		    	Element el=j.next();
	        		    	if(el!=null)
	        		    	{
	        		    		if(Filter(el)){
		            		    	SubNode<Element> new_node=new SubNode<Element>(el,root);
		            		    	new_node.setRelation(relation.getRelationType());
		            		    	new_node.setParent(parent);
		            		    	checkName(new_node);
		            		    	if(new_node.getRelation().equals(RelationType.fset.name())){
		            		    		children.add(new_node);
		            		    		recordchild.add(new_node.getData().getTraditionalFullName());
		            		    		
		            		    		generateSubGraph(new_node,recordchild);
		            		    	}
		            		    	else if(new_node.getRelation().equals(RelationType.mcall.name())){
		            		    		checkName(new_node);
		            		    		boolean flag=false;
		            		    		for(String child_node: recordchild){
		            		    			if(child_node.equals(new_node.getData().getTraditionalFullName())){
		            		    				flag=true;
		            		    				break;
		            		    			}
		            		    		}
		            		    		children.add(new_node);
		            		    		
		            		    		if(!flag){
		            		    			recordchild.add(new_node.getData().getTraditionalFullName());
		            		    			generateSubGraph(new_node,recordchild);
		            		    		}
		            		    	}
		            		    	else if(new_node.getRelation().equals(RelationType.mconcretize.name())){
		            		    		checkName(new_node);
		            		    		children.add(new_node);
		            		    		recordchild.add(new_node.getData().getTraditionalFullName());
		            		    		generateSubGraph(new_node,recordchild);
		            		    	}
		            		    
		        		    	}
	        		    		
	        		    	}
	        		    }
	        		    
	        		}
	        		}
	    		}
			parent.setChildren(children);
			
	    	}
		}
		
	/*
	 * Filter the useless element,
	 * including java APIs, 
	 */
	private boolean Filter(Element elem){
		String fullname=elem.getFullName();
		if(fullname!=null)
		{
		String[] names=fullname.split("[.]");
		if(names!=null){
			if(names.length>=1){
			  if(names[0].equals("java")){
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
}
