package jaist.info.aspectj.nataly2.seed;

import java.util.LinkedList;
import java.util.List;

public class Seed {

	private List<String> seedlist=new LinkedList<String>();
	private String category;
	public void setSeedList(List<String> seedlist) {
		this.seedlist = seedlist;
	}
	public List<String> getSeedList() {
		return seedlist;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategory() {
		return category;
	}
	
}
