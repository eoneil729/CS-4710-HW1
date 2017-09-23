import java.util.*;

public class Fact {
	private boolean truthValue;
	private String variableName;
	private String data;
	private List<Rule> conditionsOf;
	private List<Rule> consequentsOf;
	private boolean isRoot;

	public Fact() {

	}

	public Fact(boolean truthValue,
				String variableName,
				String data,
				List<Rule> conditionsOf,
				List<Rule> consequentsOf,
				boolean isRoot) {
		this.truthValue = truthValue;
		this.variableName = variableName;
		this.data = data;
		this.conditionsOf = conditionsOf;
		this.consequentsOf = consequentsOf;
		this.isRoot = isRoot;
	}

	public boolean getTruthValue() {
		return truthValue;
	}

	public void setTruthValue(boolean truthValue) {
		this.truthValue = truthValue;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public List<Rule> getConditionsOf() {
		return conditionsOf;
	}

	public void setConditionsOf(List<Rule> conditionsOf) {
		this.conditionsOf = conditionsOf;
	}

	public List<Rule> getConsequentsOf() {
		return consequentsOf;
	}

	public void setConsequentsOf(List<Rule> consequentsOf) {
		this.consequentsOf = consequentsOf;
	}

	public boolean getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
	
	public void addToConditionsOf (Rule r) {
		this.conditionsOf.add(r);
	}
	
	public void addToConsequentsOf (Rule r) {
		this.consequentsOf.add(r);
	}
	
	public String toString() {
		String s = ""+ this.variableName + " = " + this.data + "\nTruth Value: " + this.truthValue + "\nisRoot: " + this.isRoot + "\nConditions of: " + this.conditionsOf + "\nConsequents of: " + this.consequentsOf;
		return s;
	}
	
}