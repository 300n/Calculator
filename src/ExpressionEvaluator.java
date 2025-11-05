
/**
 * Classe pour évaluer une fonction en un point
 */
public class ExpressionEvaluator{


    public ExpressionEvaluator(){}

    /**
     * Evalue la fonction en un point x
     * @param expr Expression littérale
     * @param x point x
     * @return valeur de f en x
     */
    public double evaluate(String expr, double x) {
        expr = expr.replaceAll(" ", "").replace("π", String.valueOf(Math.PI));

        try {
            return parseExpression(expr, x);
        } catch (Exception e) {
            return Double.NaN;
        }
    }
    
    /**
     * Transforme l'expression littérale en expression mathématique
     * @param expr Expression littérale
     * @param x point x
     * @return valeur de f en x
     */
    private double parseExpression(String expr, double x) {
        for (int i = expr.length() - 1; i >= 0; i--) {
            if (expr.charAt(i) == '+' || expr.charAt(i) == '-') {
                if (i > 0 && !isOperator(expr.charAt(i - 1)) && expr.charAt(i - 1) != '(') {
                    String left = expr.substring(0, i);
                    String right = expr.substring(i + 1);
                    if (expr.charAt(i) == '+') {
                        return parseExpression(left, x) + parseExpression(right, x);
                    } else {
                        return parseExpression(left, x) - parseExpression(right, x);
                    }
                }
            }
        }

        for (int i = expr.length() - 1; i >= 0; i--) {
            if ((expr.charAt(i) == '*' || expr.charAt(i) == '÷' || expr.charAt(i) == '/') && !isInsideParentheses(expr, i)) {
                if (i > 0 && !isOperator(expr.charAt(i - 1)) && expr.charAt(i - 1) != '(') {
                    String left = expr.substring(0, i);
                    String right = expr.substring(i + 1);
                    if (expr.charAt(i) == '*') {
                        return parseExpression(left, x) * parseExpression(right, x);
                    } else {
                        return parseExpression(left, x) / parseExpression(right, x);
                    }
                }
            }
        }

        for (int i = expr.length() - 1; i >= 0; i--) {
            if (expr.charAt(i) == '^') {
                String left = expr.substring(0, i);
                String right = expr.substring(i + 1);
                return Math.pow(parseExpression(left, x), parseExpression(right, x));
            }
        }
        if (expr.startsWith("sin(") || expr.startsWith("cos(") || expr.startsWith("tan(")
            || expr.startsWith("log(") || expr.startsWith("exp(") || expr.startsWith("sqt(")) {

            int openIndex = expr.indexOf('(');
            int closeIndex = findMatchingParentheses(expr, openIndex);
            if (closeIndex != -1) {
                String func = expr.substring(0, openIndex);
                String inner = expr.substring(openIndex + 1, closeIndex);
                double innerValue = parseExpression(inner, x);

                return switch (func) {
                    case "sin" -> Math.sin(innerValue);
                    case "cos" -> Math.cos(innerValue);
                    case "tan" -> Math.tan(innerValue);
                    case "log" -> Math.log(innerValue);
                    case "exp" -> Math.exp(innerValue);
                    case "sqt" -> Math.sqrt(innerValue); 
                    default -> throw new IllegalArgumentException();
                };
                
            }
        }

        if (expr.startsWith("(") && expr.endsWith(")")) {
            return parseExpression(expr.substring(1, expr.length() - 1), x);
        }

        if (expr.equals("x")) {
            return x;
        }

        return Double.parseDouble(expr);
    }

    /**
     * Détermine si une expr est entre une paire de parenthèse
     * @param expr Expression littérale
     * @param index index de la parenthèse ouverte + 1
     * @return true or false selon la position de l'expression
     */
    private boolean isInsideParentheses(String expr, int index) {
        int depth = 0;
        for (int i = 0; i < index; i++) {
            if (expr.charAt(i) == '(') depth++;
            if (expr.charAt(i) == ')') depth--;
        }
        return depth > 0;
    }

    /**
     * Trouve l'indice de la parenthèse fermée correspondant à la parenthèse ouverte
     * @param expr Expression littérale
     * @param openIndex indice de la parenthèse ouverte
     * @return indice de la parenthèse fermée
     */
    private int findMatchingParentheses(String expr, int openIndex) {
        int count = 1;
        for (int i = openIndex + 1; i < expr.length(); i++) {
            if (expr.charAt(i) == '(')
                count++;
            if (expr.charAt(i) == ')') {
                count--;
                if (count == 0)
                    return i;
            }
        }
        return -1;
    }

    /**
     * Trouve si le caractère est un opérateur 
     * @param c caractère
     * @return c est un opérateur ?
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '÷' || c == '/' || c == '^';
    }
}