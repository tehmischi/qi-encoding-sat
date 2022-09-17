import org.tweetyproject.commons.BeliefSet;
import org.tweetyproject.logics.pl.syntax.*;

import java.util.HashSet;

public class SetInclusionEncoding {

    private final RuleBase ruleBase;

    public SetInclusionEncoding (RuleBase rulebase){
        this.ruleBase = rulebase;
    }

    public void addSetInclusionConstraints (PlBeliefSet beliefSet){
        HashSet<String> atomsInBase = ruleBase.getPossibleAtoms();
        HashSet<String> keepTrackOfDoubles = new HashSet<>();
        atomsInBase.forEach(atom ->{
            if (!keepTrackOfDoubles.contains(atom.replaceFirst("-", ""))){
                Proposition a = new Proposition("x_1." + atom);
                Proposition b = new Proposition("x_2." + atom);
                Proposition c;
                Proposition d;
                if (atom.startsWith("-")){
                    c = new Proposition("x_1." + atom.replaceFirst("-", ""));
                    d = new Proposition("x_2." + atom.replaceFirst("-", ""));
                } else {
                    c = new Proposition("x_1.-" + atom);
                    d = new Proposition("x_2.-" + atom);
                }
                // Set Inclusion X_1 -> X_2
                Implication t = new Implication(a,b);
                Implication u = new Implication(c,d);
                // Consistency in X_1 and X_2
                Conjunction e = new Conjunction(a,c);
                Conjunction f = new Conjunction(b,d);
                Negation g = new Negation(e);
                Negation h = new Negation(f);
                keepTrackOfDoubles.add(atom);
                beliefSet.add(t,u,g,h);
            }
        });
    }
}
