import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class DeclareModelParser {


    public DeclareModelParser (){
    }

    public RuleBase parseCSV (String filePath) throws FileNotFoundException {
        HashSet<DeclareBusinessRule> usefulRules = new HashSet<>();
        if (filePath == null || !new File(filePath).isFile()) {
            System.err.println(filePath + " is not a valid file!");
        } else {
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
        }
        return getRuleBase(usefulRules);
    }

    private boolean matches (String template){
        boolean matches = template.equals("'Response'") || template.equals("'NotResponse'");
        return matches;
    }

    private RuleBase getRuleBase (HashSet<DeclareBusinessRule> usefulRules){
        RuleBase returnBase = new RuleBase();
        usefulRules.forEach(declareRule -> {
            boolean isResponse = declareRule.getTemplate().equals("Response");
            Literal body = new Literal(declareRule.getBody(), true);
            LinkedList<Literal> bodyList = new LinkedList<>();
            bodyList.add(body);
            Literal head;
            if (isResponse) {
                head = new Literal(declareRule.getHead(), true);

            } else {
                head = new Literal(declareRule.getHead(), false);
            }
            Rule ruleToAdd = new Rule(head, bodyList);
            returnBase.addRuleToBase(ruleToAdd);
        });
        return returnBase;
    }

}


