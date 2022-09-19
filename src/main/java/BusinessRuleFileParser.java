import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class BusinessRuleFileParser implements InputFileParser {

    private final String inputFilePath;

    public BusinessRuleFileParser (String inputFilePath){
        this.inputFilePath = inputFilePath;
    }
    @Override
    public RuleBase readFile() {
        RuleBase returnBase = new RuleBase();
        try {
            ArrayList<String> ruleBaseStrings = getRuleStrings();
            ruleBaseStrings.forEach(ruleString -> {
                Rule ruleToAdd = generateRuleFromString(ruleString);
                returnBase.addRuleToBase(ruleToAdd);
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return returnBase;
    }

    public ArrayList<String> getRuleStrings () throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(inputFilePath));
        scanner.useDelimiter(";");
        ArrayList<String> rules = new ArrayList<>();
        while(scanner.hasNext()){
            String rule = scanner.next();
            rule = rule.replace("\n", "").replace("\r", "");
            if (!rule.trim().isEmpty()){
                boolean isComment = rule.startsWith("//");
                if(!isComment){
                    rules.add(rule);
                }
            }
        }
        return rules;
    }

    private Rule generateRuleFromString (String ruleString) {
        String[] seperated = ruleString.split(",");
        String headString = seperated[0];
        String bodyString = seperated[1];
        headString = headString.trim();
        bodyString = bodyString.trim();
        String[] ruleBody = bodyString.split(" +");
        Literal headLiteral = generateLiteral(headString);
        ArrayList<Literal> ruleBodyLiterals = new ArrayList<>();
        for (String s: ruleBody){
            ruleBodyLiterals.add(generateLiteral(s.trim()));
        }
        return new Rule (headLiteral, ruleBodyLiterals);
    }

    private Literal generateLiteral(String literalString){
        boolean negated;
        if (literalString.startsWith("-")) {
            negated = true;
            literalString = literalString.substring(1);
        } else {
            negated = false;
        }
        return new Literal(literalString, negated);
    }
}
