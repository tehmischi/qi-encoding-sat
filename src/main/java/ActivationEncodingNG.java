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
        LinkedList<Formula> inconsistencyActivation = new LinkedList<>();
        HashMap<String, LinkedList<String>> heads = new HashMap<>(); //HashMap für "letzte Regel" c_Act(1) -> Disjunction alle c_Act(1,i)
        ruleBase.getHeads().forEach(literalString -> heads.put(literalString, new LinkedList<>()));
        ruleBase.getRuleBase().forEach((id, rule) -> {
            String headString = rule.head().toString();
            heads.get(headString).add(id);
            Variable rulePresent1 = formulaFactory.variable("r_1" + id);
            Variable rulePresent2 = formulaFactory.variable("r_2" + id);
            Variable varInAct1 = formulaFactory.variable(headString + "_ActR1");
            Variable varInAct1Id = formulaFactory.variable(headString + "_ActR1" + id);
            Variable varInAct2 = formulaFactory.variable(headString + "_ActR2");
            Variable varInAct2Id = formulaFactory.variable(headString + "_ActR2" + id);
            Variable varInActBoth = formulaFactory.variable(headString + "_ActR5");
            Variable varInActBothId = formulaFactory.variable(headString + "_ActR5" + id);
            returnConj.add(formulaFactory.implication(varInAct1Id,varInAct1)); // C_Act(1,1) -> C_Act(1)
            returnConj.add(formulaFactory.implication(varInAct2Id,varInAct2));
            returnConj.add(formulaFactory.equivalence(varInAct1Id,rulePresent1));
            returnConj.add(formulaFactory.equivalence(varInAct2Id,rulePresent2));
            //TODO braucht man das hier?
            returnConj.add(formulaFactory.implication(varInActBothId,varInActBoth));
            returnConj.add(formulaFactory.equivalence(varInActBothId,formulaFactory.or(rulePresent1,rulePresent2)));
            //Hauptregel für Hinzufügen von Activation Sets
            LinkedList<Formula> activationRuleBody1 = new LinkedList<>(); //conj
            LinkedList<Formula> activationRuleBody2 = new LinkedList<>(); //conj
            LinkedList<Formula> activationRuleBodyBoth = new LinkedList<>(); //conj
            rule.body().forEach(bodyLiteral ->{
                Variable bodyRule1 = formulaFactory.variable("x_1" + bodyLiteral); //e
                Variable bodyRule2 = formulaFactory.variable("x_2" + bodyLiteral); //f
                if (heads.containsKey(bodyLiteral.toString())){
                    Variable bodyInAct1 = formulaFactory.variable(bodyLiteral + "_ActR1"); //g
                    Variable bodyInAct2 = formulaFactory.variable(bodyLiteral + "_ActR2");//h
                    Variable bodyInAct5 = formulaFactory.variable(bodyLiteral + "_ActR5");
                    activationRuleBody1.add(formulaFactory.or(bodyRule1,bodyInAct1));
                    activationRuleBody2.add(formulaFactory.or(bodyRule2,bodyInAct2));
                    activationRuleBodyBoth.add(formulaFactory.or(bodyInAct5, bodyInAct2, bodyRule2));
                } else {
                    activationRuleBody1.add(bodyRule1);
                    activationRuleBody2.add(bodyRule2);
                    activationRuleBodyBoth.add(bodyRule2);
                }
            });
            Formula ruleBodyConjunction1 = formulaFactory.and(activationRuleBody1);
            Formula ruleBodyConjunction2 = formulaFactory.and(activationRuleBody2);
            Formula activationConj1 = formulaFactory.and(ruleBodyConjunction1, rulePresent1);
            Formula activationConj2 = formulaFactory.and(ruleBodyConjunction2, rulePresent2);
            Formula ruleActivation1 = formulaFactory.equivalence(activationConj1,varInAct1Id);
            Formula ruleActivation2 = formulaFactory.equivalence(activationConj2,varInAct2Id);

            //TODO in ARbeit schreiben!
            Formula ruleBodyConjunction5 = formulaFactory.and(activationRuleBodyBoth);
            Formula activationConj5 = formulaFactory.and(ruleBodyConjunction5, formulaFactory.or(rulePresent1,rulePresent2));
            Formula ruleActivation5 = formulaFactory.equivalence(activationConj5,varInActBothId);
            //das hier müsste stimmen..
            //Formula activationBodyToHeadBoth = formulaFactory.equivalence(varInActBothId,test2);

            //Formula activationDisj12 = formulaFactory.or(activationBodyToHeadBoth, bothNegation);
            inconsistencyActivation.add(ruleActivation5);
            returnConj.add(ruleActivation1);
            returnConj.add(ruleActivation2);
            //Act Sets können nur aktiviert werden, wenn rules dafür vorhanden sind.
            heads.forEach((name,list) -> {
                if (!name.equals(headString)) {
                    returnConj.add(formulaFactory.literal(name + "_ActR1"+ id, false));
                    returnConj.add(formulaFactory.literal(name + "_ActR2"+ id, false));
                }
            });
        });


        LinkedList<Formula> inconsistencyDisj = new LinkedList<>();//disj
        // c_Act(1) -> Disj (alle c_Act(1,i))
        heads.forEach((id, list) -> {
            LinkedList<Formula> activationList1 = new LinkedList<>();//disj
            LinkedList<Formula> activationList2 = new LinkedList<>();//disj
            LinkedList<Formula> activationList5 = new LinkedList<>();
            list.forEach(listItem ->{
                activationList1.add(formulaFactory.variable(id + "_ActR1" + listItem));
                activationList2.add(formulaFactory.variable(id + "_ActR2" + listItem));
                activationList5.add(formulaFactory.variable(id + "_ActR5" + listItem));
            });
            Formula activator1 = formulaFactory.variable(id + "_ActR1");
            Formula activator2 = formulaFactory.variable(id + "_ActR2");
            //Inconsistency
            Formula activatorB = formulaFactory.variable(id + "_ActR5");
            boolean isNegative = id.startsWith("n");
            String newId = isNegative?id.replaceAll("n", ""):"n" + id;
            Formula inconsistencyActDisj = formulaFactory.or(formulaFactory.variable("x_2" + newId), formulaFactory.variable(newId + "_ActR2"));
            Formula inconsistencyActConj = formulaFactory.and(inconsistencyActDisj, activatorB);
            inconsistencyDisj.add(inconsistencyActConj);
            if (!activationList1.isEmpty()){
                Formula activationList1Disj = formulaFactory.or(activationList1);
                Formula activationList2Disj = formulaFactory.or(activationList2);
                Formula activationList5Disj = formulaFactory.or(activationList5);
                returnConj.add(formulaFactory.implication(activator1, activationList1Disj));
                returnConj.add(formulaFactory.implication(activator2, activationList2Disj));
                inconsistencyActivation.add(formulaFactory.implication(activatorB,activationList5Disj));
            }
            //TODO das hier in Arbeit schreiben!
            //Consistency with respect to fact base and to other Activation Variables
            Variable actConsistencytoX1 = formulaFactory.variable("x_1" + newId);
            Variable actConsistencytoX2 = formulaFactory.variable("x_2" + newId);
            Variable actConsistencyToAct1 = formulaFactory.variable(newId + "_ActR1");
            Variable actConsistencyToAct2 = formulaFactory.variable(newId + "_ActR2");
            Formula actConsistencyDisjunction1 = formulaFactory.or(actConsistencyToAct1,actConsistencytoX1);
            Formula actConsistencyDisjunction2 = formulaFactory.or(actConsistencyToAct2,actConsistencytoX2);
            Formula actConsistencyIn1 = formulaFactory.not(actConsistencyDisjunction1);
            Formula actConsistencyIn2 = formulaFactory.not(actConsistencyDisjunction2);
            inconsistencyActivation.add(formulaFactory.implication(activator1,actConsistencyIn1));
            inconsistencyActivation.add(formulaFactory.implication(activator2,actConsistencyIn2));
        });

        returnConj.add(formulaFactory.or(inconsistencyDisj));
        returnConj.add(formulaFactory.and(inconsistencyActivation));
        return returnConj;
    }
}
