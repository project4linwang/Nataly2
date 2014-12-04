package jaist.info.aspectj.nataly2.ajdt.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.ajde.core.AjCompiler;
import org.eclipse.ajdt.core.AspectJPlugin;
import org.eclipse.ajdt.core.builder.IAJBuildListener;
import org.eclipse.ajdt.core.lazystart.IAdviceChangedListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.compiler.CategorizedProblem;

public class ExtPointcutBuildListener implements IAJBuildListener {

	Map<IProject, ExtPointcutMungerFactory> factoryMap = new HashMap<IProject, ExtPointcutMungerFactory>();
	@Override
	public void addAdviceListener(IAdviceChangedListener adviceListener) {
		// TODO Auto-generated method stub

	}

	public void postAJBuild(int kind, IProject project, boolean noSourceChanges) {
		// TODO Auto-generated method stub
		AjCompiler compiler = AspectJPlugin.getDefault().getCompilerFactory()
		.getCompilerForProject(project);
	    compiler.setCustomMungerFactory(null);
	}

	@Override
	public void postAJClean(IProject project) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preAJBuild(int kind, IProject project,
			IProject[] requiredProjects) {
		// TODO Auto-generated method stub
		ExtPointcutMungerFactory factory = factoryMap.get(project);
		if (factory == null) {
			factory = new ExtPointcutMungerFactory();
			factoryMap.put(project, factory);
		}
		//AspectJShadows ajsh=AspectJShadows.getAspectJShadowsInstance();
		//ajsh.Refresh();
		AjCompiler compiler = AspectJPlugin.getDefault().getCompilerFactory()
		.getCompilerForProject(project);
	    compiler.setCustomMungerFactory(factory);

	}

	@Override
	public void removeAdviceListener(IAdviceChangedListener adviceListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postAJBuild(int kind, IProject project,
			boolean noSourceChanges,
			Map<IFile, List<CategorizedProblem>> newProblems) {
		// TODO Auto-generated method stub
		
	}

}
