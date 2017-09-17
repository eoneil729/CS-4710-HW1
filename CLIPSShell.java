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
		//TODO: update conditions and consequents of the facts affected
	}
	
	public void createNewRootFact(String variableName, String data) {
		Fact f = new Fact(false, variableName, data,  )
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
		String fun = "F!!D";
		List<String> fun2 = parseByOperators(fun);
		for(int i = 0; i < fun2.size(); i++){
			System.out.println(i+": " + fun2.get(i));
		}
//		Scanner sc = new Scanner(System.in);
//		while (sc.hasNext()) {
//			makeDataStructure(sc.next());
//		}
	}
}