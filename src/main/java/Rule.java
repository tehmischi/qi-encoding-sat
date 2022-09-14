import java.util.ArrayList;
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
        headString = "Head ist: " + head + "\n";
        bodyString.append("Body ist: ");
        body.forEach(x -> {
            bodyString.append(x+ " ");
        });
        return headString + bodyString.toString();
    }
}
