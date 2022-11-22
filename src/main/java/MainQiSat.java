
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
        System.out.println("Rule base:");
        System.out.println(base);
        long solverStart = System.currentTimeMillis();
        if (config.isTimerMode() || config.isBenchmarkMode()) {
            System.out.println("Encoding execution time in Miliseconds: " + (encodingStart-programStart) +"\n");
        }
        SATSolver satSolver = config.getSolver();
        satSolver.add(satEncoding);
        if (config.isDebugMode()){
            System.out.println("SAT encoding: ");
            System.out.println(satEncoding + "\n");
        }
        if (!config.isCnfMode()){
            System.out.println("Running Quasi-Inconsistency detection with SAT solver " + config.getSolverName());
            if (!satSolver.sat().toString().equals("FALSE")){
                System.out.println("The rule base is Quasi-Inconsistent with the following issue: ");
                System.out.println(formatter.parse(satSolver.model().toString()));
            } else {
                System.out.println("Rule base is not Quasi-Inconsistent!");
            }
            long endSolver = System.currentTimeMillis();
            if (config.isTimerMode() || config.isBenchmarkMode()) {
                System.out.println("Solver execution time in Miliseconds: " + (endSolver-solverStart));
            }
            //Brute Force Approach
            if (config.isBenchmarkMode()){
                long startBruteForce = System.currentTimeMillis();
                BruteForceLoop bruteForce = new BruteForceLoop(base);
                bruteForce.checkBaseQI();
                long endBruteForce = System.currentTimeMillis();
                System.out.println("Brute Force execution time in Miliseconds: " + (endBruteForce-startBruteForce));
            }
        } else {
            Formula cnf = satEncoding.cnf();
            try {
                FormulaDimacsFileWriter.write(config.getOutputFilePath(), cnf, true);
                System.out.println("Writing encoding in DIMACS Format to file: " + config.getOutputFilePath() + ".cnf");
                System.out.println("Writing DIMACS mapping file to: " + config.getOutputFilePath() + ".map");
            } catch (Exception e){
                System.out.println("I hate checked exceptions!");
            }
        }

        long programEnd = System.currentTimeMillis();
        if (config.isTimerMode() || config.isBenchmarkMode()) {
            System.out.println("Program execution time in Milliseconds: " + (programEnd-programStart));
        }
    }
}
