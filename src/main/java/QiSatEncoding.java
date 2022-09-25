
        import org.logicng.formulas.Formula;
        import org.logicng.formulas.FormulaFactory;
        import org.logicng.solvers.MiniSat;
        import org.logicng.solvers.SATSolver;

        import java.io.*;
        import java.util.LinkedList;

        public class QiSatEncoding {

    public static void main(String[] args) throws IOException {
        FormulaFactory formulaFactory = Application.getFormulaFactory();
        LinkedList<Formula> constraints = new LinkedList<>();
        QiSatConfiguration config = new QiSatConfiguration(args);
        BusinessRuleFileParser parser;
        parser = new BusinessRuleFileParser(config.getFilePath());
        RuleBase base = parser.readFile();
        SatEncoding setInclusion = new SetInclusionEncodingNG(base);
        SatEncoding activationEncoding = new ActivationEncodingNG(base);
        SatEncoding minimalActivation = new MinimalActivationEncodingNG(base);
        constraints.addAll(setInclusion.encode());
        constraints.addAll(activationEncoding.encode());
        constraints.addAll(minimalActivation.encode());
        Formula satEncoding = formulaFactory.and(constraints);
        OutputStringFormatter formatter = new OutputStringFormatter(base);

        System.out.println("Running Quasi-Inconsistency detection for rule base:");
        System.out.println(base);
        //formatter.setDebugMode(true);
        SATSolver glucose = MiniSat.glucose(formulaFactory);
        glucose.add(satEncoding);
        System.out.println("Glucose: ");
        if (!glucose.sat().toString().equals("FALSE")){
            System.out.println(formatter.parse(glucose.model().toString()));
        } else {
            System.out.println("Rule base is not Quasi-Inconsistent!");
        }

        SATSolver miniSat = MiniSat.miniSat(formulaFactory);
        miniSat.add(satEncoding);

        System.out.println("MiniSat:");
        if (!miniSat.sat().toString().equals("FALSE")){
            System.out.println(formatter.parse(miniSat.model().toString()));
        } else {
            System.out.println("Rule base is not Quasi-Inconsistent!");
        }
    }
}
