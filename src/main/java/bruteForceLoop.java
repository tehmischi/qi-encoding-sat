import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.*;
public class bruteForceLoop {

    private final RuleBase ruleBase;

    public bruteForceLoop(RuleBase ruleBase){
        this.ruleBase = ruleBase;
    }

    public void checkBaseQI(){
        long startTime = System.currentTimeMillis();
        checkBruteForce bruteForcer = new checkBruteForce();
        HashSet<Literal> facts = new HashSet<>();
        HashSet<Rule> rules = new HashSet<>();
        ruleBase.getRuleBase().forEach((id, rule) ->{
            rules.add(rule);
            facts.add(rule.head());
            rule.body().forEach(bodyLiteral -> facts.add(bodyLiteral));
        });
        Set<Set<Literal>> powerSetFacts = Sets.powerSet(facts);
        Set<Set<Rule>> powerSetRules = Sets.powerSet(rules);
        for (Set<Literal> x1 : powerSetFacts){
            for (Set<Literal> x2 : powerSetFacts){
                for (Set<Rule> r1 :powerSetRules){
                    /*
                    for (Set<Rule> r2 :powerSetRules){
                        i++;
                        System.out.println(issueString(x1,x2,r1,r2));
                    }
                    */
                    powerSetRules.parallelStream().forEach(r2 ->{
                        if (bruteForcer.isQI(x1,x2,r1,r2)){
                            //System.out.println(issueString(x1,x2,r1,r2));
                            long executionTime = System.currentTimeMillis()-startTime;
                            System.out.println("Issue found with brute force in " + executionTime + " ms");
                        }
                    });


                }
            }
        }
        //System.out.println("Not Quasi-Inconsistent!");
        //System.out.println("Number of Tries: " + i);
    }

    private String issueString (Set<Literal> x1, Set<Literal> x2, Set<Rule> r1, Set<Rule> r2){
        StringBuilder returnString = new StringBuilder();
        returnString.append("X1: ");
        x1.forEach(literal -> returnString.append(literal.toString()).append(", "));
        returnString.append("\n");
        returnString.append("X2: ");
        x2.forEach(literal ->returnString.append(literal.toString()).append(", "));
        returnString.append("\n");
        returnString.append("R1: ");
        r1.forEach(rule -> returnString.append(rule.toString()).append(", "));
        returnString.append("\n");
        returnString.append("R2: ");
        r2.forEach(rule -> returnString.append(rule.toString()).append(", "));
        returnString.append("\n");
        return returnString.toString();
    }
}
