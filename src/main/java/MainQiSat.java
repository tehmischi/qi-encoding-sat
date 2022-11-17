
        import org.logicng.formulas.Formula;
        import org.logicng.formulas.FormulaFactory;
        import org.logicng.io.writers.FormulaDimacsFileWriter;
        import org.logicng.solvers.SATSolver;

        import java.util.LinkedList;

        public class MainQiSat {

    public static void main(String[] args) {
        long programStart = System.currentTimeMillis();

        new ArgumentHandler(args);
        QiSatConfiguration config = AppSettings.getConfig();
        FormulaFactory formulaFactory = AppSettings.getFormulaFactory();
        LinkedList<Formula> constraints = new LinkedList<>();
        //config.setFilePath("examples/PrepaidTravelCost.csv");
        InputFileParser parser = config.getFileParser();
        RuleBase base = parser.parse();
        long encodingStart = System.currentTimeMillis();
        SatEncoding setInclusion = new SetInclusionEncodingNG(base);
        SatEncoding activationEncoding = new ActivationEncodingNG(base);
        SatEncoding minimalActivation = new MinimalActivationEncodingNG(base);
        constraints.addAll(setInclusion.encode());
        constraints.addAll(activationEncoding.encode());
        constraints.addAll(minimalActivation.encode());
        Formula satEncoding = formulaFactory.and(constraints);
        OutputStringFormatter formatter = new OutputStringFormatter(base);
        long solverStart = System.currentTimeMillis();
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
                FormulaDimacsFileWriter.write(config.getOutputFilePath(), cnf, true);
                System.out.println("Writing encoding in DIMACS Format to file: " + config.getOutputFilePath());
            } catch (Exception e){
                System.out.println("I hate checked exceptions!");
            }
        }
        long endSolver = System.currentTimeMillis();
        /*
        HashSet<String> allModels = new HashSet<>();
        satSolver.enumerateAllModels().forEach(model ->{
            allModels.add(formatter.parse(model.toString()));
        });
        allModels.forEach(string ->{
            System.out.println(string);
        });
        long modelEnd = System.currentTimeMillis();

         */
        if (config.isTimerMode() || config.isBenchmarkMode()) {
            System.out.println("Solver execution time in Miliseconds: " + (endSolver-solverStart));
        }
        if (config.isBenchmarkMode()){
            long startBruteForce = System.currentTimeMillis();
            bruteForceLoop bruteForce = new bruteForceLoop(base);
            bruteForce.checkBaseQI();
            long endBruteForce = System.currentTimeMillis();
            System.out.println("Brute Force execution time in Miliseconds: " + (endBruteForce-startBruteForce));
        }
    }
}
