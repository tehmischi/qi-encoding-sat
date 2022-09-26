public class DeclareBusinessRule {

    private final String head;
    private final String body;
    private final String template;

    public DeclareBusinessRule (String template, String head, String body){
        this.template = template;
        this.head = head;
        this.body = body;
    }

    public String getTemplate() {
        return template;
    }

    public String getBody() {
        return body;
    }

    public String getHead() {
        return head;
    }
}
