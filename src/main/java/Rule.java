import java.util.List;

public record Rule(Literal head, List<Literal> body) {

    @Override
    public String toString() {
        String headString;
        StringBuilder bodyString = new StringBuilder();
        String headVariable = head.toString();
        boolean negativeHead = headVariable.startsWith("n");
        headString = negativeHead?headVariable.replaceFirst("n", "-"):headVariable;
        headString += " <- ";
        body.forEach(bodyLiteral -> {
            String bodyLiteralString = bodyLiteral.toString();
            boolean isNegative = bodyLiteralString.startsWith("n");
            String returnString = isNegative?bodyLiteralString.replaceFirst("n", "-"):bodyLiteralString;
            bodyString.append(returnString).append(" & ");
        });
        int bodySize = bodyString.length();
        return headString + bodyString.substring(0, bodySize - 2);
    }
}
