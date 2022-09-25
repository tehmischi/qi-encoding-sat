import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.formulas.Variable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class MinimalActivationEncodingNG implements  SatEncoding {

    private final RuleBase ruleBase;
    private final FormulaFactory formulaFactory;

    public MinimalActivationEncodingNG(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
        this.formulaFactory = Application.getFormulaFactory();
    }

    @Override
    public LinkedList encode() {
        LinkedList<Formula> minimalActivationEncoding = new LinkedList<>();
        HashSet<String> allLiterals = new HashSet<>();
        ruleBase.getPossibleAtoms().forEach(possibleAtom -> {
            allLiterals.add(possibleAtom);
            String negatedAtom;
            if (possibleAtom.startsWith("n")){
                negatedAtom = possibleAtom.replaceFirst("n", "");
            } else {
                negatedAtom = "n" + possibleAtom;
            }
            allLiterals.add(negatedAtom);

        });
        allLiterals.forEach(possibleAtom -> {
            Literal negatedAtom1 = formulaFactory.literal("x_1" + possibleAtom, false);
            Literal negatedAtom2 = formulaFactory.literal("x_2" + possibleAtom, false);
            LinkedList<Formula> oneVariableCheck1 = new LinkedList<>();
            LinkedList<Formula> oneVariableCheck2 = new LinkedList<>();
            oneVariableCheck1.add(negatedAtom1);
            oneVariableCheck2.add(negatedAtom2);
            LinkedList<Formula> allRulesConjunction1 = new LinkedList<>();
            LinkedList<Formula> allRulesConjunction2 = new LinkedList<>();
            HashMap<String, LinkedList<String>> heads = new HashMap<>();
            ruleBase.getHeads().forEach(literalString -> heads.put(literalString, new LinkedList<>()));
            ruleBase.getRuleBase().forEach((ruleId, rule) -> {

                Variable rulePresent1 = formulaFactory.variable("r_1" + ruleId);
                Variable rulePresent2 = formulaFactory.variable("r_2" + ruleId);

                LinkedList<Formula> activationRuleBody1 = new LinkedList<>(); //conj
                LinkedList<Formula> activationRuleBody2 = new LinkedList<>(); //conj
                rule.body().forEach(bodyLiteral -> {
                    Formula bodyVariable1;
                    Formula bodyVariable2;
                    if (possibleAtom.equals(bodyLiteral.toString())){
                        bodyVariable1 = formulaFactory.falsum();
                        bodyVariable2 = formulaFactory.falsum();
                    } else {
                        bodyVariable1 = formulaFactory.variable("x_1" + bodyLiteral);
                        bodyVariable2 = formulaFactory.variable("x_2" + bodyLiteral);
                    }
                    if (heads.containsKey(bodyLiteral.toString())) {
                        Variable variableInAct1 = formulaFactory.variable(bodyLiteral + "_ActR1"); //g
                        Variable variableInAct2 = formulaFactory.variable(bodyLiteral + "_ActR2");//h
                        activationRuleBody1.add(formulaFactory.or(bodyVariable1, variableInAct1));
                        activationRuleBody2.add(formulaFactory.or(bodyVariable2, variableInAct2));

                    } else {
                        activationRuleBody1.add(bodyVariable1);
                        activationRuleBody2.add(bodyVariable2);
                    }
                });
                Formula ruleBodyConjunction1 = formulaFactory.and(activationRuleBody1);
                Formula ruleBodyConjunction2 = formulaFactory.and(activationRuleBody2);
                Formula activationDisj1 = formulaFactory.or(ruleBodyConjunction1, formulaFactory.not(rulePresent1));
                Formula activationDisj2 = formulaFactory.or(ruleBodyConjunction2, formulaFactory.not(rulePresent2));

                allRulesConjunction1.add(activationDisj1);
                allRulesConjunction2.add(activationDisj2);
            });
            oneVariableCheck1.add(formulaFactory.not(formulaFactory.and(allRulesConjunction1)));
            oneVariableCheck2.add(formulaFactory.not(formulaFactory.and(allRulesConjunction2)));
            Formula oneAtomCheck1 = formulaFactory.or(oneVariableCheck1);
            Formula oneAtomCheck2 = formulaFactory.or(oneVariableCheck2);
            minimalActivationEncoding.add(oneAtomCheck1);
            minimalActivationEncoding.add(oneAtomCheck2);
        });
        return minimalActivationEncoding;
    }
}
