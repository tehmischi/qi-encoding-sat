
        import org.logicng.formulas.Formula;
        import org.logicng.formulas.FormulaFactory;
        import org.logicng.solvers.MiniSat;
        import org.logicng.solvers.SATSolver;
        import org.logicng.solvers.sat.MiniSatConfig;

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
        System.out.println("Input: " + satEncoding + "\n");
        OutputStringFormatter formatter = new OutputStringFormatter(base);

        System.out.println("Running Quasi-Inconsistency detection for rule base:");
        System.out.println(base);
        formatter.setDebugMode(true);
        SATSolver glucose = MiniSat.glucose(formulaFactory);
        glucose.add(satEncoding);
        System.out.println(glucose.sat());
        System.out.println("Glucose: ");
        if (!glucose.sat().toString().equals("FALSE")){
            System.out.println(formatter.parse(glucose.model().toString()));
            System.out.println(glucose.model());
        } else {
            System.out.println("UNSAT");
        }

        MiniSatConfig miniSatConfig = MiniSatConfig.builder().proofGeneration(true).build();
        SATSolver solver = MiniSat.miniSat(formulaFactory, miniSatConfig);
        solver.add(satEncoding);

        System.out.println("MiniSat:");
        if (!solver.sat().toString().equals("FALSE")){
            String miniSatOutput = solver.model().toString();
            String output = formatter.parse(miniSatOutput);
            System.out.println(miniSatOutput);
            System.out.println(output);
        } else {
            System.out.println("UNSAT");
        }

            /*
        solver.sat();

        List<Assignment> models;

        models = solver.enumerateAllModels();
        System.out.println(models.size());
        LinkedList<String> modelStrings = new LinkedList<>();

        models.forEach(model ->{
            String modelString = formatter.parse(model.toString());
            modelStrings.add(modelString);
            System.out.println("MiniSat (all models): ");
            System.out.println(modelStrings);
        });
    */




    }
}
