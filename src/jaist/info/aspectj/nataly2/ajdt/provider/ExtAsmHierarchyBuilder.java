package jaist.info.aspectj.nataly2.ajdt.provider;

import org.aspectj.ajdt.internal.compiler.ast.AdviceDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.AspectDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.PointcutDeclaration;
import org.aspectj.ajdt.internal.core.builder.AsmHierarchyBuilder;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;

public class ExtAsmHierarchyBuilder extends AsmHierarchyBuilder {
	private AspectJShadows ajsh=AspectJShadows.getAspectJShadowsInstance();
	
	public boolean visit(TypeDeclaration td, CompilationUnitScope s){
    	super.visit(td, s);
    	
    	if(td instanceof AspectDeclaration){
    		
    		SourceTypeBinding source=td.binding;
    		String filename=new String(source.getFileName());  		
    		String aspectname=((AspectDeclaration)td).typeX.getSimpleName();
    	}
    	return true;
    }
    public boolean visit(MethodDeclaration md, ClassScope s){
    	super.visit(md, s);
    	if(md instanceof AdviceDeclaration ){
    		AdviceDeclaration advice=(AdviceDeclaration)md;
    		
    	}
    	if(md instanceof PointcutDeclaration){
    		PointcutDeclaration pointcut=(PointcutDeclaration)md;
    		if(pointcut!=null){
    			if(pointcut.pointcutDesignator!=null){
    				String name=pointcut.pointcutDesignator.getPointcut().toString();
    				
            		ajsh.addPointcut(pointcut);
    			}
    			
    		}
    		

    	}
    	return true;
    }
}
