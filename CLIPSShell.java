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
				String leftInput = inputArr[0].substring(inputArr[0].lastIndexOf("Teach")+1);
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
				//System.out.println(f.toString());
				factsList.add(f);
			} else if (isRoot.equals("-L")){
				f = new Fact(false, variableName, data, conditionsOf, consequentsOf, false);
				//System.out.println(f.toString());
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

//	/**
//	 * Preserves order of operations
//	 */
//	public List<String> addParentheses(List<String> expression) {
//		for (int i = 0; i < expression.size(); i++) {
//			if (expression.get(i).equals("!")) {
//				for (int j = 0; j < factsList.size(); j++) {
//					if (factsList.get(j).getVariableName().equals(expression.get(i + 1))) {
//						expression.add(i, "(");
//						expression.add(i + 3, ")");
//					}
//				}
//			}
//		}
//		for (int i = 0; i < expression.size(); i++) {
//			boolean leftIsFact = false;
//			boolean rightIsFact = false;
//			if (expression.get(i).equals("&") || expression.get(i).equals("|")) {
//				if (isExistingFact(expression.get(i - 1))) {
//					leftIsFact = true;
//				}
//				if (isExistingFact(expression.get(i + 1))) {
//					rightIsFact = true;
//				}
//			}
//			if (leftIsFact && rightIsFact) {
//				expression.add(i - 1, "(");
//				expression.add(i + 3, ")");
//				i++;
//			}
//			if (leftIsFact && !rightIsFact) {
//				expression.add(i - 1, "(");
//				int count = 0;
//				int j = i + 2;
//				do {
//					if (expression.get(j).equals("("))
//						count++;
//					if (expression.get(j).equals(")"))
//						count--;
//					j++;
//				} while (count > 0);
//				i++;
//			}
////			if (!leftIsFact && rightIsFact) {
////				expression.add(i + 1, "(");
////				int count = 0;
////				int j = i - 2;
////				do {
////					if (expression.get(j).equals("("))
////						count++;
////					if (expression.get(j).equals(")"))
////						count--;
////					j--;
////				} while (count > 0);
////				i++;
////			}
//		}
//		for (String s : expression)
//			System.out.print(s);
//		return expression;
//	}

	public boolean evaluate(List<String> expression) {
		expression.add(0, "(");
		expression.add(expression.size(), ")");
//		expression = addParentheses(expression);
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

	// fix setting learnedSomething to false problem
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

	public void query(List<String> expression) {
		//call query (which returns truth value and explanation)
		//extract truth value and return it
//		List<String> truthAndExplanation = why(expression);

		//should output the truth value
//		System.out.println(truthAndExplanation.get(0));
	}

	public String why(List<String> expression) {
		//backwards chaining
		//once you hit root, print everything on path in reverse order that you went
		//so have to keep track of path
		//no chance of going down wrong path bc it will just result in a false answer
		//add truth value and explanation - will add in reverse order so reverse and print at the end
		//recursive
		currentPath.clear();
		String truthValue = backwardsChaining(expression);
		System.out.println(truthValue);
//		for (Rule r : currentPath)
//		System.out.println(r);


//		truthAndExplanation.add("I KNOW THAT" + cond.getData());
//		truthAndExplanation.add("BECAUSE" + cond.getData() + "I KNOW THAT" + cons.getData());
//		truthAndExplanation.add("I THUS KNOW THAT" + cond.getData());
//		truthAndExplanation.add("I KNOW IT IS NOT TRUE THAT" + cond.getData());
//		truthAndExplanation.add("BECAUSE IT IS NOT TRUE THAT" + cond.getData() + "I CANNOT PROVE THAT" + cons.getData());
//		truthAndExplanation.add("THUS I CANNOT PROVE THAT" + cond.getData());

//		Collections.reverse(truthAndExplanation);
//		for (String retVal : truthAndExplanation)
//			System.out.println(retVal);
//		return truthAndExplanation;
		return truthValue;
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

	public List<Rule> currentPath = new ArrayList<Rule>();

	public String backwardsChaining(List<String> expression) {
		for (int i = 0; i < expression.size(); i++) {
		List<String> factInCondOfRuleString;
				for (Fact partOfExpression : factsList) {
					if(i ==1) System.out.println(partOfExpression.getVariableName());
					if (partOfExpression.getVariableName().equals(expression.get(i))) { //if expression is a fact
						if (partOfExpression.getIsRoot()) {
							if(expression.size() == 1) {
								if (partOfExpression.getTruthValue()) {
									return "true";
								} else {
									return "false";
								}
							}else {
								if (partOfExpression.getTruthValue()) {
									expression.set(expression.indexOf(partOfExpression.getVariableName()), "true");
								} else {
									expression.set(expression.indexOf(partOfExpression.getVariableName()), "false");
								}
							}
						} else {
							List<Rule> conseqList = partOfExpression.getConsequentsOf(); //then get the consequents of that fact
							for (Rule ruleInConseqList : conseqList) { //go through the consequents of the fact and find the conditions
								factInCondOfRuleString = ruleInConseqList.getConditions(); //this is the condition of the rule
								for (Fact factInCondOfRule : factsList) {
									for (String condition : factInCondOfRuleString) {
										if (factInCondOfRule.getVariableName().equals(condition)) {
//											if (partOfExpression.getIsRoot()) { //identify the symbols in condition and see isRoot
//												currentPath.add(ruleInConseqList); //if it is a root then add it to the path
//												System.out.println(factInCondOfRule.getVariableName() + ": " + factInCondOfRule.getTruthValue());
//												if (factInCondOfRule.getTruthValue()) {
//													expression.set(expression.indexOf(factInCondOfRule.getVariableName()), "true");
//												} else {
//													expression.set(expression.indexOf(factInCondOfRule.getVariableName()), "false");
//												}
//												//replace symbol with a truth value
//											} else {
												List<String> pak = new ArrayList<String>();
												pak.add(condition);
												expression.set(i,backwardsChaining(pak));
											//}
										}
									}
								}
							}
						}
					}
				}
			}
		int countFacts = 0;
		System.out.println(expression);
//		int countRoots = 0;
		for (int i = 0; i < expression.size(); i++) { //tests if everything in the expression is a root
			if(expression.get(i).equals("true") || expression.get(i).equals("false") ||
					expression.get(i).equals("&") || expression.get(i).equals("|") ||
					expression.get(i).equals("!") || expression.get(i).equals("(") || expression.get(i).equals(")"))
				countFacts++;
		}

		if (countFacts == expression.size()) { //if everything in the expression is a root then evaluate
			if(evaluate(expression)) return "true";
			else return "false";
		}
		return "bad";
	}

	public static void main(String[] args) {
		CLIPSShell shell = new CLIPSShell();
		Scanner sc = new Scanner(System.in);
		String line = "";
//		while (sc.hasNextLine() && !(line = sc.nextLine()).equals("Exit")) {
//			shell.determineInputInstruction(line);
//		}

		shell.createNewFact("-R", "p", "a");
		shell.createNewFact("-L", "q", "a");
		shell.createNewFact("-L", "r", "a");

		shell.createNewRule("p", "q");
		shell.createNewRule("q", "r'");


		shell.teachTruthValue("p", "true");
		//shell.teachTruthValue("q", "true");

//		shell.createNewFact("-L","r", "vam");
//		shell.createNewRule("q", "r");


		//shell.createNewRule("r", "p");

//		for(Fact f : factsList) System.out.println(f.getVariableName() + ": " + f.getTruthValue());
//		shell.learn();
//		System.out.println("\n");
//		for(Fact f : factsList) System.out.println(f.getVariableName() + ": " + f.getTruthValue());
		//shell.listVariables();
		List<String> list = new ArrayList<String>();
		//list.add("p");
		//list.add("&");
//		list.add("true");

		list.add("p");
		list.add("&");
		list.add("q");


		//list.add("r");

		//System.out.println(shell.evaluate(list));

		System.out.println(shell.backwardsChaining(list));

//		System.out.println(rulesList.get(0));
//		System.out.println(shell.evaluate(parseByOperators("true")));
//		shell.listVariables();


	}
}