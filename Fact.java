import java.util.*;

public class Fact {
	private boolean truthValue;
	private String variableName;
	private String data;
	private List<Rule> conditionsOf;
	private List<Rule> consequentsOf;
	private boolean isRoot;

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
//hello
	public boolean getTruthValue() {
		return truthValue;
	}

	public void setTruthValue() {
		this.truthValue = truthValue;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName() {
		this.variableName = variableName;
	}

	public String getData() {
		return data;
	}

	public void setData() {
		this.data = data;
	}

	public List<Rule> getConditionsOf() {
		return conditionsOf;
	}

	public void setConditionsOf() {
		this.conditionsOf = conditionsOf;
	}

	public List<Rule> getConsequentsOf() {
		return consequentsOf;
	}

	public void setConsequentsOf() {
		this.consequentsOf = consequentsOf;
	}

	public boolean getIsRoot() {
		return isRoot;
	}

	public void setIsRoot() {
		this.isRoot = isRoot;
	}
	
}