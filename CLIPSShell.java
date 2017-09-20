import java.util.*;


public class CLIPSShell {
	public static List<Rule> rulesList;
	public static List<Fact> factsList;

	public CLIPSShell() {
	}

	public void determineInputInstruction(String input) {
		if (input.contains("Teach")) {
			String[] inputArr = input.split(" ");
			// if (inputArr.length == 5) {

			// } else if (inputArr.length == 4) {
			 	if (inputArr[2].equals("=")) {
					teachTruthValue(inputArr[1], inputArr[3]);
			// 	} else if (inputArr[2].equals("->")) {
			if (inputArr[2].equals("->")) {
					createNewRule(inputArr[1], inputArr[3]);
				}
			}
		} else if (input.equals("List")) {
			listVariables();
		}
		// } else if (input.equals("Learn")) {

		// } else if (input.contains("Query")) {

		// } else if (input.contains("Why")) {


	}

	public void createNewRule(String condition, String consequence) { //put in everything left of -> and right of ->
		Rule newRule = new Rule(parseByOperators(condition), parseByOperators(consequence)); 
		rulesList.add(newRule); //add this rule
		for(int i = 0; i < parseByOperators(condition).size(); i++){ //going through all the facts that is listed in the condition
			for(int j = 0; j < factList.size(); j++) { //going through all the facts
				if(parseByOperators(conditions).get(i).equals(factList.get(j)) factList.get(j).addToConditionsOf(newRule);
				//if a certain symbol in the condition is in the the fact list, add this new rule into that rule's conditions of list
			}
		}
		for(int i = 0; i < parseByOperators(consequence).size(); i++){ //going through all the facts listed in the consequence
			for(int j = 0; j < factList.size(); j++) { //going through all the facts
				if(parseByOperators(consequence).get(i).equals(factList.get(j)) factList.get(j).addToConsequentsOf(newRule);
				//if a certain symbol in the consequence is in the fact list, add this new rule into this fact's consequentsof list 
			}
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

	public void teachTruthValue(String root_var, String bool) {
		for (int i = 0; i < factsList.size(); i++) {
			if (factsList.get(i).getVariableName().equals(root_var) && !factsList.get(i).getIsRoot()) {
				System.out.println("You cannot set a learned variable directly");
			}
			if (factsList.get(i).getVariableName().equals(root_var) && factsList.get(i).getIsRoot()) {
				if (bool.equals("true")) {
					factsList.get(i).setTruthValue(true);
				} else if (bool.equals("false")) {
					factsList.get(i).setTruthValue(false);
				}
				getAllSubconditions(factsList.get(i));
			}
		}
	}

	public void getAllSubconditions(Fact f) {
		if (f.getConditionsOf().isEmpty()) {
			return;
		} else {
			for (int i = 0; i < f.getConditionsOf().size(); i++) {
				List<String> consequenceList = f.getConditionsOf().get(i).getConsequences();
				for (int j = 0; j < factsList.size(); j++) {
					for (int k = 0; k < consequenceList.size(); k++) {
						if (factsList.get(j).equals(consequenceList.get(k)))
							factsList.get(j).setTruthValue(false);
					}
				}
			}
			getAllSubconditions(f);
		}
	}

	public static List<String> parseByOperators(String str) {
		List<String> accumulator = new ArrayList<String>();
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

	public void listVariables() {
		System.out.println("Root Variables:");
		if (!factsList.isEmpty()) {
			for (Fact f : factsList) {
				if (f.getIsRoot())
					System.out.println(f.getVariableName() + " = " + f.getData());
			}
		}
		System.out.println("Learned Variables:");
		if (!factsList.isEmpty()) {
			for (Fact f : factsList) {
				if (!f.getIsRoot())
					System.out.println(f.getVariableName() + " = " + f.getData());
			}
		}
		System.out.println("Facts:");
		if (!factsList.isEmpty()) {
			for (Fact f : factsList) {
				if (f.getIsRoot())
					System.out.println(f.getVariableName());
			}
		}
		System.out.println("Rules:");
		if (!rulesList.isEmpty()) {
			for (Rule r : rulesList) {
				for (String sCond : r.getConditions())
					System.out.print(sCond + " ");
				System.out.print(" -> ");
				for (String sCons : r.getConsequences())
					System.out.print(sCons + " ");
			}
		}
	}
	
	public static boolean evaluateTruthValue(Rule rule) {
		return false;
	}

	public static void main(String[] args) {
//		CLIPSShell shell = new CLIPSShell();
//		String fun = "F!!D";
//		List<String> fun2 = parseByOperators(fun);
//		for(int i = 0; i < fun2.size(); i++){
//			System.out.println(i+": " + fun2.get(i));
//		}
//		Scanner sc = new Scanner(System.in);
//		while (sc.hasNext()) {
//			shell.determineInputInstruction(sc.next());
//			break;
//		}
//	}
		
		Fact f = new Fact(true, f, "fun",,,true);
		
//		public Fact(boolean truthValue,
//				String variableName,
//				String data,
//				List<Rule> conditionsOf,
//				List<Rule> consequentsOf,
//				boolean isRoot)
}