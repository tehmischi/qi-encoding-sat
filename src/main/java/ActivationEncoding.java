import org.tweetyproject.logics.pl.syntax.*;

import java.util.HashMap;
import java.util.LinkedList;

public class ActivationEncoding {

    private final RuleBase ruleBase;

    public ActivationEncoding (RuleBase ruleBase) {this.ruleBase = ruleBase;
    }

    public void addActivationConstraints (PlBeliefSet beliefSet) {

        //
        Conjunction overallConj = new Conjunction();
        HashMap<String, LinkedList<String>> heads = new HashMap<>(); //HashMap für "letzte Regel" c_Act(1) -> alle c_Act(1,i)
        ruleBase.getRuleBase().forEach((id, rule) -> {
            String headString = rule.getHead().toString();
            if (heads.containsKey(headString)){
                heads.get(headString).add(id);
            } else {
                heads.put(headString, new LinkedList<>());
                heads.get(headString).add(id);
            }
            Proposition rulePresent1 = new Proposition("r_1," + id);
            Negation presentNegation1 = new Negation(rulePresent1);
            Proposition rulePresent2 = new Proposition("r_2," + id);
            Negation presentNegation2 = new Negation(rulePresent2);
            Proposition a = new Proposition(headString + "_Act(1)");
            Proposition b = new Proposition(headString + "_Act(1," + id + ")");
            Proposition c = new Proposition(headString + "_Act(2)");
            Proposition d = new Proposition(headString + "_Act(2," + id + ")");
            overallConj.add(new Implication(b,a)); // C_Act(1,1) -> C_Act(1)
            overallConj.add(new Implication(d,c));
            overallConj.add(new Equivalence(b, rulePresent1)); // r_1,1 <->  C_Act(1,1)
            overallConj.add(new Equivalence(d, rulePresent2));
            //Hauptregel für Hinzufügen von Activation Sets
            Conjunction activationRuleBody1 = new Conjunction();
            Conjunction activationRuleBody2 = new Conjunction();
            rule.getBody().forEach(bodyLiteral ->{
                Proposition e = new Proposition("x_1," + bodyLiteral);
                Proposition f = new Proposition("x_2," + bodyLiteral);
                Proposition g = new Proposition(bodyLiteral + "_Act(1)");
                Proposition h = new Proposition(bodyLiteral + "_Act(2)");
                activationRuleBody1.add(new Disjunction(e,g));
                activationRuleBody2.add(new Disjunction(f,h));
            });
            Equivalence activationEq1 = new Equivalence(activationRuleBody1, b);
            Equivalence activationEq2 = new Equivalence(activationRuleBody2, d);
            Disjunction activationDisj1 = new Disjunction(activationEq1, presentNegation1);
            Disjunction activationDisj2 = new Disjunction(activationEq2, presentNegation2);
            overallConj.add(activationDisj2, activationDisj1);
        });
        // c_Act(1) -> Disj (alle c_Act(1,i))
        heads.forEach((id, list) -> {
            Proposition activator1 = new Proposition(id + "_Act(1)");
            Proposition activator2 = new Proposition(id + "_Act(2)");
            Disjunction activationList1 = new Disjunction();
            Disjunction activationList2 = new Disjunction();
            list.forEach(listItem ->{
                activationList1.add(new Proposition(id + "_Act(1," + listItem + ")"));
                activationList2.add(new Proposition(id + "_Act(2," + listItem + ")"));
            });
            System.out.println("Größe der Liste mit id " + id +" = " + activationList1.size());
            if (activationList1.isEmpty()){
                overallConj.add(new Implication(activator1, new Contradiction()));
                overallConj.add(new Implication(activator2, new Proposition("asdf")));
            } else {
                overallConj.add(new Implication(activator1, activationList1));
                overallConj.add(new Implication(activator2, activationList2));
            }
        });
        beliefSet.add(overallConj);
    }
}
