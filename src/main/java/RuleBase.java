import java.util.*;

public class RuleBase {

    private final HashMap<String, Rule> rules;
    private final HashSet<String> possibleAtoms = new HashSet<>();

    private int maxIndex;

    public RuleBase() {
        this.rules = new HashMap<>();
        this.maxIndex = 0;
    }

    public void addRuleToBase (Rule newRule){
        maxIndex++;
        rules.put(String.valueOf(maxIndex), newRule);
        possibleAtoms.add(newRule.head().toString());
        List<Literal> ruleBody = newRule.body();
        ruleBody.forEach(literal -> possibleAtoms.add(literal.toString()));
    }

    public HashMap<String, Rule> getRuleBase (){
        return rules;
    }

    public HashSet<String> getPossibleAtoms() {
        return possibleAtoms;
    }
}
