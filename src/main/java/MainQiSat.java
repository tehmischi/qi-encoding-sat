
        import org.logicng.formulas.Formula;
        import org.logicng.formulas.FormulaFactory;
        import org.logicng.solvers.SATSolver;

        import java.io.*;
        import java.util.LinkedList;

        public class MainQiSat {

    public static void main(String[] args) {
        new ArgumentHandler(args);
        QiSatConfiguration config = AppSettings.getConfig();
        FormulaFactory formulaFactory = AppSettings.getFormulaFactory();
        LinkedList<Formula> constraints = new LinkedList<>();
        BusinessRuleFileParser parser = new BusinessRuleFileParser(config.getFilePath());
        RuleBase base = parser.readFile();

        DeclareModelParser declareParser = new DeclareModelParser();
        try{
            base = declareParser.parseCSV("examples/PrepaidTravelCost.csv");
        } catch (Exception e) {
            System.err.println("Not a valid CSV file!");
        }
        SatEncoding setInclusion = new SetInclusionEncodingNG(base);
        SatEncoding activationEncoding = new ActivationEncodingNG(base);
        SatEncoding minimalActivation = new MinimalActivationEncodingNG(base);
        constraints.addAll(setInclusion.encode());
        constraints.addAll(activationEncoding.encode());
        constraints.addAll(minimalActivation.encode());
        Formula satEncoding = formulaFactory.and(constraints);
        OutputStringFormatter formatter = new OutputStringFormatter(base);

        SATSolver satSolver = config.getSolver();
        satSolver.add(satEncoding);
        if (config.isDebugMode()){
            System.out.println(satEncoding);
        }
        System.out.println("Running Quasi-Inconsistency detection with " + config.getSolverName() + " for rule base:");
        System.out.println(base);

        if (!satSolver.sat().toString().equals("FALSE")){
            System.out.println("The rule base is Quasi-Inconsistent with the following issue: ");
            System.out.println(formatter.parse(satSolver.model().toString()));
        } else {
            System.out.println("Rule base is not Quasi-Inconsistent!");
        }
    }
}
