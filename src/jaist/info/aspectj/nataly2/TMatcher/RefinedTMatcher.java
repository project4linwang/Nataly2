package jaist.info.aspectj.nataly2.TMatcher;

import jaist.info.aspectj.nataly2.metamodel.AbstractRelationGraph;
import jaist.info.aspectj.nataly2.metamodel.Element;
import jaist.info.aspectj.nataly2.metamodel.Node;
import jaist.info.aspectj.nataly2.metamodel.PatternNode;
import jaist.info.aspectj.nataly2.metamodel.RelationshipGraph;
import jaist.info.aspectj.nataly2.metamodel.RootNode;
import jaist.info.aspectj.nataly2.metamodel.TPatternPath;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.StringLabelFactory;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeReader;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

public class RefinedTMatcher {

	public double tmatches(RelationshipGraph can_tree,List<TPatternPath> reg_patterns){
		String tree_str=changegraphtoString(can_tree);
		double matched_rate=0.00d;
	    if(reg_patterns!=null){
	    	if(reg_patterns.size()>0){
	    		for(TPatternPath reg:reg_patterns){
	    			matched_rate = tMatchesPaths(tree_str, matched_rate, reg);
	    		}
	    	}
	    }
	    return matched_rate;
	}
	private double tMatchesPaths(String tree_str, double matched_rate,
			TPatternPath reg) {
		List<String> pathTregexlist=reg.getPaths();
		if(pathTregexlist.size()>0){
			int retrievedValue=0;
			int matchednum=0;
			int allpathnum=pathTregexlist.size();
			for(String pathTregex:pathTregexlist){
				int weight=1;
				String regstr="\\bmconcretize\\b";
				Pattern pattern=Pattern.compile(regstr);
				Matcher matcher=pattern.matcher(pathTregex);
				if(matcher.find()){
					weight=5;
				}
				if(isMatched(tree_str,pathTregex)){
					retrievedValue=retrievedValue+weight;
					matchednum++;
				}
			}
			double mr=(double)retrievedValue/(allpathnum-matchednum+retrievedValue);
			if(mr>matched_rate){
				matched_rate=mr;
			}
		}
		else{
		    int matchednum=0;
			int allpathnum=1;
			if(isMatched(tree_str,reg.getData())){
				matchednum++;
			}
			double mr=(double)matchednum/allpathnum;
			if(mr>matched_rate){
				matched_rate=mr;
			}
		}
		return matched_rate;
	}
	public Map<RelationshipGraph,Double> TgetMatchedRankingData(List<RelationshipGraph> treelist, List<TPatternPath> regtexPattern){
		Map<RelationshipGraph,Double> rank_list=new HashMap<RelationshipGraph,Double>();
		if(treelist.size()>0){
			for(RelationshipGraph graph: treelist){
				double rank=tmatches(graph,regtexPattern);
				rank_list.put(graph, rank);
			}
		}
		return rank_list;
	}
	public List<RelationshipGraph> matchTRefinedCandidate(List<RelationshipGraph> treelist,List<TPatternPath> regtexPattern,List<String> nwlist){
		List<RelationshipGraph> tmpGraph=new LinkedList<RelationshipGraph>();
		List<RelationshipGraph> relevanttree=new LinkedList<RelationshipGraph>();
		List<RelationshipGraph> shortestdpTrees=new LinkedList<RelationshipGraph>();
		if(treelist.size()>0){
			for(RelationshipGraph tree: treelist){
				if(isTMatched(tree, regtexPattern)){
						
					if(nwlist.contains(tree.getRoot().getData().getTraditionalFullName())){
						relevanttree.add(tree);
					}
					else{
						tmpGraph.add(tree);
					}
				}
				
			}
		}
		tmpGraph=getRefinedGraphs(tmpGraph,relevanttree);
		while(tmpGraph.size()>0){
			List<RelationshipGraph> shorttree=getRefinedGraphs(tmpGraph);
			shortestdpTrees.addAll(shorttree);
			tmpGraph=getRefinedGraphs(tmpGraph,shorttree);
		}
		shortestdpTrees.addAll(relevanttree);
		
		return shortestdpTrees;
	}
	public boolean isTMatched(RelationshipGraph graph, List<TPatternPath> regtexPattern){
		if(regtexPattern!=null){
			if(regtexPattern.size()>0){
				String str_tree=changegraphtoString(graph);
			   
				for(TPatternPath reg:regtexPattern){
					boolean ismatch=isMatched(str_tree,reg.getData());
					if(ismatch){
						return true;
					}
					else{
						double matchedRate=tMatchesPaths(str_tree,0.0d,reg);
						if(matchedRate>=0.8){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	private List<RelationshipGraph> getRefinedGraphs(List<RelationshipGraph> tempTrees,List<RelationshipGraph>shortestTrees){
		List<RelationshipGraph> refinedGraphs=new LinkedList<RelationshipGraph>();
		if(tempTrees!=null){
			if(tempTrees.size()>0){
				refinedGraphs.addAll(tempTrees);
				for(RelationshipGraph rt:tempTrees){
					Queue<Node<Element>> tlist=rt.breadthSearch();
					if(tlist!=null){
						for(Node<Element> el:tlist){
							boolean isinclude=false;
							for(RelationshipGraph srt:shortestTrees){
								if(!rt.equals(srt)){
									if(el.getData().getTraditionalFullName().equals(srt.getRoot().getData().getTraditionalFullName())){
										refinedGraphs.remove(rt);
										isinclude=true;
										break;
									}
								}
							}
							if(isinclude){
								break;//net.sf.jftp.net.FtpConnection.uploadDir(java.lang.String)
							}
						}
					}
				}
				
			}
		}
		return refinedGraphs;
	}
	private List<RelationshipGraph> getRefinedGraphs(List<RelationshipGraph> treelist){
		List<RelationshipGraph> shortestTrees=new LinkedList<RelationshipGraph>();
		List<RelationshipGraph> tmpGraphs=new LinkedList<RelationshipGraph>();
		tmpGraphs.addAll(treelist);
		
		   int shortestLength=10000000;
		   if(treelist!=null){
			   if(treelist.size()>0){
				   for(RelationshipGraph graph:tmpGraphs){
					   if(graph.depth()<shortestLength){
						   shortestLength=graph.depth();
					   }		  
				   }
				   for(RelationshipGraph graph:tmpGraphs){
					   boolean isinclude=false;										   
					   if(graph.depth()==shortestLength){						   						  
						   Queue<Node<Element>> tlist=graph.breadthSearch();						   						   
						   if(tlist!=null){
							   if(tlist.size()>0){
								   for(Node<Element> el:tlist){
									   isinclude=false;
									   for(RelationshipGraph srt:tmpGraphs){
										   if(!graph.equals(srt)){
											   if(el.getData().getTraditionalFullName().equals(srt.getRoot().getData().getTraditionalFullName())){ 
												  treelist.remove(graph);
												  isinclude=true; 												
												  break;
											   }
										   }
									   }
									   if(isinclude){
										   break;
									   }
								   }
							   }
						   }						   						   
					   }
					   if(!isinclude){
						   shortestTrees.add(graph);
						   treelist.remove(graph);
					   }
				   }
			   }
		   }
		   
		   return shortestTrees;
	}
	/*
	 * match candidate by using shortest approach, remove the candidates, which include the shortest candidate tree
	 */
	public List<RelationshipGraph> matchshortestCandidate(List<RelationshipGraph> treelist,List<AbstractRelationGraph> regtreelist){
		List<RelationshipGraph> tmptree=new LinkedList<RelationshipGraph>();
		List<RelationshipGraph> canditree=new LinkedList<RelationshipGraph>();
		RelationshipGraph shtree=null;
		int shortestLength=1000000000;
		if(treelist.size()>0){
			for(RelationshipGraph tree: treelist){
				if(isMatched(tree, regtreelist)){
					
					if(tree.depth()<shortestLength){
						if(shtree!=null){
							tmptree.add(shtree);
							shortestLength=tree.depth();
							shtree=tree;
						}
						else{
							shortestLength=tree.depth();
							shtree=tree;
						}
						
					}
					else{
					   tmptree.add(tree);
					}
				}
			}
			if(shtree!=null){
				canditree.add(shtree);
				if(tmptree.size()>0){
					for(RelationshipGraph rt:tmptree){
						Queue<Node<Element>> tlist=rt.breadthSearch();
						boolean isinclude=false;
						if(tlist!=null){
							for(Node<Element> el:tlist){
								
								if(el.getData().getTraditionalFullName().equals(shtree.getRoot().getData().getTraditionalFullName()))
								{
									isinclude=true;
									break;
								}
							
							}
						}
						if(!isinclude){
							canditree.add(rt);
						}
						
					}
				}
			}
			
		}
		return canditree;
	}
	/*
	 * match candidate by TP
	 */
	public boolean isMatched(RelationshipGraph tree,List<AbstractRelationGraph> regtex_tree){
		if(regtex_tree!=null){
			if(regtex_tree.size()>0){
				for(AbstractRelationGraph rt:regtex_tree){
					boolean ismatch=isMatched(tree,rt);
					if(ismatch){
						
						return true;
					}
				}
			}
		}
		return false;
		
	}
	public boolean isMatched(RelationshipGraph tree,AbstractRelationGraph regtex_tree) {
		String regtex=changePatterntoTregex(regtex_tree);
		TregexPattern tpattern=TregexPattern.compile(regtex);
		String str_tree=changegraphtoString(tree);
		TreeReader r=new PennTreeReader(new StringReader(str_tree),new LabeledScoredTreeFactory(new StringLabelFactory()));
		Tree t=null;
		try {
			t = r.readTree();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(t!=null){
			TregexMatcher matcher=tpattern.matcher(t);
			try{
				while(matcher.find()){
					return true;
				}
			}
			catch(Exception e){
				System.out.println(e.toString());
				return false;
			}
			
		}
		return false;
	}
	private String reomveIllegalChar(String str){
		 
		String regstr="\\(.*\\)";
		String legalStr=str.replaceFirst("\\$", "-");
		Pattern pattern=Pattern.compile(regstr);
		Matcher matcher=pattern.matcher(str);
		if(matcher.find()){
		   legalStr= matcher.replaceFirst("");
		}
		return legalStr.replaceAll("\\.", "-");
	}
	private String changePatterntoTregex(AbstractRelationGraph tree){
		StringBuilder treg_str=new StringBuilder();
		if(tree!=null){
			PatternNode<String> node=tree.getWildCare_Root();
			treg_str.append("("+reomveIllegalChar(node.getData())+")");
			List<PatternNode<String>> children=node.getChildrend();
			int index=0;
			int offset=1;
			if(children!=null){
				for(PatternNode<String> child: children){
					
				    offset=1;
					if(index==0){
						if(child.getChildrend()!=null){
							if(child.getChildrend().size()>0){
								treg_str.insert(treg_str.length()-offset, "<<");
								//offset++;
								treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getRelation().getPatternName())+")");
								offset++;
								treg_str.insert(treg_str.length()-offset, "<");
								treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getData())+")");
								
							}
							else{
								treg_str.insert(treg_str.length()-offset, "<<");							
								treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getRelation().getPatternName())+")");
								offset++;
								treg_str.insert(treg_str.length()-offset, "<");
								treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getData())+")");
						
							}
						}
						else{
							treg_str.insert(treg_str.length()-offset, "<<");
							treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getRelation().getPatternName())+")");
							offset++;
							treg_str.insert(treg_str.length()-offset, "<");
							treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getData())+")");
						}
						
						
					}
					else{
						treg_str.insert(treg_str.length()-offset, "&");
						treg_str.insert(treg_str.length()-offset, "<<");
						treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getRelation().getPatternName())+")");
						offset++;
						treg_str.insert(treg_str.length()-offset, "<");
						treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getData())+")");
					}					
					index++;
					addTregexChild(child,treg_str,offset);
				
				
			}
			}
			
		}
		return treg_str.toString();
		
	}
	private void addTregexChild(PatternNode<String> pn, StringBuilder treg_str,int offset){
		List<PatternNode<String>> children=pn.getChildrend();
		int default_offset=offset;
		if(children!=null){
			if(children.size()>0){
				int index=0;
				for(PatternNode<String> child:children){
					offset=default_offset;
					offset++;
					if(index==0){
						
						
						if(child.getChildrend()!=null){
							if(child.getChildrend().size()>0){
								treg_str.insert(treg_str.length()-offset, "<<");
								//treg_str.insert(treg_str.length()-offset,"()");
								//offset++;
								treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getRelation().getPatternName())+")");
								offset++;
								treg_str.insert(treg_str.length()-offset, "<");
								treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getData())+")");
								
							}
							else{
								treg_str.insert(treg_str.length()-offset, "<<");
								//treg_str.insert(treg_str.length()-1,"()");
								treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getRelation().getPatternName())+")");
								offset++;
								treg_str.insert(treg_str.length()-offset, "<");
								treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getData())+")");
								
							}
						}
						else{
							treg_str.insert(treg_str.length()-offset, "<<");
							//treg_str.insert(treg_str.length()-1,"()");
							treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getRelation().getPatternName())+")");
							offset++;
							treg_str.insert(treg_str.length()-offset, "<");
							treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getData())+")");
							
						}
						
					}
					else{
						
						treg_str.insert(treg_str.length()-offset, "&");
						treg_str.insert(treg_str.length()-offset, "<<");
				
						treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getRelation().getPatternName())+")");
						offset++;
						treg_str.insert(treg_str.length()-offset, "<");
						treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getData())+")");
					
					}
					index++;
					addTregexChild(child,treg_str,offset);
				}
			}
		}
		
	}
	private String changegraphtoString(RelationshipGraph tree){
		StringBuilder tree_strbld=new StringBuilder();
		RootNode<Element> rn=tree.getRoot();
		 List<Node<Element>> children=rn.getChildrend();
		tree_strbld.append("(");
		tree_strbld.append(reomveIllegalChar(rn.getData().getTraditionalFullName())+")");
		int offset=1;
		if(children!=null){
			if(children.size()>0){
				for(Node<Element> m_child:children){
					checkName(m_child);
					offset=1;
					tree_strbld.insert(tree_strbld.length()-offset, "("+reomveIllegalChar(m_child.getRelation())+")");
					offset++;
					tree_strbld.insert(tree_strbld.length()-offset, "("+reomveIllegalChar(m_child.getData().getTraditionalFullName())+")");
					offset++;
					addChildNode(m_child,tree_strbld,offset);
				}
			}
			
		}		
		return tree_strbld.toString();

	}
	private void addChildNode(Node<Element> child, StringBuilder tree_str,int offset){
		int default_offset=offset;
		if(child!=null){
			List<Node<Element>> sub_child=child.getChildrend();
			if(sub_child!=null){
				if(sub_child.size()>0){
					for(Node<Element> m_child: sub_child){
						offset=default_offset;
						tree_str.insert(tree_str.length()-offset, "("+reomveIllegalChar(m_child.getRelation())+")");
						offset++;
						tree_str.insert(tree_str.length()-offset, "("+reomveIllegalChar(m_child.getData().getTraditionalFullName())+")");
						offset++;
						addChildNode(m_child,tree_str,offset);
					}
				}
			}
			
		}
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
			new_node.getData().setFullName(tmpfullname.replaceFirst("<cinit>", "cnew"));
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
	public boolean isMatched(String can_tree, String regtex){
		TregexPattern tpattern=TregexPattern.compile(regtex);
		TreeReader r=new PennTreeReader(new StringReader(can_tree),new LabeledScoredTreeFactory(new StringLabelFactory()));
		Tree t=null;
		try {
			t = r.readTree();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(t!=null){
			try{
				TregexMatcher matcher=tpattern.matcher(t);
				while(matcher.find()){
					return true;
				}
			}
			catch(Exception e){
				System.out.println(e.toString());
				return false;
			}
			
		}
		
		return false;
	}
}
