package jaist.info.aspectj.nataly2.metamodel;

import java.util.LinkedList;
import java.util.List;

public class TPattern {

	private String pointcutName;
	private List<TPatternPath> kHTPattern=new LinkedList<TPatternPath>();
	public void setPointcutName(String pointcutName) {
		this.pointcutName = pointcutName;
	}
	public String getPointcutName() {
		return pointcutName;
	}

	public void setKHeadTPattern(List<TPatternPath> kppattern) {
		this.kHTPattern = kppattern;
	}
	public List<TPatternPath> getKHeadTPattern() {
		return kHTPattern;
	}
	
	
}
