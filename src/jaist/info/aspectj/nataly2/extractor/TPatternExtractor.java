package jaist.info.aspectj.nataly2.extractor;



import jaist.info.aspectj.nataly2.extractor.Extractor;
import jaist.info.aspectj.nataly2.metamodel.Element;
import jaist.info.aspectj.nataly2.metamodel.PKHTPattern;
import jaist.info.aspectj.nataly2.metamodel.RelationElements;
import jaist.info.aspectj.nataly2.metamodel.RelationshipGraph;
import jaist.info.aspectj.nataly2.relationanalyzer.RelationshipGraphGenerator;
import jaist.info.aspectj.nataly2.tools.PatternPersistenceUtil;


import java.util.LinkedList;
import java.util.List;

/**
 * Facade of TPattern Extractor.
 * @author suse-wl
 *
 */
public class TPatternExtractor {

	private RelationElements relems=RelationElements.getRelationElements();
	/**
	 * 
	 * @param jplist is the set of seeds
	 * @param pcname is the name of pointcut
	 * @return
	 */
	public PKHTPattern extractTPattern(List<String> jplist,String pcname){
		RelationshipGraphGenerator rtGenerator=new RelationshipGraphGenerator();
		List<Element> elementlist= relems.getElementList();
		List<Element> joinpointList=new LinkedList<Element>();
		if(jplist!=null){
			if(jplist.size()>0){
				for(String jp:jplist){
					joinpointList.add(FindElement(jp,elementlist));
				}
			}
		}
		 List<RelationshipGraph> treeList=rtGenerator.generateRG(joinpointList);
		 Extractor exAlgorithm=new Extractor();
		 PKHTPattern tpattern= exAlgorithm.extractTPattern(treeList,pcname);
		 //save PTPattern to  a file		 
 		 PatternPersistenceUtil.writeTRPattern(tpattern, relems.getFilePath(), pcname);
		 //
		 return tpattern;
	}
	
	private Element FindElement(String fullname,List<Element> elementlist){
		 for(Element el: elementlist){
			 if(el.getTraditionalFullName().equals(fullname)){
				 return el; 
			 }
		 }
		 return null;
	 }

}
