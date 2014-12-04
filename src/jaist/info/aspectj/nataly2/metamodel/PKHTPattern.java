package jaist.info.aspectj.nataly2.metamodel;

import java.util.List;

public class PKHTPattern {


	private AbstractRelationGraph keyheadTP;

	private String pointcutname;

	public void setKeypointTP(AbstractRelationGraph keypointTP) {
		this.keyheadTP = keypointTP;
	}
	public AbstractRelationGraph getKeyHeadTP() {
		return keyheadTP;
	}

	public void setPointcutName(String pointcutname) {
		this.pointcutname = pointcutname;
	}
	public String getPointcutName() {
		return pointcutname;
	}
}
