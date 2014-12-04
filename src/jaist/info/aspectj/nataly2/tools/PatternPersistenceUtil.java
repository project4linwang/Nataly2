package jaist.info.aspectj.nataly2.tools;



import jaist.info.aspectj.nataly2.metamodel.AbstractRelationGraph;
import jaist.info.aspectj.nataly2.metamodel.PKHTPattern;
import jaist.info.aspectj.nataly2.metamodel.PatternNode;
import jaist.info.aspectj.nataly2.metamodel.TPattern;
import jaist.info.aspectj.nataly2.metamodel.TPatternPath;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class of tools for serializer and deserialize.
 * For TPattern.
 * @author suse-wl
 *
 */
public class PatternPersistenceUtil {

	public static void writeTRPattern(PKHTPattern pt,String filepath,String pcname){
		String filename=getRelativeXMLFullFileName(filepath+pcname);
 		TPattern pattern=new TPattern();
 		pattern.setPointcutName(pcname);
 

 		AbstractRelationGraph kpTree=pt.getKeyHeadTP();
 		if(kpTree!=null){
 			String trp=changePatterntoTregex(kpTree);
 			List<String> pathTregexlist=changePatterntoPathTregex(kpTree);
 			TPatternPath tsub=new TPatternPath();
			tsub.setData(trp);
			tsub.setPaths(pathTregexlist);
 			pattern.getKHeadTPattern().add(tsub);
 		}
 		try{
 				
 			File f=new File(filepath);
 			if(!f.exists()){
 				f.mkdir();
 			}
 			SerialXMLUtil.save(pattern, filename);
 		}
	    catch(IOException e){
 			System.out.println(e.toString());
 		}
 		
	}
	public static TPattern readTRPattern(String filepath,String pcname){
		String filename=getRelativeXMLFullFileName(filepath+pcname);
		TPattern trpattern=null;
		try {
			trpattern = (TPattern)SerialXMLUtil.load(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return trpattern;
	}
	/*
     * Get the name of the file, to which save the relation maps.
     */
	public static String getRelativeXMLFullFileName(String advpath){
		StringBuilder fileNameBuilder = new StringBuilder(advpath);
		fileNameBuilder.append("#" + "sourcecode");
		fileNameBuilder.append("-ptpattern.xml");
		return fileNameBuilder.toString();
	}
	private static List<String> changePatterntoPathTregex(AbstractRelationGraph tree){
		List<String> pathlist=new LinkedList<String>();
		if(tree!=null){
			PatternNode<String> node=tree.getWildCare_Root();
			
			
			List<PatternNode<String>> children=node.getChildrend();
	        if(children!=null){
	        	for(PatternNode<String> child:children){
					StringBuilder path_str=new StringBuilder();
					path_str.append("("+reomveIllegalChar(node.getData())+")");
					int offset=1;							
						//
						path_str.insert(path_str.length()-offset, "<<");			
						
						path_str.insert(path_str.length()-offset, "("+reomveIllegalChar(child.getRelation().getPatternName())+")");
						offset++;
						path_str.insert(path_str.length()-offset, "<");
						path_str.insert(path_str.length()-offset, "("+reomveIllegalChar(child.getData())+")");
						//
						
						//
						addChildPath(child,path_str,pathlist,offset);						
				
				}
	        }
			
		}
		return pathlist;
	}
	private static String addChildPath(PatternNode<String> child, StringBuilder path_str,List<String> pathlist,int offset){
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
						
								
							}
							else{
								sub_path.insert(sub_path.length()-m_offset, "<<");
								sub_path.insert(sub_path.length()-m_offset, "("+reomveIllegalChar(subchild.getRelation().getPatternName())+")");
								m_offset++;
								sub_path.insert(sub_path.length()-m_offset, "<");
								sub_path.insert(sub_path.length()-m_offset, "("+reomveIllegalChar(subchild.getData())+")");
								path=addChildPath(subchild,sub_path,pathlist,m_offset);
							
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
					
							
						}
						else{
							sub_path.insert(sub_path.length()-m_offset, "<<");
							sub_path.insert(sub_path.length()-m_offset, "("+reomveIllegalChar(subchild.getRelation().getPatternName())+")");
							m_offset++;
							sub_path.insert(sub_path.length()-m_offset, "<");
							sub_path.insert(sub_path.length()-m_offset, "("+reomveIllegalChar(subchild.getData())+")");
							path=addChildPath(subchild,sub_path,pathlist,m_offset);
					
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
	private static String changePatterntoTregex(AbstractRelationGraph tree){
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
	private static void addTregexChild(PatternNode<String> pn, StringBuilder treg_str,int offset){
		int default_offset=offset;
		List<PatternNode<String>> children=pn.getChildrend();
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
						//offset++;
						treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getRelation().getPatternName())+")");
						offset++;
						treg_str.insert(treg_str.length()-offset, "<");
						treg_str.insert(treg_str.length()-offset, "("+reomveIllegalChar(child.getData())+")");
						//offset++;
					}
					index++;
					addTregexChild(child,treg_str,offset);
				}
			}
		}
		
	}
	private static String reomveIllegalChar(String str){
		 
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
