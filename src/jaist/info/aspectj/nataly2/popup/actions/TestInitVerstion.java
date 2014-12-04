package jaist.info.aspectj.nataly2.popup.actions;



import jaist.info.aspectj.nataly2.ajdt.provider.ExecuteExperimentA;
import jaist.info.aspectj.nataly2.extractor.TPatternExtractor;
import jaist.info.aspectj.nataly2.metamodel.RelationElements;
import jaist.info.aspectj.nataly2.metamodel.TPattern;
import jaist.info.aspectj.nataly2.seed.PointcutSignatureAnalyzer;
import jaist.info.aspectj.nataly2.seed.Seed;
import jaist.info.aspectj.nataly2.test.ExperimentForInit;
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

public class TestInitVerstion implements IObjectActionDelegate {

	private Shell shell;
	private String pcname;
	private String pc_fullname;
	private TPatternExtractor generator;
	private PointcutSignatureAnalyzer analyzer;
	private ExecuteExperimentA experiment;
	private ExperimentForInit testexperiment;
	private RelationElements relems=RelationElements.getRelationElements();
	/**
	 * Constructor for Action1.
	 */
	public TestInitVerstion() {
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
		analyzer=new PointcutSignatureAnalyzer();
		generator=new TPatternExtractor();
		experiment=new ExecuteExperimentA();
		long begin =System.currentTimeMillis();
		List<Seed> seedList= analyzer.AnalyzePointcut(pcname, pc_fullname);
		List<String> realCandidates= experiment.getJoinPointShadows(pcname);
		
		for(Seed seed:seedList){
			List<String> elems=null;
			String pointcuttest=pcname+"test";
			elems=experiment.getJoinPointShadows(pointcuttest);
			 String path=relems.getFilePath();
		     generator.extractTPattern(seed.getSeedList(), pcname);
		     TPattern pattern=PatternPersistenceUtil.readTRPattern(path, pcname);	
			 testexperiment=new ExperimentForInit();
			 testexperiment.test(pc_fullname, realCandidates, elems, pattern, pcname);
		}
		long current=System.currentTimeMillis();
		System.out.println((current-begin)+"ms");
		MessageDialog.openInformation(
			shell,
			"Nataly",
			"TestExperiment was executed.");
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
