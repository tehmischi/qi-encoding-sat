import org.tweetyproject.logics.pl.syntax.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class ActivationEncoding {

    private final RuleBase ruleBase;
    private boolean minimal;

    public ActivationEncoding (RuleBase ruleBase) {
        this.ruleBase = ruleBase;
        this.minimal = false;
    }

    public void addActivationConstraints (PlBeliefSet beliefSet) {

        //
        Conjunction overallConj = new Conjunction();
        HashMap<String, LinkedList<String>> heads = new HashMap<>(); //HashMap für "letzte Regel" c_Act(1) -> alle c_Act(1,i)
        ruleBase.getHeads().forEach(literalString -> heads.put(literalString, new LinkedList<>()));
        Conjunction minimalConj1 = new Conjunction();
        Conjunction minimalConj2 = new Conjunction();
        ruleBase.getRuleBase().forEach((id, rule) -> {
            String headString = rule.head().toString();
            heads.get(headString).add(id);
            Proposition rulePresent1 = new Proposition("r_1." + id);
            Negation presentNegation1 = new Negation(rulePresent1);
            Proposition rulePresent2 = new Proposition("r_2." + id);
            Negation presentNegation2 = new Negation(rulePresent2);
            Proposition a = new Proposition(headString + "_ActR1");
            Proposition b = new Proposition(headString + "_ActR1." + id + "");
            Proposition c = new Proposition(headString + "_ActR2");
            Proposition d = new Proposition(headString + "_ActR2." + id + "");
            overallConj.add(new Implication(b,a)); // C_Act(1,1) -> C_Act(1)
            overallConj.add(new Implication(d,c));
            overallConj.add(new Equivalence(b, rulePresent1)); // r_1,1 <->  C_Act(1,1)
            overallConj.add(new Equivalence(d, rulePresent2));
            //Hauptregel für Hinzufügen von Activation Sets
            Conjunction activationRuleBody1 = new Conjunction();
            Conjunction activationRuleBody2 = new Conjunction();
            Conjunction minimalRuleBody1 = new Conjunction();
            Conjunction minimalRuleBody2 = new Conjunction();
            rule.body().forEach(bodyLiteral ->{
                Proposition e = new Proposition("x_1." + bodyLiteral);
                Proposition f = new Proposition("x_2." + bodyLiteral);
                if (heads.containsKey(bodyLiteral.toString())){
                    Proposition g = new Proposition(bodyLiteral + "_ActR1");
                    Proposition h = new Proposition(bodyLiteral + "_ActR2");
                    if (minimal) {
                        Proposition p1 = new Proposition("xm_1." + bodyLiteral);
                        Proposition p2 = new Proposition("xm_2." + bodyLiteral);
                        Negation n1 = new Negation(p1);
                        Negation n2 = new Negation(p2);
                        Conjunction c1 = new Conjunction(e,n1);
                        Conjunction c2 = new Conjunction(f,n2);
                        minimalRuleBody1.add(new Disjunction(c1,g));
                        minimalRuleBody2.add(new Disjunction(c2,h));
                    }
                    activationRuleBody1.add(new Disjunction(e,g));
                    activationRuleBody2.add(new Disjunction(f,h));
                } else {
                    activationRuleBody1.add(e);
                    activationRuleBody2.add(f);
                    if (minimal) {
                        Proposition p1 = new Proposition("xm_1." + bodyLiteral);
                        Proposition p2 = new Proposition("xm_2." + bodyLiteral);
                        Negation n1 = new Negation(p1);
                        Negation n2 = new Negation(p2);
                        Conjunction c1 = new Conjunction(e,n1);
                        Conjunction c2 = new Conjunction(f,n2);
                        minimalRuleBody1.add(c1);
                        minimalRuleBody2.add(c2);
                    }
                }
            });
            Equivalence activationEq1 = new Equivalence(activationRuleBody1, b);
            Equivalence activationEq2 = new Equivalence(activationRuleBody2, d);
            Disjunction activationDisj1 = new Disjunction(activationEq1, presentNegation1);
            Disjunction activationDisj2 = new Disjunction(activationEq2, presentNegation2);
            if (minimal){
                Equivalence minEq1 = new Equivalence(minimalRuleBody1, b);
                Equivalence minEq2 = new Equivalence(minimalRuleBody2, d);
                Disjunction minActDis1= new Disjunction(minEq1, presentNegation1);
                Disjunction minActDis2= new Disjunction(minEq2, presentNegation2);
                minimalConj1.add(minActDis1);
                minimalConj2.add(minActDis2);
            }
            overallConj.add(activationDisj2, activationDisj1);
            //Act Sets können nur aktiviert werden wenn rules dafür vorhanden sind.
            heads.forEach((name,list) -> {
                if (!name.equals(headString)) {
                    overallConj.add(new Negation(new Proposition(name + "_ActR1."+ id + "")));
                    overallConj.add(new Negation(new Proposition(name + "_ActR2."+ id + "")));
                }
            });
        });
        if (minimal){
            Conjunction atMostOne1 = new Conjunction();
            Conjunction atMostOne2 = new Conjunction();
            Disjunction atLeastOne1 = new Disjunction();
            Disjunction atLeastOne2 = new Disjunction();
            HashSet<String> alreadyIncluded = new HashSet<>();
            for (String outerLoopAtom : ruleBase.getPossibleAtoms()) {
                Proposition firstItem1 = new Proposition("xm_1." + outerLoopAtom);
                Proposition firstItem2 = new Proposition("xm_2." + outerLoopAtom);
                atLeastOne1.add(firstItem1);
                atLeastOne2.add(firstItem2);
                for (String innerLoopAtom : ruleBase.getPossibleAtoms()){
                    boolean same = outerLoopAtom.equals(innerLoopAtom);
                    String identifier = outerLoopAtom + "." + innerLoopAtom;
                    String identifier2 = innerLoopAtom + "." +  innerLoopAtom;
                    //TODO warum checkt das hier nicht richtig
                    boolean alreadyDone = alreadyIncluded.contains(identifier) || alreadyIncluded.contains(identifier2);
                    alreadyDone = false;
                    if (!same && !alreadyDone){
                        alreadyIncluded.add(identifier);
                        alreadyIncluded.add(identifier2);
                        Negation secondItem1 = new Negation (new Proposition("xm_1." + innerLoopAtom));
                        Negation secondItem2 = new Negation (new Proposition("xm_2." + innerLoopAtom));
                        Disjunction item1 = new Disjunction(new Negation(firstItem1), secondItem1);
                        Disjunction item2 = new Disjunction(new Negation(firstItem2), secondItem2);
                        atMostOne1.add(item1);
                        atMostOne2.add(item2);
                    }
                }
            }
            //TODO Wo/wie negiere ich richtig..?
            Conjunction exactlyOne1 = new Conjunction(atLeastOne1,atMostOne1);
            Conjunction exactlyOne2 = new Conjunction(atLeastOne2,atMostOne2);
            //Negation negMinAct1 = new Negation(minimalConj1);
            //Negation negMinAct2 = new Negation(minimalConj2);
            Conjunction notMinimal1 = new Conjunction(minimalConj1, exactlyOne1);
            Conjunction notMinimal2 = new Conjunction(minimalConj2, exactlyOne2);
            Negation isMinimal1 = new Negation(notMinimal1);
            Negation isMinimal2 = new Negation(notMinimal2);

            beliefSet.add(isMinimal1,isMinimal2);

        }
        // c_Act(1) -> Disj (alle c_Act(1,i))
        heads.forEach((id, list) -> {
            Proposition activator1 = new Proposition(id + "_ActR1");
            Proposition activator2 = new Proposition(id + "_ActR2");
            Disjunction activationList1 = new Disjunction();
            Disjunction activationList2 = new Disjunction();
            list.forEach(listItem ->{
                activationList1.add(new Proposition(id + "_ActR1." + listItem + ""));
                activationList2.add(new Proposition(id + "_ActR2." + listItem + ""));
            });
            if (!activationList1.isEmpty()){
                overallConj.add(new Implication(activator1, activationList1));
                overallConj.add(new Implication(activator2, activationList2));
            }
        });
        beliefSet.add(overallConj);
    }

    public void setMinimal(boolean minimal) {
        this.minimal = minimal;
    }
}
