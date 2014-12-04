package jaist.info.aspectj.nataly2.popup.actions;

import jaist.info.aspectj.nataly2.ajdt.provider.ExecuteExperimentA;
import jaist.info.aspectj.nataly2.metamodel.RelationElements;
import jaist.info.aspectj.nataly2.metamodel.TPattern;
import jaist.info.aspectj.nataly2.seed.PointcutSignatureAnalyzer;
import jaist.info.aspectj.nataly2.test.ExperimentForUpdate;
import jaist.info.aspectj.nataly2.tools.PatternPersistenceUtil;

import java.util.Iterator;
import java.util.List;


import org.eclipse.ajdt.core.javaelements.PointcutElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class TestUpdateVersion implements IObjectActionDelegate {

	private Shell shell;
	private String pcname;
	private String pc_fullname;
	private ExecuteExperimentA experiment;
	private ExperimentForUpdate experiment_up;
	/**
	 * Constructor for Action1.
	 */
	public TestUpdateVersion() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {

        experiment=new ExecuteExperimentA();
        experiment_up=new ExperimentForUpdate();

		RelationElements relems=RelationElements.getRelationElements();
		String path=relems.getFilePath();
		String pointcuttest=pcname+"test";
		List<String> elems=experiment.getJoinPointShadows(pointcuttest);

		TPattern pattern=PatternPersistenceUtil.readTRPattern(path, pcname);		
		List<String> relevants=experiment_up.getRelevants(path, pcname);
		List<String> npc_jpsList=experiment.getJoinPointShadows(pcname);
		experiment_up.test(pc_fullname, relevants, npc_jpsList, pattern,elems,pcname);
		MessageDialog.openInformation(
			shell,
			"Nataly",
			"testforupdate was executed.");
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		IStructuredSelection aSelection=null;
		if (selection instanceof IStructuredSelection)
			 aSelection = (IStructuredSelection) selection;
		final Iterator i = aSelection.iterator();
		while (i.hasNext()) {
			final Object lNext = i.next();
			 if(lNext instanceof PointcutElement){
				 PointcutElement pce=(PointcutElement)lNext;
				 //System.out.println("Unit: "+pce.getCompilationUnit().getElementName());
				 this.pcname=pce.getElementName(); 
				 try {
					 this.pc_fullname=pce.retrieveSignatureFromSource();
					 
					
				} catch (JavaModelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			 
			 }

		}
	}

}
