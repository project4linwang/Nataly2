package jaist.info.aspectj.nataly2.ajdt.provider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.aspectj.ajdt.internal.compiler.ast.PointcutDeclaration;
import org.aspectj.ajdt.internal.core.builder.AjBuildManager;
import org.aspectj.weaver.Shadow;
import org.eclipse.ajdt.core.builder.AJBuilder;
import org.eclipse.ajdt.core.builder.IAJBuildListener;
import org.eclipse.core.runtime.Assert;

public class AspectJShadows {
	private List<PointcutDeclaration> pointcutList;
	private List<Shadow> shadowList;
	private static AspectJShadows instance=null;
	private IAJBuildListener ajBuildListener;
	private ExtAsmHierarchyBuilder asmBuilder;
	private AspectJShadows(){
		pointcutList=new LinkedList<PointcutDeclaration>();
		shadowList=new LinkedList<Shadow>();
	}
	public static AspectJShadows getAspectJShadowsInstance(){
		if(instance==null){
			instance=new AspectJShadows();
		}
		return instance;
	}
	
	public List<PointcutDeclaration> getPoincutList(){
		return this.pointcutList;
	}
	public List<Shadow> getShadowList(){
		return this.shadowList;
	}
	public void Init(){
		initAspectJWeaverExtension();
		initASTWalker();
	}
	private void initAspectJWeaverExtension() {
		Assert.isTrue(ajBuildListener==null);
		ajBuildListener = new ExtPointcutBuildListener();
		AJBuilder.addAJBuildListener(ajBuildListener);
	}
	private void initASTWalker(){
		asmBuilder=new ExtAsmHierarchyBuilder();
		AjBuildManager.setAsmHierarchyBuilder(asmBuilder);
	}
	public void addPointcut(PointcutDeclaration pc){
		if(pointcutList.size()>0){
			for(PointcutDeclaration p:pointcutList){
				if(p.pointcutDesignator.getPointcutDeclarationText().equals(pc.pointcutDesignator.getPointcutDeclarationText())){
					return;
				}
			}
		}
		this.pointcutList.add(pc);
		
	}
	public void addShadow(Shadow sh){
		if(shadowList.size()>0){
			for(Shadow shadow:shadowList){
				if(shadow.toString().equals(sh.toString())){
					return;
				}
			}
		}
		this.shadowList.add(sh);
	}
	public void Refresh(){
		pointcutList.clear();
	    shadowList.clear();
	}
}
