/**
 * CS 4710 HW 1
 * Pak Hin Luu - pl4me
 * Ellie O'Neil - ebo6jt
 *
 * Discussed with Josh Peters and Ryan Dean
 *
 * Disclaimer: why function works when tested in main, but sometimes fails when read through stdin
 */

import java.util.*;

public class CLIPSShell {
	public static List<Rule> rulesList = new ArrayList<Rule>();
	public static List<Fact> factsList = new ArrayList<Fact>();

	public CLIPSShell() {
	}

	public void determineInputInstruction(String input) {
		if (input.contains("Teach")) {
			if (input.contains("=")) {
				String[] inputArr = input.split(" = ");
				String[] leftInputArr = inputArr[0].split(" ");
				if (leftInputArr.length == 3) {
					createNewFact(leftInputArr[1], leftInputArr[2], inputArr[1]);
				}
				else if (leftInputArr.length == 2) {
					teachTruthValue(leftInputArr[1], inputArr[1]);
				}
			} else if (input.contains("->")) {
				String[] inputArr = input.split(" -> ");
				String leftInput = inputArr[0].substring(inputArr[0].lastIndexOf("Teach")+6);
				createNewRule(leftInput, inputArr[1]);
			}
		} else if (input.equals("List")) {
			listVariables();
		} else if (input.equals("Learn")) {
			learn();
		} else if (input.contains("Query")) {
//			query(inputArr[1]);
			String expression = input.substring(input.lastIndexOf("Query")+1);
			List<String> inputList = parseByOperators(expression);
			query(inputList);
		} else if (input.contains("Why")) {
//			why(inputArr[1]);
			String expression = input.substring(input.lastIndexOf("Why")+1);
			List<String> inputList = parseByOperators(expression);
			List<Rule> path = new ArrayList<Rule>();
			why(inputList);
		}
	}

	public void createNewRule(String condition, String consequence) {
		Rule newRule = new Rule(parseByOperators(condition), parseByOperators(consequence));
		rulesList.add(newRule);
		for(int i = 0; i < parseByOperators(condition).size(); i++) {
			for(int j = 0; j < factsList.size(); j++) {
				if(parseByOperators(condition).get(i).equals(factsList.get(j).getVariableName()))
					factsList.get(j).addToConditionsOf(newRule);
			}
		}
		for(int i = 0; i < parseByOperators(consequence).size(); i++) {
			for(int j = 0; j < factsList.size(); j++) {
				if(parseByOperators(consequence).get(i).equals(factsList.get(j).getVariableName()))
					factsList.get(j).addToConsequentsOf(newRule);
			}
		}
	}

	public void createNewFact(String isRoot, String variableName, String data) {
		int count = 0;
		for (int k = 0; k < factsList.size(); k++) {
			if (factsList.get(k).getVariableName().equals(variableName))
				System.out.println("Variable name already used");
			else
				count++;
		}
		if (count == factsList.size()) {
			List<Rule> conditionsOf = new ArrayList<Rule>();
			List<Rule> consequentsOf = new ArrayList<Rule>();
			for(int i = 0; i < rulesList.size(); i++) { //iterates through rulesList
				for(int j = 0; j < rulesList.get(i).getConsequences().size(); j++) { //iterates through the consequences of a specific rule
					String symbol = rulesList.get(i).getConsequences().get(j);
					if(variableName.equals(symbol))
						consequentsOf.add(rulesList.get(i)); //if this new fact is in the consequence, add the rule to consequentsOf
				}
				for(int j = 0; j < rulesList.get(i).getConditions().size(); j++) { //same as above except for conditions
					String symbol = rulesList.get(i).getConditions().get(j);
					if(variableName.equals(symbol))
						conditionsOf.add(rulesList.get(i));
				}
			} //goes through the rulesList to find out conditionsOf and consequentsOf
			Fact f;
			if (isRoot.equals("-R")) {
				f = new Fact(false, variableName, data, conditionsOf, consequentsOf, true);
				factsList.add(f);
			} else if (isRoot.equals("-L")){
				f = new Fact(false, variableName, data, conditionsOf, consequentsOf, false);
				factsList.add(f);
			}

		}
	}

	/**
	 * Sets the truth value of a root variable based on user input and calls getAllSubconditions
	 * to reset all learned variables that are subconditions of the root variable to false
	 */
	public void teachTruthValue(String root_var, String bool) {
		boolean truthValueChanged = false;
		for (int i = 0; i < factsList.size(); i++) {
			if (factsList.get(i).getVariableName().equals(root_var) && !factsList.get(i).getIsRoot()) {
				System.out.println("You cannot set a learned variable directly");
			}
			if (factsList.get(i).getVariableName().equals(root_var) && factsList.get(i).getIsRoot()) {
				if (bool.equals("true") && !factsList.get(i).getTruthValue()) {
					factsList.get(i).setTruthValue(true);
					truthValueChanged = true;
				} else if (bool.equals("false")  && factsList.get(i).getTruthValue()) {
					factsList.get(i).setTruthValue(false);
					truthValueChanged = true;
				}
			}
		}
		if (truthValueChanged) {
			for (int i = 0; i < factsList.size(); i++) {
				if (!factsList.get(i).getIsRoot())
					factsList.get(i).setTruthValue(false);
			}
		}
	}

	/**
	 * Takes in a String of variables and operations and parses out each individual condition/consequence
	 * in order to add them to rulesList and factsList
	 */
	public static List<String> parseByOperators(String str) {
		List<String> accumulator = new ArrayList<String>();
		StringBuilder s = new StringBuilder();
		int m = 0;
		if(str.charAt(0) == '(') {
			accumulator.add(""+str.charAt(0));
			m++;
		}
			for (int i = m; i < str.length(); i++) {
				if (str.charAt(i) == '&' || str.charAt(i) == '|' || str.charAt(i) == '!' || str.charAt(i) == '(' || str.charAt(i) == ')') {
					if (str.charAt(i - 1) == '&' || str.charAt(i - 1) == '|' || str.charAt(i - 1) == '!' || str.charAt(i - 1) == '(' || str.charAt(i - 1) == ')') {
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
				if (f.getTruthValue() == true)
					System.out.println("\t" + f.getVariableName());
			}
		}
		System.out.println("Rules:");
		if (!rulesList.isEmpty()) {
			for (Rule r : rulesList) {
				System.out.print("\t");
				for (String sCond : r.getConditions())
					System.out.print(sCond);
				System.out.print(" -> ");
				for (String sCons : r.getConsequences())
					System.out.println(sCons);
			}
		}
	}

	public boolean evaluate(List<String> expression) {
		expression.add(0, "(");
		expression.add(expression.size(), ")");
		expression = addParentheses(expression);
		Stack<String> myStack = new Stack<String>();
		boolean truth = false;
		List<String> expressionList = new ArrayList<String>();
		for (int i = 0; i < expression.size(); i++) {
			myStack.push(expression.get(i));
			if (expression.get(i).equals(")")) {
				while (!myStack.peek().equals("(")) {
					String s = myStack.pop();
					expressionList.add(s);
				}
				expressionList.add(myStack.pop());
				int count = 0;
				if (expression.get(0).equals("(") && expression.get(2).equals((")"))) {
					for (int j = 0; j < factsList.size(); j++) {
						if (factsList.get(j).getVariableName().equals(expression.get(1))) {
							return(factsList.get(j).getTruthValue());
						}
					}
				}
				if (expressionList.contains("!")) {
					boolean rightBool = false;
					if(expressionList.get(1).equals("true")) {
						rightBool = true;
						count++;
					} else if (expressionList.get(1).equals("false")){
						rightBool = false;
						count++;
					}
					for (int j = 0; j < factsList.size(); j++) {
						if (factsList.get(j).getVariableName().equals(expressionList.get(1))) {
							count++;
							rightBool = factsList.get(j).getTruthValue();
						}
					}
					if (count == 1) {
						truth = evalNot(rightBool);
					}
				} else if (expressionList.contains("&") || expressionList.contains("|")) {
					boolean leftBool = false;
					boolean rightBool = false;
					if(expressionList.get(3).equals("true")) {
						leftBool = true;
						count++;
					} else if (expressionList.get(3).equals("false")){
						leftBool = false;
						count++;
					}
					if(expressionList.get(1).equals("true")) {
						rightBool = true;
						count++;
					} else if (expressionList.get(1).equals("false")){
						rightBool = false;
						count++;
					}
					for (int j = 0; j < factsList.size(); j++) {
						if (factsList.get(j).getVariableName().equals(expressionList.get(3))) {
							count++;
							leftBool = factsList.get(j).getTruthValue();
						}
						if (factsList.get(j).getVariableName().equals(expressionList.get(1))) {
							count++;
							rightBool = factsList.get(j).getTruthValue();
						}
					}
					if (count == 2) {
						if (expressionList.get(2).equals("&")) {
							truth = evalAnd(leftBool, rightBool);
						} else if (expressionList.get(2).equals("|")) {
							truth = evalOr(leftBool, rightBool);
						}
					}
				}
				String truthString;
				if (truth)
					truthString = "true";
				else
					truthString = "false";
				myStack.push(truthString);
			}
		}
		return truth;
	}

	public boolean evalAnd(boolean left, boolean right) { return left & right; }

	public boolean evalOr(boolean left, boolean right) { return left | right; }

	public boolean evalNot(boolean val) {
		return !val;
	}

	public void learn() {
		boolean learnedSomething;
		boolean conditionTruthValue;
		do  {
			learnedSomething = false;
			for (int i = 0; i < rulesList.size(); i++) {
				conditionTruthValue = evaluate(rulesList.get(i).getConditions());
				if (conditionTruthValue) {
					List<String> consequences = rulesList.get(i).getConsequences();
					for (int j = 0; j < consequences.size(); j++) {
						for (int k = 0; k < factsList.size(); k++) {
							if (factsList.get(k).getVariableName().equals(consequences.get(j)) && !factsList.get(k).getTruthValue()) {
								factsList.get(k).setTruthValue(true);
								learnedSomething = true;
							}
						}
					}
				}
			}
		} while (learnedSomething);
	}

	public List<Fact> deepCopyFactsLists(List<Fact> factsList, List<Fact> factsListCopy) {
		for (Fact f : factsList) {
			Fact fCopy = new Fact();
			fCopy.setTruthValue(f.getTruthValue());
			fCopy.setVariableName(f.getVariableName());
			fCopy.setData(f.getData());
			fCopy.setConditionsOf(f.getConditionsOf());
			fCopy.setConsequentsOf(f.getConsequentsOf());
			fCopy.setIsRoot(f.getIsRoot());
			factsListCopy.add(fCopy);
		}
		return factsListCopy;
	}

	public List<Rule> deepCopyRulesLists(List<Rule> rulesList, List<Rule> rulesListCopy) {
		for (Rule r : rulesList) {
			Rule rCopy = new Rule();
			rCopy.setConditions(r.getConditions());
			rCopy.setConsequences(r.getConsequences());
			rulesListCopy.add(rCopy);
		}
		return rulesListCopy;
	}

	public boolean query(List<String> expression) {
		List<Fact> factsListCopy = new ArrayList<Fact>();
		factsListCopy = deepCopyFactsLists(factsList, factsListCopy);
		List<Rule> rulesListCopy = new ArrayList<Rule>();
		rulesListCopy = deepCopyRulesLists(rulesList, rulesListCopy);
		learn();
		boolean truth = evaluate(expression);
		System.out.println(truth);
		factsList.clear();
		factsList = deepCopyFactsLists(factsListCopy, factsList);
		rulesList.clear();
		rulesList = deepCopyRulesLists(rulesListCopy, rulesList);
		return truth;
	}

	public boolean isExistingFact(String expression) {
		boolean ans = false;
		for (Fact f: factsList) {
			if (f.getVariableName().equals(expression)) {
				ans = true;
			}
		}
		return ans;
	}

	public List<List<String>> rulePath = new ArrayList<List<String>>();

	public void backwardsChaining(List<String> expression) {
		boolean foundNewRule = false;
		List<String> chainList = new ArrayList<String>();
		List<Rule> alreadySeen = new ArrayList<Rule>();
		for (String exp : expression) {
			chainList = new ArrayList<String>();
			alreadySeen = new ArrayList<Rule>();
			if (exp.equals("(") || exp.equals(")") || exp.equals("!") || exp.equals("&") || exp.equals("|")) {
				if (exp.equals("!") || exp.equals("&") || exp.equals("|")) {
					chainList = new ArrayList<String>();
					chainList.add(exp);
					rulePath.add(chainList);
				}
			} else {
				do {
					foundNewRule = false;
					for (Rule rule : rulesList) {
						for (Fact fact : factsList) {
							if (!alreadySeen.contains(rule) && fact.getVariableName().equals(exp) && (rule.getConditions().contains(fact.getVariableName()) || rule.getConsequences().contains(fact.getVariableName()))) {
								foundNewRule = true;
								alreadySeen.add(rule);
								chainList.add(rule.toString());
								List<String> expConditions = rule.getConditions();
								exp = expConditions.get(expConditions.lastIndexOf("(")+1);
							}
						}
					}
				} while (foundNewRule);
				Collections.reverse(chainList);
				rulePath.add(chainList);
			}
		}
	}

	/**
	 * Preserves order of operations
	 */
	public List<String> addParentheses(List<String> expression) {
		for (int i = 0; i < expression.size(); i++) {
			if (expression.get(i).equals("!")) {
				for (int j = 0; j < factsList.size(); j++) {
					if (factsList.get(j).getVariableName().equals(expression.get(i + 1))) {
						expression.add(i, "(");
						expression.add(i + 3, ")");
						i++;
					}
				}
			}
		}
		for (int i = 0; i < expression.size(); i++) {
			boolean leftIsFact = false;
			boolean rightIsFact = false;
			if (expression.get(i).equals("&") || expression.get(i).equals("|")) {
				if (isExistingFact(expression.get(i - 1))) {
					leftIsFact = true;
				}
				if (isExistingFact(expression.get(i + 1))) {
					rightIsFact = true;
				}
			}
			if (leftIsFact && rightIsFact) {
				expression.add(i - 1, "(");
				expression.add(i + 3, ")");
				i++;
			}
			if (leftIsFact && !rightIsFact) {
				expression.add(i - 1, "(");
				i++;
				int count = 0;
				int j = i + 1;
				do {
					if (expression.get(j).equals("(")) {
						count++;
					}
					if (expression.get(j).equals(")")) {
						count--;
					}
					j++;
				} while (count > 0);
				expression.add(j, ")");
			}
			if (!leftIsFact && rightIsFact) {
				expression.add(i + 2, ")");
				int count = 0;
				int j = i-1;
				do {
					if (expression.get(j).equals(")")) {
						count++;
					}
					if (expression.get(j).equals("(")) {
						count--;
					}
					if(j >0) j--;
				} while (count > 0);
				expression.add(j, "(");
				i++;
			}
		}
		return expression;
	}

	public Rule searchForRule (String s) {
		Rule ans = new Rule();
		for (Rule r: rulesList) {
			if (r.toString().equals(s)) {
				ans = r;
			}
		}
		return ans;
	}

	public Fact searchForFacts (String s) {
		Fact ans = new Fact();
		for (Fact f: factsList) {
			if (f.getVariableName().equals(s)) {
				ans = f;
			}
		}
		return ans;
	}

	public boolean isExistingRule(String expression) {
		boolean ans = false;
		for (Rule r: rulesList) {
			if (r.toString().equals(expression)) {
				ans = true;
			}
		}
		return ans;
	}

	public void why(List<String> expression) {

		List<Fact> factsListCopy = new ArrayList<Fact>();
		factsListCopy = deepCopyFactsLists(factsList, factsListCopy);
		List<Rule> rulesListCopy = new ArrayList<Rule>();
		rulesListCopy = deepCopyRulesLists(rulesList, rulesListCopy);
		learn();
		boolean ellie = query(expression);
		System.out.println(ellie);
		backwardsChaining(expression);
		boolean isRoot = false;
		if (!rulePath.isEmpty()) {
			for (List<String> currentPath : rulePath) {
				if (currentPath.get(0).equals("&")) System.out.println("AND");
				for (String currentRuleString : currentPath) {
					if (isExistingRule(currentRuleString)) {
						Rule currentRule = searchForRule(currentRuleString);
						if (!currentRule.getConditions().isEmpty()) {
							for (String factString : currentRule.getConditions()) {
								Fact currentFact = searchForFacts(factString);
								if (currentFact.getTruthValue()) {
									System.out.println("I KNOW THAT " + currentFact.getData());
									isRoot = currentFact.getIsRoot();
								} else if (isExistingFact(currentFact.getVariableName())) {
									System.out.println("I KNOW IT IS NOT TRUE THAT " + currentFact.getData());
									isRoot = currentFact.getIsRoot();
								}


							}
						}
						if (!isRoot) {
							System.out.print("BECAUSE ");
							List<String> conditions = currentRule.getConditions();
							for (String currentFactString2 : conditions) {
								if (currentFactString2.equals("&")) {
									System.out.println("\n AND ");
								}
								if (currentFactString2.equals("|")) {
									System.out.print(" OR ");
								}
								Fact currentFact = searchForFacts(currentFactString2);
								if (currentFact.getTruthValue()) {
									System.out.print("I KNOW THAT " + currentFact.getData());
								} else if (isExistingFact(currentFact.getVariableName())) {
									System.out.print("I KNOW IT IS NOT TRUE THAT " + currentFact.getData());
								}
							}
							List<String> consequents = currentRule.getConsequences();
							System.out.print(" I KNOW THAT ");
							for (String currentFactString2 : consequents) {
								if (currentFactString2.equals("&")) {
									System.out.println("\n AND ");
								}
								if (currentFactString2.equals("|")) {
									System.out.print(" OR ");
								}
								Fact currentFact = searchForFacts(currentFactString2);
								if (currentFact.getTruthValue()) {
									System.out.print(currentFact.getData());
								} else if (isExistingFact(currentFact.getVariableName())) {
									System.out.print(currentFact.getData());
								}
							}
						}
						isRoot = false;

					}
				}

			}
		}
			boolean answer = ellie;
			if(answer) {
				System.out.print("\nI THUS KNOW THAT (");
				for(String fact : expression) {
				if(isExistingFact(fact)) {
					System.out.print(searchForFacts(fact).getData());
				} else if (fact.equals("&")) {
					System.out.print(" AND ");
				} else if (fact.equals("|")) {
					System.out.print(" OR ");
				}
			} }else {
				System.out.print("\nTHUS I CANNOT PROVE THAT (");
				for (String fact : expression) {
					if (isExistingFact(fact)) {
						System.out.print(searchForFacts(fact).getData());
					} else if (fact.equals("&")) {
						System.out.print(" AND ");
					} else if (fact.equals("|")) {
						System.out.print(" OR ");
					}
				}
		}
		System.out.print(")");




		factsList.clear();
		factsList = deepCopyFactsLists(factsListCopy, factsList);
		rulesList.clear();
		rulesList = deepCopyRulesLists(rulesListCopy, rulesList);

		rulePath.clear();
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