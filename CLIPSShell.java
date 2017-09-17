import java.util.*;


public class CLIPSShell {
	public static List<Rule> rulesList;
	public static List<Fact> factsList;

	public CLIPSShell() {
	}

	public void makeDataStructure(String input) {
		if (input.contains("Teach")) {
			String[] inputArr = input.split(" ");
			// if (inputArr.length == 5) {

			// } else if (inputArr.length == 4) {
			// 	if (inputArr[2].equals("=")) {

			// 	} else if (inputArr[2].equals("->")) {
			if (inputArr[2].equals("->")) {
					createNewRule(inputArr[1], inputArr[3]);
				}
			}
		// } else if (input.equals("List")) {

		// } else if (input.equals("Learn")) {

		// } else if (input.contains("Query")) {

		// } else if (input.contains("Why")) {

		// }
	}

	//
	public void createNewRule(String condition, String consequence) {
		Rule newRule = new Rule(parseByOperators(condition), parseByOperators(consequence));
		rulesList.add(newRule);
		for(int i = 0; i < parseByOperators(condition).size(); i++){
			for(int j = 0; j < factList.size(); j++) {
				if(parseByOperators(conditions).get(i).equals(factList.get(j)) factList.get(j).setConditionsOf()
			}
		}
		for(int i = 0; i < parseByOperators(consequence).size(); i++){
			
		}
	}
	
	public void createNewRootFact(String variableName, String data) {
		if(factList.contains(variableName)) {
			System.out.println("Variable name already used")
		} else {
		List<String> conditionsOf = new List<String>;
		List<String> consequentsOf = new List<String>;
		for(int i = 0; i < rulesList.size(); i++) { //iterates through rulesList
			for(int j = 0; j < rulesList.get(i).getConsequences().size(); j++) { //iterates through the consequences of a specific rule
				String symbol = rulesList.get(i).getConsequences().get(j);
				if(variableName.equals(symbol)) consequentsOf.add(rulesList.get(i)); //if this new fact is in the consequence, add the rule to consequentsOf
			}
			for(int k = 0; k < rulesList.get(i).getConditions().size(); j++) { //same as above except for conditions
				String symbol = rulesList.get(i).getConditions().get(j);
				if(variableName.equals(symbol)) conditionsOf.add(rulesList.get(i));
			}
		} //goes through the rulesList to find out conditionsOf and consequentsOf
		Fact f = new Fact(false, variableName, data, conditionsOf, consequentsOf);
		factsList.add(f);
		}
	}
	
	public void updateConditionsAndConsequents (Rule r) {
		
	}
	

	public static List<String> parseByOperators(String str) {
		List<String> accumulator = new ArrayList<>();
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '&' || str.charAt(i) == '|' || str.charAt(i) == '!') {
				if (str.charAt(i-1) == '&' || str.charAt(i-1) == '|' || str.charAt(i-1) == '!') {
					accumulator.add(""+str.charAt(i));
					s = new StringBuilder();
				} else {
					accumulator.add(s.toString());
					accumulator.add(""+str.charAt(i));
					s = new StringBuilder();
				}
			} else if (i == str.length() - 1) {
				s.append(str.charAt(i));
				accumulator.add(s.toString());
			} else {
				s.append(str.charAt(i));
			}
		}
		return accumulator;
	}
	
	public static boolean evaluateTruthValue(Rule rule) {
		return false;
	}

	public static void main(String[] args) {
	rulesList.add("p -> q")
//		Scanner sc = new Scanner(System.in);
//		while (sc.hasNext()) {
//			makeDataStructure(sc.next());
//		}
	}
}