
        import org.logicng.formulas.Formula;
        import org.logicng.formulas.FormulaFactory;
        import org.logicng.io.writers.FormulaDimacsFileWriter;
        import org.logicng.solvers.SATSolver;
        import java.util.LinkedList;

        public class MainQiSat {

    public static void main(String[] args) {
        new ArgumentHandler(args);
        QiSatConfiguration config = AppSettings.getConfig();
        FormulaFactory formulaFactory = AppSettings.getFormulaFactory();
        LinkedList<Formula> constraints = new LinkedList<>();
        //config.setFilePath("examples/PrepaidTravelCost.csv");
        InputFileParser parser = config.getFileParser();
        RuleBase base = parser.parse();

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
        if (!config.isCnfMode()){
            System.out.println("Running Quasi-Inconsistency detection with " + config.getSolverName() + " for rule base:");
            System.out.println(base);

            if (!satSolver.sat().toString().equals("FALSE")){
                System.out.println("The rule base is Quasi-Inconsistent with the following issue: ");
                System.out.println(formatter.parse(satSolver.model().toString()));
            } else {
                System.out.println("Rule base is not Quasi-Inconsistent!");
            }
        } else {
            Formula cnf = satEncoding.cnf();
            try {
                FormulaDimacsFileWriter.write(config.getOutputFilePath(), cnf, false);
                System.out.println("Writing encoding in DIMACS Format to file.");
            } catch (Exception e){
                System.out.println("I hate checked exceptions!");
            }
        }
    }
}
