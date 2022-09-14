import org.tweetyproject.commons.BeliefSet;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.Proposition;

import java.util.HashSet;

public class SetInclusionEncoding {

    private final RuleBase ruleBase;

    public SetInclusionEncoding (RuleBase rulebase){
        this.ruleBase = rulebase;
    }

    public PlBeliefSet addSetInclusionConstraints (PlBeliefSet beliefSet){
        PlBeliefSet returnBeliefSet = beliefSet;
        HashSet<String> atomsInBase = ruleBase.getPossibleAtoms();
        atomsInBase.forEach(atom ->{
            Proposition a = new Proposition("x_1," + atom);
            Proposition b = new Proposition("x_2," + atom);
            Proposition c = new Proposition("x_1,-" + atom);
            Proposition d = new Proposition("x_2,-" + atom);
            Negation e = new Negation(a);
            Negation f = new Negation(b);
            Negation g = new Negation(c);
            Negation h = new Negation(d);
            // Set Inclusion X_1 -> X_2
            Implication t = new Implication(a,b);
            Implication u = new Implication(c,d);
            // Consistency in X_1 and X_2
            Implication v = new Implication(a,g);
            Implication w = new Implication(c,e);
            Implication x = new Implication(b,h);
            Implication y = new Implication(d,f);
            returnBeliefSet.add(t,u,v,w,x,y);
        });
        return returnBeliefSet;
    }
}
