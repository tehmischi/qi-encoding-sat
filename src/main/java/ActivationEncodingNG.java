import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Variable;

import java.util.HashMap;
import java.util.LinkedList;


public class ActivationEncodingNG implements SatEncoding{

    private final FormulaFactory formulaFactory;
    private final RuleBase ruleBase;

    public ActivationEncodingNG (FormulaFactory formulaFactory, RuleBase ruleBase){
        this.formulaFactory = formulaFactory;
        this.ruleBase = ruleBase;
    }

    @Override
    public LinkedList<Formula> encode() {
        LinkedList<Formula> returnConj = new LinkedList<>();
        HashMap<String, LinkedList<String>> heads = new HashMap<>(); //HashMap für "letzte Regel" c_Act(1) -> alle c_Act(1,i)
        ruleBase.getHeads().forEach(literalString -> heads.put(literalString, new LinkedList<>()));
        ruleBase.getRuleBase().forEach((id, rule) -> {
            String headString = rule.head().toString();
            heads.get(headString).add(id);
            Variable rulePresent1 = formulaFactory.variable("r_1" + id);
            Formula presentNegation1 = formulaFactory.not(rulePresent1);
            Variable rulePresent2 = formulaFactory.variable("r_2" + id);
            Formula presentNegation2 = formulaFactory.not(rulePresent2);
            Variable varInAct1 = formulaFactory.variable(headString + "_ActR1");
            Variable varInAct1Id = formulaFactory.variable(headString + "_ActR1" + id);
            Variable varInAct2 = formulaFactory.variable(headString + "_ActR2");
            Variable varInAct2Id = formulaFactory.variable(headString + "_ActR2" + id);
            returnConj.add(formulaFactory.implication(varInAct1Id,varInAct1)); // C_Act(1,1) -> C_Act(1)
            returnConj.add(formulaFactory.implication(varInAct2Id,varInAct2));
            returnConj.add(formulaFactory.equivalence(varInAct1Id,rulePresent1));
            returnConj.add(formulaFactory.equivalence(varInAct2Id,rulePresent2));
            //Hauptregel für Hinzufügen von Activation Sets
            LinkedList<Formula> activationRuleBody1 = new LinkedList<>(); //conj
            LinkedList<Formula> activationRuleBody2 = new LinkedList<>(); //conj
            rule.body().forEach(bodyLiteral ->{
                Variable bodyRule1 = formulaFactory.variable("x_1" + bodyLiteral); //e
                Variable bodyRule2 = formulaFactory.variable("x_2" + bodyLiteral); //f
                if (heads.containsKey(bodyLiteral.toString())){
                    Variable bodyInAct1 = formulaFactory.variable(bodyLiteral + "_ActR1"); //g
                    Variable bodyInAct2 = formulaFactory.variable(bodyLiteral + "_ActR2");//h
                    activationRuleBody1.add(formulaFactory.or(bodyRule1,bodyInAct1));
                    activationRuleBody2.add(formulaFactory.or(bodyRule2,bodyInAct2));
                } else {
                    activationRuleBody1.add(bodyRule1);
                    activationRuleBody2.add(bodyRule2);
                }
            });
            Formula ruleBodyConjunction1 = formulaFactory.and(activationRuleBody1);
            Formula ruleBodyConjunction2 = formulaFactory.and(activationRuleBody2);
            Formula activationBodyToHead1 = formulaFactory.equivalence(varInAct1Id,ruleBodyConjunction1);
            Formula activationBodyToHead2 = formulaFactory.equivalence(varInAct2Id,ruleBodyConjunction2);

            Formula activationDisj1 = formulaFactory.or(activationBodyToHead1, presentNegation1);
            Formula activationDisj2 = formulaFactory.or(activationBodyToHead2, presentNegation2);
            returnConj.add(activationDisj1);
            returnConj.add(activationDisj2);
            //Act Sets können nur aktiviert werden wenn rules dafür vorhanden sind.
            heads.forEach((name,list) -> {
                if (!name.equals(headString)) {
                    returnConj.add(formulaFactory.literal(name + "_ActR1"+ id, false));
                    returnConj.add(formulaFactory.literal(name + "_ActR2"+ id, false));
                }
            });
        });
        // c_Act(1) -> Disj (alle c_Act(1,i))
        heads.forEach((id, list) -> {
            LinkedList<Formula> activationList1 = new LinkedList<>();//disj
            LinkedList<Formula> activationList2 = new LinkedList<>();//disj
            list.forEach(listItem ->{
                activationList1.add(formulaFactory.variable(id + "_ActR1" + listItem));
                activationList2.add(formulaFactory.variable(id + "_ActR2" + listItem));
            });
            if (!activationList1.isEmpty()){
                Formula activationList1Disj = formulaFactory.or(activationList1);
                Formula activationList2Disj = formulaFactory.or(activationList2);
                Formula activator1 = formulaFactory.variable(id + "_ActR1");
                Formula activator2 = formulaFactory.variable(id + "_ActR2");
                returnConj.add(formulaFactory.implication(activator1, activationList1Disj));
                returnConj.add(formulaFactory.implication(activator2, activationList2Disj));
            }
        });
        return returnConj;
    }
}
