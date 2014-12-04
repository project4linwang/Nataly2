package jaist.info.aspectj.nataly2.ajdt.provider;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.CustomMungerFactory;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.ShadowMunger;

public class ExtPointcutMungerFactory implements CustomMungerFactory {

	@Override
	public Collection<ShadowMunger> createCustomShadowMungers(ResolvedType aspectType) {
		// TODO Auto-generated method stub
		List<ShadowMunger> newm = new ArrayList<ShadowMunger>();
		ResolvedMember[] pointcuts = aspectType.getDeclaredPointcuts();
		for (ResolvedMember ptc:pointcuts){
			if (ptc instanceof ResolvedPointcutDefinition) {//TODO why "instanceof" is needed here?
				newm.add(ExtShadowMunger.createPointcutMunger((ResolvedPointcutDefinition)ptc));
			}
		}
		 
		return newm;
	}

	@Override
	public Collection<ConcreteTypeMunger> createCustomTypeMungers(
			ResolvedType arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ShadowMunger> getAllCreatedCustomShadowMungers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ConcreteTypeMunger> getAllCreatedCustomTypeMungers() {
		// TODO Auto-generated method stub
		return null;
	}

}
