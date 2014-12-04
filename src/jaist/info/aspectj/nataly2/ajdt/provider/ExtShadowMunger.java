package jaist.info.aspectj.nataly2.ajdt.provider;

import java.util.Collection;
import java.util.Map;

import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.Pointcut;

public class ExtShadowMunger extends ShadowMunger {

	private AspectJShadows ajsh=AspectJShadows.getAspectJShadowsInstance();
	public static ExtShadowMunger createPointcutMunger(ResolvedPointcutDefinition ptcDef) {
		Pointcut ptc = ptcDef.getPointcut();
		ExtShadowMunger munger = createMungerPerPointcut(ptc, false);
		
		return munger;
	}
	private static ExtShadowMunger createMungerPerPointcut(Pointcut ptc, boolean advice) {
		ExtShadowMunger munger = new ExtShadowMunger(ptc, ptc.getStart(), ptc.getEnd(), ptc.getSourceContext(), advice);
		return munger;
	}
	public ExtShadowMunger(Pointcut pointcut, int start, int end, ISourceContext sourceContext, boolean advice) {
		super(pointcut, start, end, sourceContext, ShadowMungerDeow);

	}
	@Override
	public boolean match(Shadow shadow, World world) {		
		ajsh.addShadow(shadow);
		//ajsh.getShadowList().add(shadow);		
		return false;
	}
	@Override
	public ShadowMunger concretize(ResolvedType arg0, World arg1, PerClause arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResolvedType getConcreteAspect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ResolvedType> getThrownExceptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean implementOn(Shadow arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mustCheckExceptions() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ShadowMunger parameterizeWith(ResolvedType arg0,
			Map<String, UnresolvedType> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void specializeOn(Shadow arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
