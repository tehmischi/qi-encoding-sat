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
        //Necessary like this for lexicographic order
        String[] ruleBaseArray = new String[rules.size()];
        rules.forEach((id, rule)->{
            ruleBaseArray[Integer.parseInt(id)-1] = rule.toString();
        });
        for (int i = 0; i < rules.size(); i++){
            returnString.append("Rule ").append(i+1).append(": ").append(ruleBaseArray[i]).append("\n");
        }
        return returnString.toString();
    }
}
