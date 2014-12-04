package jaist.info.aspectj.nataly2.core;



import jaist.info.aspectj.nataly2.metamodel.Element;
import jaist.info.aspectj.nataly2.metamodel.RelationElements;
import jaist.info.aspectj.nataly2.relationanalyzer.RelationAnalyzer;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;

/**
 * The facade of Relationship Analyzer component.
 */
public class ExNatalyProvider {
	private RelationAnalyzer analyzer=new RelationAnalyzer();
	//private RelationTreeGenerator RTgenerator=new RelationTreeGenerator();
	private List<Element> elementlist=new LinkedList<Element>();
	/**
	 * Analyze the project.
	 */
	public void AnalyzeProject(IProgressMonitor lMonitor){
		RelationElements relation_elems=RelationElements.getRelationElements();
		elementlist=analyzer.AnalyzeSourceCode(lMonitor);
		relation_elems.setElementList(elementlist);	
	}
	public void selectionChanged(IAction action, ISelection selection) {
		analyzer.selectionChanged(action, selection);
    }
	
}
