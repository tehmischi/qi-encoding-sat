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
        possibleAtoms.add(newRule.getHead().getAtomName());
        List<Literal> ruleBody = newRule.getBody();
        ruleBody.forEach(literal ->{
            possibleAtoms.add(literal.getAtomName());
        });
    }

    public int getSize() {
        return maxIndex;
    }

    public Rule getRuleAtPos (int x) {
        return rules.get(x);
    }

    public HashMap<String, Rule> getRuleBase (){
        return rules;
    }

    public HashSet<String> getPossibleAtoms() {
        return possibleAtoms;
    }
}
