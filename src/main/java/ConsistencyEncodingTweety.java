import org.tweetyproject.logics.pl.syntax.*;

public class ConsistencyEncodingTweety {

    private final RuleBase ruleBase;


    public ConsistencyEncodingTweety(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
    }

    public void addConsistencyRestraints (PlBeliefSet beliefSet) {
        Conjunction overallConj12 = new Conjunction();
        ruleBase.getRuleBase().forEach((id, ruleFromBase) -> {
            //Rules Present in R_1 and R_2
            Proposition rulePresent1 = new Proposition("r_1." + id);
            Negation presentNegation1 = new Negation(rulePresent1);
            Proposition rulePresent2 = new Proposition("r_2." + id);
            Negation presentNegation2 = new Negation(rulePresent2);
            Disjunction bothBases = new Disjunction(rulePresent1, rulePresent2);
            Negation eitherPresent = new Negation(bothBases);
            //add Constraints for Rules
            Disjunction ruleClauseBase1 = new Disjunction();
            Disjunction ruleClauseBase2 = new Disjunction();
            Disjunction ruleClauseBaseBoth = new Disjunction();
            Literal headAtom = ruleFromBase.head();
            String headString1 = "x_1." + headAtom.atomName();
            String headString2 = "x_2." + headAtom.atomName();
            Proposition head1 = new Proposition(headString1);
            Proposition head2 = new Proposition(headString2);
            Negation headNeg1 = new Negation(head1);
            Negation headNeg2 = new Negation(head2);
            if (!headAtom.negated()) {
                ruleClauseBase1.add(head1);
                ruleClauseBase2.add(head2);
                ruleClauseBaseBoth.add(head2);
            } else {
                ruleClauseBase1.add(headNeg1);
                ruleClauseBase2.add(headNeg2);
                ruleClauseBaseBoth.add(headNeg2);
            }
            ruleFromBase.body().forEach(literal ->{
                String literalString1 = "x_1." + literal.atomName();
                String literalString2 = "x_2." + literal.atomName();
                Proposition bodyAtom1 = new Proposition(literalString1);
                Proposition bodyAtom2 = new Proposition(literalString2);
                if (!literal.negated()) {
                    ruleClauseBase1.add(new Negation(bodyAtom1));
                    ruleClauseBase2.add(new Negation(bodyAtom2));
                    ruleClauseBaseBoth.add(new Negation(bodyAtom2));
                } else {
                    ruleClauseBase1.add(bodyAtom1);
                    ruleClauseBase2.add(bodyAtom2);
                    ruleClauseBaseBoth.add(bodyAtom2);
                }
            });
            Disjunction ruleDisjBase1 = new Disjunction(presentNegation1, ruleClauseBase1);
            Disjunction ruleDisjBase2 = new Disjunction(presentNegation2, ruleClauseBase2);
            Disjunction ruleDisjBase12 = new Disjunction(eitherPresent, ruleClauseBaseBoth);
            beliefSet.add(ruleDisjBase1,ruleDisjBase2);
            overallConj12.add(ruleDisjBase12);
        });

        beliefSet.add(new Negation(overallConj12));
    }
}
