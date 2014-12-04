package jaist.info.aspectj.nataly2.popup.actions;




import jaist.info.aspectj.nataly2.code.generator.CodeGenerator;
import jaist.info.aspectj.nataly2.extractor.TPatternExtractor;
import jaist.info.aspectj.nataly2.metamodel.PKHTPattern;
import jaist.info.aspectj.nataly2.seed.PointcutSignatureAnalyzer;
import jaist.info.aspectj.nataly2.seed.Seed;

import java.util.Iterator;
import java.util.List;

import org.aspectj.org.eclipse.jdt.core.IJavaElement;
import org.aspectj.org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ajdt.core.javaelements.PointcutElement;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class RefactorPointcut implements IObjectActionDelegate {

	private Shell shell;
	private String pcname;
	private String pc_fullname;
	private TPatternExtractor generator;
	private PointcutSignatureAnalyzer analyzer;
	private CodeGenerator code_generator;
	private String codeunit;
    private IProject aproject;
	/**
	 * Constructor for Action1.
	 */
	public RefactorPointcut() {
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
		long begin =System.currentTimeMillis();
		System.out.println(pcname);
		System.out.println(pc_fullname);
		List<Seed> candidatesList= analyzer.AnalyzePointcut(pcname, pc_fullname);
		code_generator=new CodeGenerator();
		
		for(Seed candidates:candidatesList){
			PKHTPattern pattern= generator.extractTPattern(candidates.getSeedList(), pcname);
			code_generator.gereratePointcutRefactor(codeunit,pcname, pc_fullname,aproject);
		}
		long current=System.currentTimeMillis();
		System.out.println((current-begin)+"ms");
		
		MessageDialog.openInformation(
			shell,
			"Nataly",
			"PCRefactorAction was executed.");
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
				 aproject=pce.getJavaProject().getProject();
				 try {
					 this.pc_fullname=pce.retrieveSignatureFromSource();
					 
					
				} catch (JavaModelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				 codeunit=pce.getCompilationUnit().getElementName();
			 }

		}
	}
	

	private IProject convertSelection(IStructuredSelection structuredSelection)
	{
	  IProject project = null;
	  Object element = structuredSelection.getFirstElement();

	  if (element instanceof IResource)
	  {
	    project = ((IResource) element).getProject();
	  }
	  else if (element instanceof IJavaElement)
	  {
	    IJavaElement javaElement = (IJavaElement) element;
	    project = javaElement.getJavaProject().getProject();
	  }
	  else if (element instanceof IAdaptable)
	  {
	    IAdaptable adaptable = (IAdaptable) element;
	    IWorkbenchAdapter adapter = (IWorkbenchAdapter) adaptable.getAdapter(IWorkbenchAdapter.class);
	    if (adapter != null)
	    {
	      Object parent = adapter.getParent(adaptable);
	      if (parent instanceof IJavaProject)
	      {
	        IJavaProject javaProject = (IJavaProject) parent;
	        project = javaProject.getProject();
	      }
	    }
	  }
	  else if(element instanceof PointcutElement){
		 PointcutElement pce= (PointcutElement)element;
		 pce.getJavaProject().getProject();
	  }

	  return project;
	}


}
