package jaist.info.aspectj.nataly2.TMatcher;

import jaist.info.aspectj.nataly2.metamodel.AbstractRelationGraph;
import jaist.info.aspectj.nataly2.metamodel.Element;
import jaist.info.aspectj.nataly2.metamodel.Node;
import jaist.info.aspectj.nataly2.metamodel.PatternNode;
import jaist.info.aspectj.nataly2.metamodel.RelationshipGraph;
import jaist.info.aspectj.nataly2.metamodel.RootNode;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.StringLabelFactory;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeReader;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

/*
 * This Matcher is not used for Tregex.
 */
public class OTMatcher {
	public double matches(RelationshipGraph can_tree,List<AbstractRelationGraph> regtrees){
		String tree_str=changetreetoString(can_tree);
		double matched_rate=0.00d;
		if(regtrees!=null){
			if(regtrees.size()>0){
				
				for(AbstractRelationGraph rt:regtrees){
					List<String> pathTregexlist=changePatterntoPathTregex(rt);

					if(pathTregexlist.size()>0){
						int matchednum=0;
						
						int allpathnum=pathTregexlist.size();
						for(String pathTregex:pathTregexlist){
							if(isMatched(tree_str,pathTregex)){
								matchednum++;
							}
						}
						double mr=(double)matchednum/allpathnum;
						if(mr>matched_rate){
							matched_rate=mr;
						}
					}
					
				}
				
				
			}
		}
		
		return matched_rate;
	}
	private String changetreetoString(RelationshipGraph tree){
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
	private List<String> changePatterntoPathTregex(AbstractRelationGraph tree){
		List<String> pathlist=new LinkedList<String>();
		if(tree!=null){
			PatternNode<String> node=tree.getWildCare_Root();
			
			
			List<PatternNode<String>> children=node.getChildrend();
			int index=0;
	        if(children!=null){
	        	for(PatternNode<String> child:children){
					StringBuilder path_str=new StringBuilder();
					path_str.append("("+reomveIllegalChar(node.getData())+")");
					int offset=1;							
						//
						path_str.insert(path_str.length()-offset, "<<");
						//offset++;
						
						path_str.insert(path_str.length()-offset, "("+reomveIllegalChar(child.getRelation().getPatternName())+")");
						offset++;
						path_str.insert(path_str.length()-offset, "<");
						path_str.insert(path_str.length()-offset, "("+reomveIllegalChar(child.getData())+")");
						//
						
						//
						addChildPath(child,path_str,pathlist,offset);
						
						//pathlist.add(childpath);
				
				}
	        }
			
		}
		return pathlist;
	}
	private String addChildPath(PatternNode<String> child, StringBuilder path_str,List<String> pathlist,int offset){
		List<PatternNode<String>> subchildlist=child.getChildrend();
		String childData=reomveIllegalChar(child.getData());
		String childRData=reomveIllegalChar(child.getRelation().getPatternName());
		String path=path_str.toString();
		StringBuilder tmp_path=new StringBuilder();
		int tmp_offset=offset;
		tmp_path.append(path_str.toString());
		if(subchildlist!=null){
			if(subchildlist.size()>0){
				int index=0;
				for(PatternNode<String> subchild:subchildlist){
					if(childData.equals("__")){
						String subchildRData=reomveIllegalChar(subchild.getRelation().getPatternName());
						String subchildData=reomveIllegalChar(subchild.getData());
						if(childData.equals(subchildData) && childRData.equals(subchildRData)){
							path=addChildPath(subchild,path_str,pathlist,offset);
						}
						else{
							int m_offset=tmp_offset;
							StringBuilder sub_path=new StringBuilder();
							sub_path.append(tmp_path.toString());
							m_offset++;
							if(index==0){
								
								sub_path.insert(sub_path.length()-m_offset, "<<");
								sub_path.insert(sub_path.length()-m_offset, "("+reomveIllegalChar(subchild.getRelation().getPatternName())+")");
								m_offset++;
								sub_path.insert(sub_path.length()-m_offset, "<");
								sub_path.insert(sub_path.length()-m_offset, "("+reomveIllegalChar(subchild.getData())+")");
								path=addChildPath(subchild,sub_path,pathlist,m_offset);
								//pathlist.add(path);
								
							}
							else{
								sub_path.insert(sub_path.length()-m_offset, "<<");
								sub_path.insert(sub_path.length()-m_offset, "("+reomveIllegalChar(subchild.getRelation().getPatternName())+")");
								m_offset++;
								sub_path.insert(sub_path.length()-m_offset, "<");
								sub_path.insert(sub_path.length()-m_offset, "("+reomveIllegalChar(subchild.getData())+")");
								path=addChildPath(subchild,sub_path,pathlist,m_offset);
								//pathlist.add(path);
							}
						}
					}
					else{
						int m_offset=tmp_offset;
						StringBuilder sub_path=new StringBuilder();
						sub_path.append(tmp_path.toString());
						m_offset++;
						if(index==0){
							
							sub_path.insert(sub_path.length()-m_offset, "<<");
							sub_path.insert(sub_path.length()-m_offset, "("+reomveIllegalChar(subchild.getRelation().getPatternName())+")");
							m_offset++;
							sub_path.insert(sub_path.length()-m_offset, "<");
							sub_path.insert(sub_path.length()-m_offset, "("+reomveIllegalChar(subchild.getData())+")");
							path=addChildPath(subchild,sub_path,pathlist,m_offset);
							//pathlist.add(path);
							
						}
						else{
							sub_path.insert(sub_path.length()-m_offset, "<<");
							sub_path.insert(sub_path.length()-m_offset, "("+reomveIllegalChar(subchild.getRelation().getPatternName())+")");
							m_offset++;
							sub_path.insert(sub_path.length()-m_offset, "<");
							sub_path.insert(sub_path.length()-m_offset, "("+reomveIllegalChar(subchild.getData())+")");
							path=addChildPath(subchild,sub_path,pathlist,m_offset);
							//pathlist.add(path);
						}
					}										
					index++;
				}
			}
			else{
				pathlist.add(path);
			}
		}
		else{
			pathlist.add(path);
		}
		return path;
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
	
}
