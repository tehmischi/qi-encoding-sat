
        import org.logicng.formulas.Formula;
        import org.logicng.formulas.FormulaFactory;
        import org.logicng.io.parsers.ParserException;
        import org.logicng.solvers.MiniSat;
        import org.logicng.solvers.SATSolver;

        import java.io.*;
        import java.util.LinkedList;

        public class QiSatEncoding {

    public static void main(String[] args) throws IOException {
        FormulaFactory formulaFactory = new FormulaFactory();
        LinkedList<Formula> constraints = new LinkedList<>();
        QiSatConfiguration config = new QiSatConfiguration(args);
        BusinessRuleFileParser parser;
        parser = new BusinessRuleFileParser(config.getFilePath());
        RuleBase base = parser.readFile();

        SetInclusionEncodingNG setInclusion = new SetInclusionEncodingNG(base);
        ConsistencyEncodingNG consistencyEncodingNG = new ConsistencyEncodingNG(base);
        ActivationEncodingNG activationEncoding = new ActivationEncodingNG(formulaFactory, base);


        constraints.addAll(setInclusion.getSetInclusionConstraints(formulaFactory));
        constraints.addAll(consistencyEncodingNG.getConsistencyRestraints(formulaFactory));
        constraints.addAll(activationEncoding.encode());


        Formula satEncoding = formulaFactory.and(constraints);
        System.out.println("Input: " + satEncoding + "\n");
        OutputStringFormatter formatter = new OutputStringFormatter(base);

        System.out.println("Running Quasi-Inconsistency detection for rule base:");
        System.out.println(base);
        formatter.setDebugMode(true);
        SATSolver glucose = MiniSat.glucose(formulaFactory);
        glucose.add(satEncoding);
        glucose.sat();
        System.out.println("Glucose: ");
        if (!glucose.sat().toString().equals("FALSE")){
            System.out.println(formatter.parse(glucose.model().toString()));
        } else {
            System.out.println("UNSAT");
        }
        /*
        MiniSatConfig miniSatConfig = MiniSatConfig.builder().proofGeneration(true).build();
        SATSolver solver = MiniSat.miniSat(f, miniSatConfig);
        solver.add(formula);
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


        System.out.println("MiniSat:");
        if (!solver.sat().toString().equals("FALSE")){
            String miniSatOutput = solver.model().toString();
            String output = formatter.parse(miniSatOutput);
            System.out.println(miniSatOutput);
            System.out.println(output);
        } else {
            System.out.println("UNSAT");
        }

        */
    }
}
