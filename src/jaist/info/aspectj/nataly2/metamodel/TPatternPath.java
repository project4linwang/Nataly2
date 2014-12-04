package jaist.info.aspectj.nataly2.metamodel;

import java.util.LinkedList;
import java.util.List;

public class TPatternPath {

	private String data;
	private List<String> paths=new LinkedList<String>();
	public void setData(String data) {
		this.data = data;
	}
	public String getData() {
		return data;
	}
	public void setPaths(List<String> paths) {
		this.paths = paths;
	}
	public List<String> getPaths() {
		return paths;
	}
	
}
