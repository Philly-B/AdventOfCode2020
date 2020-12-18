package days;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import general.Helper;

public class Day18 {

	private static final String[] examples = new String[] {
			"1 + 2 * 3 + 4 * 5 + 6",
			"1 + (2 * 3) + (4 * (5 + 6))",
			"2 * 3 + (4 * 5)",
			"5 + (8 * 3 + 9 + 3 * 4 * 3)",
			"5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))",
			"((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"
	};
	private static final int[] exampleResultsPart1 = new int[] {
			71,
			51,
			26,
			437,
			12240,
			13632
	};
	private static final int[] exampleResultsPart2 = new int[] {
			231,
			51,
			46,
			1445,
			669060,
			23340
	};


	public static void main(String[] args) {

		List<String> input = Helper.readFile("./data/days/day18p1.txt");

		solve(input, exampleResultsPart1, (c1, c2) -> 0);

		Comparator<Combiner> operationPriority = (c1, c2) -> {
			if (c1.operation.equals(Combiner.OPERATION.PLUS) || !c2.operation.equals(Combiner.OPERATION.PLUS)) {
				return -1;
			} else {
				return 1;
			}
		};
		solve(input, exampleResultsPart2, operationPriority);
	}


	private static void solve(List<String> input, int[] exampleResults, Comparator<Combiner> operationPriority) {

		runTestsPart(exampleResults, operationPriority);
		long sum = 0;
		for (String expression : input) {
			sum += solveOneEquation(expression, operationPriority);
		}
		Helper.printResultPart1(String.valueOf(sum));
	}


	private static void runTestsPart(int[] expectedResults, Comparator<Combiner> operationPriority) {

		for (int i = 0; i < examples.length; i++) {
			long result = solveOneEquation(examples[i], operationPriority);
			if (result != expectedResults[i]) {
				throw new RuntimeException("Noe noe " + result + " but should be " + expectedResults[i] + " for " + examples[i]);
			}
		}
	}


	private static long solveOneEquation(String expression, Comparator<Combiner> operationPriority) {

		String cleanedExpression = expression.replace(" ", "");
		Equation equation = new Equation(operationPriority);
		Stack<Equation> subEquation = new Stack<>();
		for (int i = 0; i < cleanedExpression.length(); i++) {
			switch (cleanedExpression.charAt(i)) {
				case '(':
					subEquation.push(equation);
					equation = new Equation();
					break;
				case ')':
					long partResult = equation.evaluate();
					equation = subEquation.pop();
					equation.addExpression(new ExpressionPart(partResult));
					break;
				case '+':
					equation.addExpression(new Combiner(Combiner.OPERATION.PLUS));
					break;
				case '*':
					equation.addExpression(new Combiner(Combiner.OPERATION.MULTIPLY));
					break;
				default:
					equation.addExpression(new ExpressionPart(Long.parseLong(cleanedExpression.charAt(i) + "")));
			}
		}
		return equation.evaluate();
	}


	private static interface Expression {

	}


	private static class Combiner implements Expression {

		public enum OPERATION {
			PLUS,
			MULTIPLY
		}


		OPERATION operation;


		public Combiner(OPERATION operation) {

			this.operation = operation;
		}


		public Expression evaluate(Expression left, Expression right) {

			ExpressionPart leftPart = (ExpressionPart) left;
			ExpressionPart rightPart = (ExpressionPart) right;
			switch (operation) {
				case PLUS:
					return new ExpressionPart(leftPart.partResult + rightPart.partResult);
				case MULTIPLY:
					return new ExpressionPart(leftPart.partResult * rightPart.partResult);
			}
			throw new IllegalArgumentException();
		}
	}


	private static class ExpressionPart implements Expression {

		long partResult = -1;


		public ExpressionPart(long partResult) {

			this.partResult = partResult;
		}
	}


	private static class Equation {

		List<Expression> expressions = new ArrayList<>();
		private static Comparator<Combiner> operationPriority;


		public Equation(Comparator<Combiner> operationPriority) {

			this();
			this.operationPriority = operationPriority;
		}


		public Equation() {
			// nothing to do
		}


		public long evaluate() {

			while (expressions.size() > 0) {
				if (expressions.size() == 1) {
					return ((ExpressionPart) expressions.get(0)).partResult;
				}
				if (expressions.size() == 3) {
					combineTwoExpressions(0);
				} else {
					ExpressionPart partToEvaluate = (ExpressionPart) expressions.get(2);
					Combiner leftComb = (Combiner) expressions.get(1);
					Combiner rightComb = (Combiner) expressions.get(3);
					if (operationPriority.compare(leftComb, rightComb) <= 0) {
						combineTwoExpressions(0);
					} else {
						combineTwoExpressions(2);
					}
				}
			}

			throw new IllegalArgumentException("We should not reach this");
		}


		private void combineTwoExpressions(int firstPartIndex) {

			expressions.set(firstPartIndex, ((Combiner) expressions.get(firstPartIndex + 1))
					.evaluate(expressions.get(firstPartIndex), expressions.get(firstPartIndex + 2)));
			expressions.remove(firstPartIndex + 2);
			expressions.remove(firstPartIndex + 1);
		}


		public void addExpression(Expression expression) {

			expressions.add(expression);
		}
	}

}
