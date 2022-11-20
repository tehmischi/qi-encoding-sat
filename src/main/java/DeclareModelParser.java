import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class DeclareModelParser implements InputFileParser{

    private final String filePath;
    public DeclareModelParser (String filePath){
        this.filePath = filePath;
    }

    public RuleBase parse () {
        HashSet<DeclareBusinessRule> usefulRules = new HashSet<>();
        if (filePath == null || !new File(filePath).isFile()) {
            System.err.println(filePath + " is not a valid file!");
        } else {
            try {
                Scanner scanner = new Scanner(new File(filePath));
                scanner.useDelimiter("\n");
                ArrayList<String[]> rules = new ArrayList<>();
                while(scanner.hasNext()){
                    String rule = scanner.next();
                    if (!rule.trim().isEmpty()){
                        String[] seperated = rule.split(";");
                        rules.add(seperated);
                    }
                }
                rules.forEach(rule ->{
                    if (rule.length > 3){
                        if (matches(rule[1])){
                            usefulRules.add(new DeclareBusinessRule(rule[1], rule[2], rule[3]));
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(filePath + " not found.");
            }
        }
        return getRuleBase(usefulRules);
    }

    private boolean matches (String template){
        return template.equals("'NotResponse'" ) || template.equals("'Response'");
    }

    private RuleBase getRuleBase (HashSet<DeclareBusinessRule> usefulRules){
        RuleBase returnBase = new RuleBase();
        usefulRules.forEach(declareRule -> {
            boolean isResponse = declareRule.template().equals("'Response'");
            Literal body = new Literal(declareRule.body(), false);
            LinkedList<Literal> bodyList = new LinkedList<>();
            bodyList.add(body);
            Literal head;
            if (isResponse) {
                head = new Literal(declareRule.head(), false);
            } else {
                head = new Literal(declareRule.head(), true);
            }
            Rule ruleToAdd = new Rule(head, bodyList);
            returnBase.addRuleToBase(ruleToAdd);
        });
        return returnBase;
    }

}


