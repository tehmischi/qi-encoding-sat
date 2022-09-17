import java.util.List;

public record Rule(Literal head, List<Literal> body) {

    @Override
    public String toString() {
        String headString;
        StringBuilder bodyString = new StringBuilder();
        headString = head + " <- ";
        body.forEach(x -> bodyString.append(x).append(" & "));
        int bodySize = bodyString.length();
        return headString + bodyString.substring(0, bodySize - 2);
    }
}
