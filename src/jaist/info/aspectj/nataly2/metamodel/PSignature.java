package jaist.info.aspectj.nataly2.metamodel;

import jaist.info.aspectj.nataly2.syntax.PAccessModifer;
import jaist.info.aspectj.nataly2.syntax.PClass;
import jaist.info.aspectj.nataly2.syntax.PEClass;
import jaist.info.aspectj.nataly2.syntax.PField;
import jaist.info.aspectj.nataly2.syntax.PMethod;
import jaist.info.aspectj.nataly2.syntax.PPackage;
import jaist.info.aspectj.nataly2.syntax.PParameter;
import jaist.info.aspectj.nataly2.syntax.PReturnType;
import jaist.info.aspectj.nataly2.syntax.PType;

public class PSignature {

	private PAccessModifer accessModifer;
	private PClass pclass;
	private PMethod pmethod;
	private PPackage ppackage;
	private PParameter ppara;
	private PReturnType returntype;
	private String exception;
	private PType ptype;
	private PField pfield;
	private PEClass peclass;
	public PSignature(){
		accessModifer=new PAccessModifer();
		pclass=new PClass();
		pmethod=new PMethod();
		ppackage=new PPackage();
		ppara=new PParameter();
		returntype=new PReturnType();
		exception=null;
	}
	public void setAccessModifer(PAccessModifer accessModifer) {
		this.accessModifer = accessModifer;
	}
	public PAccessModifer getAccessModifer() {
		return accessModifer;
	}
	public void setPclass(PClass pclass) {
		this.pclass = pclass;
	}
	public PClass getPclass() {
		return pclass;
	}
	public void setPmethod(PMethod pmethod) {
		this.pmethod = pmethod;
	}
	public PMethod getPmethod() {
		return pmethod;
	}
	public void setPpackage(PPackage ppackage) {
		this.ppackage = ppackage;
	}
	public PPackage getPpackage() {
		return ppackage;
	}
	public void setPpara(PParameter ppara) {
		this.ppara = ppara;
	}
	public PParameter getPpara() {
		return ppara;
	}
	public void setReturntype(PReturnType returntype) {
		this.returntype = returntype;
	}
	public PReturnType getReturntype() {
		return returntype;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public String getException() {
		return exception;
	}
	public void setPtype(PType ptype) {
		this.ptype = ptype;
	}
	public PType getPtype() {
		return ptype;
	}
	public void setPfield(PField pfield) {
		this.pfield = pfield;
	}
	public PField getPfield() {
		return pfield;
	}
	public void setPeclass(PEClass peclass) {
		this.peclass = peclass;
	}
	public PEClass getPeclass() {
		return peclass;
	}
	
}
