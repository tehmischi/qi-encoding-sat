import org.logicng.formulas.*;

import java.util.LinkedList;

public class ConsistencyEncodingNG {

    private final RuleBase ruleBase;


    public ConsistencyEncodingNG(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
    }

    public LinkedList<Formula> getConsistencyRestraints (FormulaFactory formulaFactory) {
        LinkedList<Formula> returnFormulas = new LinkedList<>();
        LinkedList<Formula> overallConj12 = new LinkedList<>();
        ruleBase.getRuleBase().forEach((id, ruleFromBase) -> {
            //Rules Present in R_1 and R_2
            Variable rulePresent1 = formulaFactory.variable("r_1" + id);
            Formula presentNegation1 = formulaFactory.not(rulePresent1);
            Variable rulePresent2 = formulaFactory.variable("r_2" + id);
            Formula presentNegation2 = formulaFactory.not(rulePresent2);
            Formula bothBases = formulaFactory.or(rulePresent1, rulePresent2);
            Formula eitherPresent = formulaFactory.not(bothBases);
            //add Constraints for Rules
            LinkedList<Formula> ruleClauseBaseDisj1 = new LinkedList<>();
            LinkedList<Formula> ruleClauseBaseDisj2 = new LinkedList<>();
            Literal headAtom = ruleFromBase.head();
            String headString1 = "x_1" + headAtom;
            String headString2 = "x_2" + headAtom;
            Formula head1 = formulaFactory.variable(headString1);
            Formula head2 = formulaFactory.variable(headString2);
            ruleClauseBaseDisj1.add(head1);
            ruleClauseBaseDisj2.add(head2);
            ruleFromBase.body().forEach(literal ->{
                String literalString1 = literal.negated()?"x_1" + literal.toString().replaceAll("n", ""):"x_1n" + literal;
                String literalString2 = literal.negated()?"x_2" + literal.toString().replaceAll("n", ""):"x_1n" + literal;
                //TODO stimmt das so, oder "n" umdrehen?
                Formula bodyAtom1 = formulaFactory.variable(literalString1);
                Formula bodyAtom2 = formulaFactory.variable(literalString2);
                ruleClauseBaseDisj1.add(bodyAtom1);
                ruleClauseBaseDisj2.add(bodyAtom2);
            });
            Formula ruleBase1 = formulaFactory.or(ruleClauseBaseDisj1);
            Formula ruleBase2 = formulaFactory.or(ruleClauseBaseDisj2);
            Formula ruleDisjBase1 = formulaFactory.or(presentNegation1, ruleBase1);
            Formula ruleDisjBase2 = formulaFactory.or(presentNegation2, ruleBase2);
            Formula ruleDisjBase12 = formulaFactory.or(eitherPresent, ruleBase2);

            //returnFormulas.add(ruleDisjBase1);
            //returnFormulas.add(ruleDisjBase2);
            overallConj12.add(ruleDisjBase12);
        });
        Formula conjunction12 = formulaFactory.and(overallConj12);
        Formula negConjunction = formulaFactory.not(conjunction12);
        returnFormulas.add(negConjunction);
        return returnFormulas;
    }
}
