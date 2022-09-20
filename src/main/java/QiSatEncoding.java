
        import org.logicng.datastructures.Assignment;
        import org.logicng.formulas.Formula;
        import org.logicng.formulas.FormulaFactory;
        import org.logicng.io.parsers.PropositionalParser;
        import org.logicng.io.parsers.ParserException;
        import org.logicng.solvers.MiniSat;
        import org.logicng.solvers.SATSolver;
        import org.logicng.solvers.sat.MiniSatConfig;
        import org.tweetyproject.logics.pl.syntax.*;
        import java.io.*;
        import java.util.LinkedList;
        import java.util.List;

        public class QiSatEncoding {

    public static void main(String[] args) throws ParserException, IOException {
        QiSatConfiguration config = new QiSatConfiguration(args);
        BusinessRuleFileParser parser;
        parser = new BusinessRuleFileParser(config.getFilePath());
        RuleBase base = parser.readFile();
        PlBeliefSet satFormula = new PlBeliefSet();
        SetInclusionEncoding setInclusion = new SetInclusionEncoding(base);
        ConsistencyEncoding consistencyEncoding = new ConsistencyEncoding(base);
        ActivationEncoding activationEncoding = new ActivationEncoding(base);
        activationEncoding.setMinimal(config.isMinimalSearchActive());
        consistencyEncoding.addConsistencyRestraints(satFormula);
        setInclusion.addSetInclusionConstraints(satFormula);
        activationEncoding.addActivationConstraints(satFormula);
        String inputString = satFormula.toString();
        String ngString = new LogicNGParser().parse(inputString);
        System.out.println("Input: " + ngString + "\n");
        OutputStringFormatter formatter = new OutputStringFormatter(base);
        FormulaFactory f = new FormulaFactory();
        PropositionalParser p = new PropositionalParser(f);
        Formula formula = null;
        try{
            formula = p.parse(ngString);
        } catch (org.logicng.io.parsers.ParserException e) {
            System.err.println("Not a valid LogicNG formula");
        }
        System.out.println("Running Quasi-Inconsistency detection for rule base:");
        System.out.println(base);
        formatter.setDebugMode(true);
        SATSolver glucose = MiniSat.glucose(f);
        glucose.add(formula);
        glucose.sat();
        System.out.println("Glucose: ");
        if (!glucose.sat().toString().equals("FALSE")){
            System.out.println(formatter.parse(glucose.model().toString()));
        } else {
            System.out.println("UNSAT");
        }
        MiniSatConfig miniSatConfig = MiniSatConfig.builder().proofGeneration(true).build();
        SATSolver solver = MiniSat.miniSat(f, miniSatConfig);
        solver.add(formula);
        solver.sat();
        /*
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
        System.out.println("MiniSat:");
        if (!solver.sat().toString().equals("FALSE")){
            String miniSatOutput = solver.model().toString();
            String output = formatter.parse(miniSatOutput);
            System.out.println(miniSatOutput);
            System.out.println(output);
        } else {
            System.out.println("UNSAT");
        }
    }
}
