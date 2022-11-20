import java.util.HashSet;
import java.util.Set;

public class CheckBruteForce {


    public CheckBruteForce() {
    }

    public boolean isQI(Set<Literal> x1i, Set<Literal> x2i, Set<Rule> r1i, Set<Rule> r2i){
        boolean returnValue = false;
        HashSet<Literal> x1 = new HashSet<>(x1i);
        HashSet<Literal> x2 = new HashSet<>(x2i);
        HashSet<Rule> r1 = new HashSet<>(r1i);
        HashSet<Rule> r2 = new HashSet<>(r2i);
        HashSet<Rule> combined = new HashSet<>(r1);
        combined.addAll(r2);
        boolean consistent = checkFactBaseConsistency(x1) && checkFactBaseConsistency(x2);
        if (consistent) {
            if (checkSetInclusion(x1,x2)){
                if (checkMinimalActivation(x1,r1)){
                    if (checkMinimalActivation(x2,r2)){
                        if (checkInconsistency(x2, combined)){
                            returnValue = true;
                        }
                    }
                }
            }
        } else {
            returnValue = false;
        }
        return returnValue;
    }

    public boolean checkSetInclusion(HashSet<Literal> x1, HashSet<Literal> x2) {
        boolean returnValue = true;
        for (Literal element : x1){
            if (!x2.contains(element)){
                returnValue = false;
            }
        }
        return returnValue;
    }

    public boolean checkFactBaseConsistency(HashSet<Literal> factBase){
        boolean returnValue = true;
        for (Literal fact : factBase){
            Literal negatedLiteral = new Literal(fact.atomName(), !fact.negated());
            if (factBase.contains(negatedLiteral)){
                returnValue = false;
            }
        }
        return returnValue;
    }

    private boolean checkActivation(HashSet<Literal> factBase, HashSet<Rule> ruleBase){
        boolean returnValue = false;
        boolean continueLoop = true;
        HashSet<Literal> factBaseCopy = new HashSet<>(factBase);
        HashSet<Rule> ruleBaseCopy = new HashSet<>(ruleBase);
        while (continueLoop){
            continueLoop = false;
            HashSet<Rule> ruleBaseCopy2 = new HashSet<>(ruleBaseCopy);
            for (Rule rule : ruleBaseCopy){
                if (factBaseCopy.containsAll(rule.body())){
                    factBaseCopy.add(rule.head());
                    ruleBaseCopy2.remove(rule);
                    continueLoop = true;
                }
            }
            ruleBaseCopy = ruleBaseCopy2;
        }
        if (ruleBaseCopy.isEmpty()){
            returnValue = true;
        }
        if (!checkFactBaseConsistency(factBaseCopy)){
            returnValue = false;
        }
        return returnValue;
    }

    public boolean checkMinimalActivation(HashSet<Literal> factBase, HashSet<Rule> ruleBase){
        boolean returnValue = true;
        if (!checkActivation(factBase, ruleBase)) {
            returnValue = false;
        } else {
            HashSet<HashSet<Literal>> reducedFactBases = new HashSet<>();
            for (Literal fact : factBase) {
                HashSet<Literal> reducedBase = new HashSet<>(factBase);
                reducedBase.remove(fact);
                reducedFactBases.add(reducedBase);
            }
            for (HashSet<Literal> reducedBase : reducedFactBases){
                if (checkActivation(reducedBase,ruleBase)){
                    returnValue = false;
                }
            }
        }
        return returnValue;
    }

    public boolean checkInconsistency(HashSet<Literal> factBase, HashSet<Rule> ruleBase) {
        boolean returnValue = true;
        boolean continueLoop = true;
        HashSet<Literal> factBaseCopy = new HashSet<>(factBase);
        HashSet<Rule> ruleBaseCopy = new HashSet<>(ruleBase);
        while (continueLoop){
            continueLoop = false;
            HashSet<Rule> ruleBaseCopy2 = new HashSet<>(ruleBaseCopy);
            for (Rule rule : ruleBaseCopy){
                if (factBaseCopy.containsAll(rule.body())){
                    factBaseCopy.add(rule.head());
                    ruleBaseCopy2.remove(rule);
                    continueLoop = true;
                }
            }
            ruleBaseCopy = ruleBaseCopy2;
        }
        if (checkFactBaseConsistency(factBaseCopy)){
            returnValue = false;
        }
        return returnValue;
    }
}

