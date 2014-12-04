package jaist.info.aspectj.nataly2.code.generator;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

//import org.aspectj.org.eclipse.jdt.core.IBuffer;
//import org.aspectj.org.eclipse.jdt.core.ICompilationUnit;
//import org.aspectj.org.eclipse.jdt.core.IJavaElement;
//import org.aspectj.org.eclipse.jdt.core.IJavaProject;
//import org.aspectj.org.eclipse.jdt.core.IMember;
//import org.aspectj.org.eclipse.jdt.core.IMethod;
//import org.aspectj.org.eclipse.jdt.core.IPackageFragment;
//import org.aspectj.org.eclipse.jdt.core.IPackageFragmentRoot;
//import org.aspectj.org.eclipse.jdt.core.ISourceRange;
//import org.aspectj.org.eclipse.jdt.core.IType;
//import org.aspectj.org.eclipse.jdt.core.JavaCore;
//import org.aspectj.org.eclipse.jdt.core.JavaModelException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.corext.codemanipulation.StubUtility;
import org.eclipse.jdt.internal.corext.util.CodeFormatterUtil;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.Strings;



import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * Adding the code of pointcut into the source code.
 */
public class AddSourcePointcut {

	/**
	 * Add the code of analysis-based pointcut into the source code. Meanwhile, import a number of library into the 
	 * source code.
	 */
    public void addCode(final String codeUnit,final String analysispc, final IProject project){
    	IRunnableWithProgress job =new IRunnableWithProgress(){
   	
			@Override
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				// TODO Auto-generated method stub
				IJavaProject javaProject=JavaCore.create(project);
				if(javaProject.exists() && javaProject!=null){
					try{
						IPackageFragmentRoot root= javaProject.getPackageFragmentRoots()[0];
						for(IJavaElement pack: root.getChildren()){
							if(pack instanceof IPackageFragment){
								for(ICompilationUnit cu: ((IPackageFragment)pack).getCompilationUnits()){
									String unit=cu.getElementName();
									if(codeUnit.equals(unit)){
										String lineDelimiter=StubUtility.getLineDelimiterUsed((org.eclipse.jdt.core.IJavaProject) javaProject);
									    addAnalysisPointcut(monitor,cu,lineDelimiter,analysispc);
									}
								}
							}
						}
						
					}
					catch(JavaModelException e){
						e.printStackTrace();
					}
					catch(CoreException e){
						e.printStackTrace();
					}
				}
				
			}
    		
    	};
    	try{
    		ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(null);
    		monitorDialog.run(true, false, job);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }

    
    private void addAnalysisPointcut(IProgressMonitor monitor, ICompilationUnit parentCU, String lineDelimiter,String analysispc) throws CoreException{
    	parentCU.becomeWorkingCopy(new SubProgressMonitor(monitor,1));
    	IBuffer buffer = parentCU.getBuffer();
    	IType type=parentCU.getTypes()[0];
    	List<String> allImports=getImport(parentCU,lineDelimiter);
    	String typeContent=type.getSource();
    	for(String name: allImports){
    		parentCU.createImport(name, null, monitor);
    		JavaModelUtil.reconcile((org.eclipse.jdt.core.ICompilationUnit) parentCU);
    	}
    	
    	IMethod[] methods=type.getMethods();
    	if(methods.length>0){
    		IMember member= methods[0];
    		buffer.replace(member.getSourceRange().getOffset(), 0, analysispc);
    		JavaModelUtil.reconcile((org.eclipse.jdt.core.ICompilationUnit) parentCU);
    	}
    	ISourceRange sourceRange=type.getSourceRange();

    	parentCU.commitWorkingCopy(true, monitor);
    	if(parentCU!=null){
    		parentCU.discardWorkingCopy();
    	}
    	monitor.done();
    	
    }
	private List<String> getImport(ICompilationUnit parentCU,
			String lineDelimiter) throws JavaModelException {
		// TODO Auto-generated method stub
		List<String> allImports = new ArrayList<String>();
		for(int i=0; i<parentCU.getImports().length;i++){
			allImports.add(parentCU.getImports()[i].getElementName());
		}
		allImports.add("jaist.info.aspectj.nataly2.lib.TMatcher");
		allImports.add("jaist.info.aspectj.nataly2.lib.TPattern");
		allImports.add("org.aspectj.lang.JoinPoint");

		return allImports;
	}
}
