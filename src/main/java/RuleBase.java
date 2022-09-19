import java.util.*;

public class RuleBase {

    private final HashMap<String, Rule> rules;
    private final HashSet<String> possibleAtoms = new HashSet<>();
    private final HashSet<String> heads = new HashSet<>();

    private int maxIndex;

    public RuleBase() {
        this.rules = new HashMap<>();
        this.maxIndex = 0;
    }

    public void addRuleToBase (Rule newRule){
        maxIndex++;
        rules.put(String.valueOf(maxIndex), newRule);
        String headString = newRule.head().toString();
        possibleAtoms.add(headString);
        heads.add(headString);
        List<Literal> ruleBody = newRule.body();
        ruleBody.forEach(literal -> possibleAtoms.add(literal.toString()));
    }

    public HashMap<String, Rule> getRuleBase (){
        return rules;
    }

    public HashSet<String> getPossibleAtoms() {
        return possibleAtoms;
    }

    public HashSet<String> getHeads(){return heads;}

    @Override
    public String toString(){
        StringBuilder returnString = new StringBuilder();
        rules.forEach((id, rule) -> returnString.append("Rule ").append(id).append(": ").append(rule.toString()).append("\n"));
        return returnString.toString();
    }
}
