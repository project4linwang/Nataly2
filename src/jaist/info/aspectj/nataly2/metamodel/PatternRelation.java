package jaist.info.aspectj.nataly2.metamodel;

public abstract class PatternRelation {

	protected String patternName;
	public PatternRelation(String name)
	{
		super();
		this.patternName=name;
	}
	public void setPatternName(String name){
		this.patternName=name;
	}
	public String getPatternName(){
		return this.patternName;
	}
	public abstract String getPatternFormat();
}
