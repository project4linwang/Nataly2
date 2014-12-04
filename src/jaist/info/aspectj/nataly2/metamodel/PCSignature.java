package jaist.info.aspectj.nataly2.metamodel;



import jaist.info.aspectj.nataly2.seed.Signature;

import java.util.LinkedList;
import java.util.List;

public class PCSignature {

	private String pcname;
	private List<Signature> signatures=new LinkedList<Signature>();
	public void setPcname(String pcname) {
		this.pcname = pcname;
	}
	public String getPcname() {
		return pcname;
	}
	public void setSignatures(List<Signature> signatures) {
		this.signatures = signatures;
	}
	public List<Signature> getSignatures() {
		return signatures;
	}
	
}
