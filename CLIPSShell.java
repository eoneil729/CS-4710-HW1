/**
 * CS 4710 HW 1
 * Pak Hin Luu - pl4me
 * Ellie O'Neil - ebo6jt
 */

import java.util.*;

public class CLIPSShell {
	public static List<Rule> rulesList = new ArrayList<Rule>();
	public static List<Fact> factsList = new ArrayList<Fact>();

	public CLIPSShell() {
	}

	public void determineInputInstruction(String input) {
		if (input.contains("Teach")) {
			String[] inputArr = input.split(" ");
			if (inputArr.length == 5) {
				createNewRootFact(inputArr[2], inputArr[4]);
			} else if (inputArr.length == 4) {
				if (inputArr[2].equals("=")) {
					teachTruthValue(inputArr[1], inputArr[3]);
				} else if (inputArr[2].equals("->")) {
					if (inputArr[2].equals("->")) {
						createNewRule(inputArr[1], inputArr[3]);
					}
				}
			}
		} else if (input.equals("List")) {
			listVariables();
		} else if (input.equals("Learn")) {
			learn();
		} else if (input.contains("Query")) {
			String[] inputArr = input.split("[\\(\\)]");
			query(inputArr[1]);
		} else if (input.contains("Why")) {
			String[] inputArr = input.split("[\\(\\)]");
			why(inputArr[1]);
		}
	}

	public void createNewRule(String condition, String consequence) {
		Rule newRule = new Rule(parseByOperators(condition), parseByOperators(consequence));
		rulesList.add(newRule);
		for(int i = 0; i < parseByOperators(condition).size(); i++){
			for(int j = 0; j < factsList.size(); j++) {
				if(parseByOperators(condition).get(i).equals(factsList.get(j)))
					factsList.get(j).setConditionsOf(rulesList);
			}
		}
		for(int i = 0; i < parseByOperators(consequence).size(); i++) {

		}
	}

	/**
	 * Need to fix so it differentiates between root and learned variables
	 * - I think this should happen in this function
	 */
	public void createNewRootFact(String variableName, String data) {
		if(factsList.contains(variableName)) {
			System.out.println("Variable name already used");
		} else {
			List<Rule> conditionsOf = new ArrayList<Rule>();
			List<Rule> consequentsOf = new ArrayList<Rule>();
			for(int i = 0; i < rulesList.size(); i++) { //iterates through rulesList
				for(int j = 0; j < rulesList.get(i).getConsequences().size(); j++) { //iterates through the consequences of a specific rule
					String symbol = rulesList.get(i).getConsequences().get(j);
					if(variableName.equals(symbol)) consequentsOf.add(rulesList.get(i)); //if this new fact is in the consequence, add the rule to consequentsOf
				}
				for(int k = 0; k < rulesList.get(i).getConditions().size(); k++) { //same as above except for conditions
					String symbol = rulesList.get(i).getConditions().get(k);
					if(variableName.equals(symbol)) conditionsOf.add(rulesList.get(i));
				}
			} //goes through the rulesList to find out conditionsOf and consequentsOf
			Fact f = new Fact(false, variableName, data, conditionsOf, consequentsOf);
			factsList.add(f);
		}
	}

	public void updateConditionsAndConsequents (Rule r) {

	}

	/**
	 * Sets the truth value of a root variable based on user input and calls getAllSubconditions
	 * to reset all learned variables that are subconditions of the root variable to false
	 */
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

	/**
	 * Recursive function used in teachTruthValue() to gather all the subconditions of a condition
	 * Sets these subconditions to false because they are learned variables of a root variable that
	 * we have changed the truth value of
	 */
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

	/**
	 * Takes in a String of variables and operations and parses out each individual condition/consequence
	 * in order to add them to rulesList and factsList
	 */
	public static List<String> parseByOperators(String str) {
		List<String> accumulator = new ArrayList<String>();
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '&' || str.charAt(i) == '|' || str.charAt(i) == '!') {
				if (str.charAt(i-1) == '&' || str.charAt(i-1) == '|' || str.charAt(i-1) == '!') {
					accumulator.add("" + str.charAt(i));
					s = new StringBuilder();
				} else {
					accumulator.add(s.toString());
					accumulator.add("" + str.charAt(i));
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

	/**
	 * List all variables, facts, and rules that the user has inputted and the system has learned
	 */
	public void listVariables() {
		System.out.println("Root Variables:");
		if (!factsList.isEmpty()) {
			for (Fact f : factsList) {
				if (f.getIsRoot())
					System.out.println("\t" + f.getVariableName() + " = " + f.getData());
			}
		}
		System.out.println("Learned Variables:");
		if (!factsList.isEmpty()) {
			for (Fact f : factsList) {
				if (!f.getIsRoot())
					System.out.println("\t" + f.getVariableName() + " = " + f.getData());
			}
		}
		System.out.println("Facts:");
		if (!factsList.isEmpty()) {
			for (Fact f : factsList) {
				if (f.getIsRoot())
					System.out.println("\t" + f.getVariableName());
			}
		}
		System.out.println("Rules:");
		if (!rulesList.isEmpty()) {
			for (Rule r : rulesList) {
				for (String sCond : r.getConditions())
					System.out.print("\t" + sCond + " ");
				System.out.print(" -> ");
				for (String sCons : r.getConsequences())
					System.out.print(sCons + " ");
			}
		}
	}

	public void learn() {

	}

	public void query(String expression) {
		System.out.println(expression);
	}

	public void why(String expression) {
		System.out.println(expression);
	}

	public static boolean evaluateTruthValue(Rule rule) {
		return false;
	}

	public static void main(String[] args) {
		CLIPSShell shell = new CLIPSShell();
		Scanner sc = new Scanner(System.in);
		String line = "";
		while (sc.hasNextLine() && !(line = sc.nextLine()).equals("Exit")) {
			shell.determineInputInstruction(line);
		}
	}
}