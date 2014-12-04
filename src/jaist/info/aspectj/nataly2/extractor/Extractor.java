package jaist.info.aspectj.nataly2.extractor;



import jaist.info.aspectj.nataly2.metamodel.AbstractPatternRelation;
import jaist.info.aspectj.nataly2.metamodel.AbstractRelationGraph;
import jaist.info.aspectj.nataly2.metamodel.Element;
import jaist.info.aspectj.nataly2.metamodel.Node;
import jaist.info.aspectj.nataly2.metamodel.PKHTPattern;
import jaist.info.aspectj.nataly2.metamodel.PatternNode;
import jaist.info.aspectj.nataly2.metamodel.PatternRelation;
import jaist.info.aspectj.nataly2.metamodel.RelationshipGraph;
import jaist.info.aspectj.nataly2.metamodel.RelationType;
import jaist.info.aspectj.nataly2.metamodel.SubNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class which is used to extract TPattern from the RG.
 */
public class Extractor {

	private boolean isParentHasKHead(SubNode<Element> kp){
		boolean isHas=false;	
		if(kp.getM_depth()>1){
			SubNode<Element> skp=(SubNode<Element>)kp.getParent();
			if(skp!=null){
				if(skp.getIsKeyPoint()){
					return true;
				}
				else{
					isHas=isParentHasKHead(skp);
				}
			}
		}
			
		return isHas;
	}
	private List<Node<Element>> refineKeyHeadSet(List<Node<Element>> keypoints){
		List<Node<Element>> kps=new LinkedList<Node<Element>>();
		
		if(keypoints!=null){
			if(keypoints.size()>0){
				
				for(Node<Element> kp:keypoints){
					
					if(!isParentHasKHead((SubNode<Element>)kp)){
						kps.add(kp);
					}
				}
			}
		}
		return kps;
	}
	private List<Node<Element>> detectKeyHead(RelationshipGraph rt,List<RelationshipGraph> cadidates){
		List<Node<Element>> keypoints=new LinkedList<Node<Element>>();
	    Queue<Node<Element>> matchedTree=rt.breadthSearch();
	    matchedTree.remove(rt.getRoot());
	    if(cadidates.size()>0){
	    	if(matchedTree!=null){
		    	
		    	for(Node<Element> nd: matchedTree){		    		
			    	if(nd.getRelation().equals(RelationType.fset.name()) || nd.getRelation().equals(RelationType.mcall.name()) ||nd.getRelation().equals(RelationType.mconcretize.name()))
			    	{
			    	boolean flag=true;
			    	if(cadidates!=null){
			    		for(RelationshipGraph tree: cadidates){
				    		Queue<Node<Element>> tnd=tree.breadthSearch();
				    		flag=false;
				    		for(Node<Element> node:tnd){
				    			flag=false;
				    			if(node.getRelation()!=null && nd.getRelation()!=null){
				    				if(node.getData().getTraditionalFullName().equals(nd.getData().getTraditionalFullName()) && node.getRelation().equals(nd.getRelation())){
								    		flag=true;
								    		break;					    				
					    			}
				    			}	
				    		
				    		}
				    		if(flag==false){
				    			break;
				    		}
				 
				    	}
			    	}
			    	
			    	if(flag){
			    		if(keypoints.size()>0){
    						for(Node<Element> kp:keypoints){
    							if(!(kp.getRelation().equals(nd.getRelation()) && kp.getData().getTraditionalFullName().equals(nd.getData().getTraditionalFullName()))){
    								keypoints.add(nd);
				    				break;
    							}
    						}
    					}
			    		else{
			    			keypoints.add(nd);
			    		}
			    		
			    		for(Node<Element> knd:keypoints){
			    			knd.setIsKeyPoint(true);
			    		}			    	
			    	}
			    	}
			    	
			    }
		    }
	    }
	    
	    List<Node<Element>> refinedkps=refineKeyHeadSet(keypoints);
	    
		return refinedkps;
	}
	/**
	 * Extract TPattern from a set of RGs. 
	 * @return the TPattern.
	 */
	public PKHTPattern extractTPattern(List<RelationshipGraph> treelist,String pcname){
		List<RelationshipGraph> cadidates=new LinkedList<RelationshipGraph>();
		PKHTPattern pc_tpattern=new PKHTPattern();
		if(treelist!=null){
			if(treelist.size()>0){
				RelationshipGraph rt=new RelationshipGraph();
				int depth=0;
				for(int i=0;i<treelist.size();i++){
					depth=treelist.get(i).depth();
					if(depth!=0){
						rt=treelist.get(i);
						break;
					}
				}
				  
				  //RelationTree rt=treelist.get(0);
				  for(RelationshipGraph tree: treelist){
					  if(tree.depth()>1){
						  cadidates.add(tree);
						  if(depth>tree.depth()){
							  depth=tree.depth();
							  rt=tree;
						  }
					  }					  
				  }
				  cadidates.remove(rt);
				  List<Node<Element>> kp= detectKeyHead(rt,cadidates);

				  List<AbstractRelationGraph> art=new LinkedList<AbstractRelationGraph>();
				  if(kp.size()>0){
					  pc_tpattern.setKeypointTP(extractPatternWithKHead(rt,kp,pcname));
					  art.add(pc_tpattern.getKeyHeadTP());
				  }
				  pc_tpattern.setPointcutName(pcname);
				}
		}
		return pc_tpattern;
		
	}


	private Node<Element> findHasKHeadLevel(List<Node<Element>> childrend,String kpname){
		Node<Element> kpNode=null;
		if(childrend!=null){
			for(Node<Element> child:childrend){
				if(child!=null){
					if(child.getData()!=null){
						checkName(child);
						if(child.getData().getTraditionalFullName().equals(kpname)){
							kpNode=child;
							return kpNode;
						}
						else{
							kpNode=findHasKHeadLevel(child.getChildrend(),kpname);
							if(kpNode!=null){
								return kpNode;
							}
						}
					}
				}
				
			}
		}
		
		return kpNode;
	}
	private AbstractRelationGraph extractPatternWithKHead(RelationshipGraph tree, List<Node<Element>> kps,String pcname){
		AbstractRelationGraph atree=new AbstractRelationGraph();
		PatternNode<String> root=new PatternNode<String>();
		root.setData("__");
		root.setName(pcname); //later change this name to a give pointcut name.
		root.setRelation(null);
		atree.setWildCardRoot(root);
		
		List<PatternNode<String>> nodelist=new LinkedList<PatternNode<String>>();
			if(kps!=null){
				for(Node<Element>  keypoint: kps){
					String kpname= keypoint.getData().getTraditionalFullName();
					List<Node<Element>> children= tree.getRoot().getChildrend();//why only check the first level
					if(children!=null){
						
						Node<Element> kpnode=findHasKHeadLevel(children,kpname);
						if(kpnode!=null){
							//If we found same keypoint in the tree, then we ignore this key point
							boolean kflag=true;
					    	if(nodelist.size()>0){
					    		for(PatternNode<String> pn:nodelist){
						    		if(pn!=null){
						    			if(pn.getName().equals(kpname)){
						    				kflag=false;
						    				break;
						    				
						    			}
						    		}
						    	}
					    	}
					    	if(kflag){
					    	//
							PatternNode<String> node=new PatternNode<String>();
							if(kpnode.getRelation().equals(RelationType.mcall.name())){								
								PatternRelation relation =new AbstractPatternRelation(kpnode.getRelation());
								node.setRelation(relation);
								boolean flag=false;
								if(kpnode.getChildrend()!=null){
									if(kpnode.getChildrend().size()>0){
										
										flag=addSubRGWithKHead(kpnode,node);
										if(flag){
										  node.setData("__");
										  node.setName(kpnode.getData().getTraditionalFullName());
										}			
									}
								}
								if(!flag){
									node.setData(kpnode.getData().getTraditionalFullName());
									node.setName(kpnode.getData().getTraditionalFullName());
									//
									
								}
								nodelist.add(node);
							
					    	
							}
							else if(kpnode.getRelation().equals(RelationType.fset.name()) || kpnode.getRelation().equals(RelationType.mconcretize.name())){
								PatternRelation relation=new AbstractPatternRelation(kpnode.getRelation());
						    	node.setRelation(relation);
						    	node.setData(kpnode.getData().getTraditionalFullName());
						    	node.setName(kpnode.getData().getTraditionalFullName());
						    	nodelist.add(node);
							}
							
						}
						}
						
					}
					
				}
			}
			
			root.setChildren(nodelist);
			//art.add(atree);
			return atree;
		
	}
	
    /*
     * In this case, we should consider two situations
     * 1. fset relationship
     * 2. mconcritize relationship
     * return value represents whether the method has the mconcritize relationhsip.
     */
	private boolean addSubRGWithKHead(Node<Element> parent,PatternNode<String> p_parent){
        boolean flag=false;
	    List<PatternNode<String>> nodelist=new LinkedList<PatternNode<String>>();
		
		List<Node<Element>> children=parent.getChildrend();
		if(children!=null){
			for(Node<Element> child: children){
				PatternNode<String> node=new PatternNode<String>();
				checkName(child);
				if(child.getRelation().equals(RelationType.mconcretize.name())){
					PatternRelation relation =new AbstractPatternRelation(child.getRelation());
					node.setRelation(relation);
					node.setData(child.getData().getTraditionalFullName());
					node.setName(child.getData().getTraditionalFullName());
					nodelist.add(node);
					flag=true;
				}	
			    else if(child.getRelation().equals(RelationType.fset.name())){
				    PatternRelation relation=new AbstractPatternRelation(child.getRelation());
		    	    node.setRelation(relation);
		    	    node.setData(child.getData().getTraditionalFullName());
		    	    node.setName(child.getData().getTraditionalFullName());
		    	    nodelist.add(node);
			    }
			    else if(child.getRelation().equals(RelationType.mcall.name())){
			    				    	
			    	List<Node<Element>> resnode=findMconcretize(child);	
			    	if(resnode!=null){
			    		if(resnode.size()>0){
			    			PatternRelation relation=new AbstractPatternRelation(child.getRelation());
				    	    node.setRelation(relation);
				    	    node.setData("__");
				    	    node.setName(child.getData().getTraditionalFullName());
				    	    
				    	    //
				    	    
				    	    List<PatternNode<String>> childnodelist=new LinkedList<PatternNode<String>>();
				    	    for(Node<Element> res:resnode){
				    	    	PatternNode<String> childnode=new PatternNode<String>();
				    	    	PatternRelation childrelation=new AbstractPatternRelation(res.getRelation());
				    	    	childnode.setRelation(childrelation);
				    	    	childnode.setData(res.getData().getTraditionalFullName());
				    	    	childnode.setName(res.getData().getTraditionalFullName());
				    	    	childnodelist.add(childnode);
				    	    }
				    	    node.setChildren(childnodelist);
				    	    nodelist.add(node);
				    	    flag=true;
			    		}
			    		
			    	}
			    }
			
		   }
		}
		
	   p_parent.setChildren(nodelist);
	   return flag;
	}
	//find mconcretize relation
	private List<Node<Element>> findMconcretize(Node<Element> node){
		List<Node<Element>> resnodes=new LinkedList<Node<Element>>();
		
		if(node!=null){
			List<Node<Element>> children=node.getChildrend();
			if(children!=null){
				for(Node<Element> child:children){
					checkName(child);
					if(child.getRelation().equals(RelationType.mconcretize.name())){
						resnodes.add(child);
						
					}
					else if(child.getRelation().equals(RelationType.mcall.name())){						
						List<Node<Element>> resnode=findMconcretize(child);				    	
				    	if(resnode!=null){
				    		if(resnode.size()>0){
				    	       resnodes.addAll(resnode);
				    		}
				    		
				    	}
					}
					
				}
			}
		}
		return resnodes;
	}

	private void checkName(Node<Element> new_node){
		//change  the name of method <init> to new
		//System.out.println(new_node.getData().getTraditionalFullName());
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
}
