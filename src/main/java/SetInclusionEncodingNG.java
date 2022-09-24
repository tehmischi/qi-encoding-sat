import org.logicng.formulas.*;

import java.util.HashSet;
import java.util.LinkedList;

public class SetInclusionEncodingNG implements SatEncoding{

    private final RuleBase ruleBase;
    private final FormulaFactory formulaFactory;

    public SetInclusionEncodingNG(RuleBase rulebase){
        this.ruleBase = rulebase;
        this.formulaFactory = Application.getFormulaFactory();
    }

    public LinkedList encode (){
        HashSet<String> atomsInBase = ruleBase.getPossibleAtoms();
        HashSet<String> keepTrackOfDoubles = new HashSet<>();
        LinkedList<Formula> returnList = new LinkedList<>();
        atomsInBase.forEach(atom ->{
            if (!keepTrackOfDoubles.contains(atom.replaceFirst("-", ""))){
                Variable a = formulaFactory.variable("x_1" + atom);
                Variable b = formulaFactory.variable("x_2" + atom);
                Variable c;
                Variable d;
                if (atom.startsWith("n")){
                    c = formulaFactory.variable("x_1" + atom.replaceFirst("n", ""));
                    d = formulaFactory.variable("x_2" + atom.replaceFirst("n", ""));
                } else {
                    c = formulaFactory.variable("x_1n" + atom);
                    d = formulaFactory.variable("x_2n" + atom);
                }
                // Set Inclusion X_1 -> X_2
                returnList.add(formulaFactory.implication(a,b));
                returnList.add(formulaFactory.implication(c,d));
                // Consistency in X_1 and X_2
                Formula e = formulaFactory.and(a,c);
                Formula f = formulaFactory.and(b,d);
                returnList.add(formulaFactory.not(e));
                returnList.add(formulaFactory.not(f));
                keepTrackOfDoubles.add(atom);
            }
        });
        return returnList;
    }
}
