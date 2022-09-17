import java.util.List;

public class Rule {

    private final List<Literal> body;
    private final Literal head;

    public Rule (Literal head, List body){
        this.body = body;
        this.head = head;
    }

    public Literal getHead() {
        return head;
    }

    public List<Literal> getBody() {
        return body;
    }

    @Override
    public String toString(){
        String headString;
        StringBuilder bodyString = new StringBuilder();
        headString = head + " <- ";
        body.forEach(x -> bodyString.append(x+ " & "));
        int bodySize = bodyString.length();
        return headString + bodyString.substring(0,bodySize-2);
    }
}
