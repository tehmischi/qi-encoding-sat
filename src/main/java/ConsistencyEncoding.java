import org.tweetyproject.logics.pl.syntax.*;

public class ConsistencyEncoding {

    private final RuleBase ruleBase;


    public ConsistencyEncoding(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
    }

    public PlBeliefSet addConsistencyRestraints (PlBeliefSet beliefSet) {
        Disjunction overallClause1 = new Disjunction();
        Disjunction overallClause2 = new Disjunction();
        Disjunction overallClause12 = new Disjunction();
        Conjunction overallConj1 = new Conjunction();
        Conjunction overallConj2 = new Conjunction();
        Conjunction overallConj12 = new Conjunction();
        ruleBase.getRuleBase().forEach((id, ruleFromBase) -> {
            //Rules Present in R_1 and R_2
            Proposition rulePresent1 = new Proposition("r_1," + id);
            Negation presentNegation1 = new Negation(rulePresent1);
            Proposition rulePresent2 = new Proposition("r_2," + id);
            Negation presentNegation2 = new Negation(rulePresent2);
            Disjunction bothBases = new Disjunction(rulePresent1, rulePresent2);
            Negation eitherPresent = new Negation(bothBases);
            //add Constraints for Rules
            Disjunction ruleClauseBase1 = new Disjunction();
            Disjunction ruleClauseBase2 = new Disjunction();
            Disjunction ruleClauseBaseBoth = new Disjunction();
            Literal headAtom = ruleFromBase.getHead();
            String headString1 = "x_1," + headAtom.atomName();
            String headString2 = "x_2," + headAtom.atomName();
            Proposition head1 = new Proposition(headString1);
            Proposition head2 = new Proposition(headString2);
            Disjunction headBoth = new Disjunction(head1, head2);
            Negation headNeg1 = new Negation(head1);
            Negation headNeg2 = new Negation(head2);
            Negation headNegBoth = new Negation(headBoth);
            if (!headAtom.negated()) {
                ruleClauseBase1.add(head1);
                ruleClauseBase2.add(head2);
                ruleClauseBaseBoth.add(headBoth);
            } else {
                ruleClauseBase1.add(headNeg1);
                ruleClauseBase2.add(headNeg2);
                ruleClauseBaseBoth.add(headNegBoth);
            }

            ruleFromBase.getBody().forEach(literal ->{
                String literalString1 = "x_1," + literal.atomName();
                String literalString2 = "x_2," + literal.atomName();
                Proposition bodyAtom1 = new Proposition(literalString1);
                Proposition bodyAtom2 = new Proposition(literalString2);
                Disjunction bodyAtomBoth = new Disjunction(bodyAtom1, bodyAtom2);
                if (!literal.negated()) {
                    ruleClauseBase1.add(new Negation(bodyAtom1));
                    ruleClauseBase2.add(new Negation(bodyAtom2));
                    ruleClauseBaseBoth.add(new Negation(bodyAtomBoth));
                } else {
                    ruleClauseBase1.add(bodyAtom1);
                    ruleClauseBase2.add(bodyAtom2);
                    ruleClauseBaseBoth.add(bodyAtomBoth);
                }
            });
            overallClause1.add(presentNegation1, ruleClauseBase1);
            overallClause2.add(presentNegation2, ruleClauseBase2);
            overallClause12.add(eitherPresent, ruleClauseBaseBoth);
            overallConj1.add(overallClause1);
            overallConj2.add(overallClause2);
            overallConj12.add(overallClause12);

        });

        beliefSet.add(overallConj1,overallConj2);
        //beliefSet.add(new Negation(overallConj12));

        return beliefSet;
    }
}
