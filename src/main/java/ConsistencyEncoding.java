import org.tweetyproject.logics.pl.syntax.Disjunction;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.Proposition;

public class ConsistencyEncoding {

    private final RuleBase ruleBase;


    public ConsistencyEncoding(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
    }

    public PlBeliefSet addConsistencyRestraints (PlBeliefSet beliefSet) {
        PlBeliefSet returnBeliefSet = beliefSet;
        ruleBase.getRuleBase().forEach((id, ruleFromBase) -> {
            //Rules Present in R_1 and R_2
            Proposition rulePresent1 = new Proposition("r_1," + id);
            Negation presentNegation1 = new Negation(rulePresent1);
            Proposition rulePresent2 = new Proposition("r_2," + id);
            Negation presentNegation2 = new Negation(rulePresent2);
            //add Constraints for Rules
            Disjunction ruleClause = new Disjunction();

            Literal headAtom = ruleFromBase.getHead();
            String headString = headAtom.getAtomName() + "_1";
            Proposition head = new Proposition(headAtom.getAtomName());
            Negation headNeg = new Negation(head);
            if (headAtom.getNegated()) {
                ruleClause.add(head);
            } else {
                ruleClause.add(headNeg);
            }


        });

        return returnBeliefSet;
    }
}
