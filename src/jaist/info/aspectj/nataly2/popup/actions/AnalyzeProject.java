package jaist.info.aspectj.nataly2.popup.actions;



import jaist.info.aspectj.nataly2.ajdt.provider.AspectJShadows;
import jaist.info.aspectj.nataly2.core.ExNatalyProvider;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class AnalyzeProject implements IObjectActionDelegate {

	private Shell shell;
	private ExNatalyProvider provider;
	
	/**
	 * Constructor for Action1.
	 */
	public AnalyzeProject() {
		
		super();
		provider=new ExNatalyProvider();
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
		
        AspectJShadows ajsh=AspectJShadows.getAspectJShadowsInstance();
        ajsh.Refresh();
        long begin =System.currentTimeMillis();
		provider.AnalyzeProject(getProgressMonitor());
		long current=System.currentTimeMillis();
		System.out.println((current-begin)+"ms");
		
		MessageDialog.openInformation(
			shell,
			"Nataly",
			"AnalyzeProject was executed.");
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		provider.selectionChanged(action, selection);				
	
	}
	protected IProgressMonitor getProgressMonitor() {
		final IProgressMonitor lMonitor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences()[0]
				.getView(true).getViewSite().getActionBars()
				.getStatusLineManager().getProgressMonitor();
		return lMonitor;
	}
}
