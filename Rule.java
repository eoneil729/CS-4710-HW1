import java.util.*;

public class Rule {
	private List<String> conditions;
	private List<String> consequences;

	public Rule(List<String> conditions, List<String> consequences) {
		this.conditions = conditions;
		this.consequences = consequences;
	}

	public List<String> getConditions() {
		return conditions;
	}

	public void setConditions() {
		this.conditions = conditions;
	}

	public List<String> getConsequences() {
		return consequences;
	}

	public void setConsequences() {
		this.consequences = consequences;
	}

	public static void main(String[] args) {
		System.out.println("hi");
	}


}