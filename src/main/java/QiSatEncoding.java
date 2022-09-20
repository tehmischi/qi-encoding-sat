
        import org.logicng.formulas.Formula;
        import org.logicng.formulas.FormulaFactory;
        import org.logicng.io.parsers.PropositionalParser;
        import org.logicng.io.parsers.ParserException;
        import org.logicng.solvers.MiniSat;
        import org.logicng.solvers.SATSolver;
        import org.logicng.solvers.sat.MiniSatConfig;
        import org.tweetyproject.logics.pl.syntax.*;
        import java.io.*;

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
        System.out.println("Input: " + inputString + "\n");
        String ngString = new LogicNGParser().parse(inputString);
        //System.out.println(ngString);
        //System.out.println("CNF: " + satFormula.toCnf() + "\n");
        OutputStringFormatter formatter = new OutputStringFormatter(base);
        FormulaFactory f = new FormulaFactory();
        PropositionalParser p = new PropositionalParser(f);
        Formula formula = null;
        try{
            formula = p.parse(ngString);
        } catch (org.logicng.io.parsers.ParserException e) {
            System.err.println("Not a valid LogicNG formula");
        }
        //System.out.println(formula);
        System.out.println("Running Quasi-Inconsistency detection for rule base:");
        System.out.println(base);
        formatter.setDebugMode(true);
        SATSolver glucose = MiniSat.glucose(f);
        glucose.add(formula);
        glucose.sat();
        System.out.println("Glucose: ");
        System.out.println(formatter.parse(glucose.model().toString()));
        MiniSatConfig miniSatConfig = MiniSatConfig.builder().proofGeneration(true).build();
        SATSolver solver = MiniSat.miniSat(f, miniSatConfig);
        solver.add(formula);
        solver.sat();
        /*
        List<Assignment> models;

        models = solver.enumerateAllModels();
        LinkedList<String> modelStrings = new LinkedList<>();
        models.forEach(model ->{
            String modelString = formatter.parse(model.toString());
            modelStrings.add(modelString);
            System.out.println("MiniSat (all models): ");
            System.out.println(modelStrings);
        });

         */
        System.out.println("MiniSat: \n");
        String miniSatOutput = solver.model().toString();
        String output = formatter.parse(miniSatOutput);
        System.out.println(miniSatOutput);
        System.out.println(output);

        //for outputting all return values and not just X1,X2,R1,R2


        //String re = DimacsSatSolver.convertToDimacs(satFormula);
        //System.out.println(re);

        /*
        if (unixOS) {
            // Using the SAT solver Lingeling
            CmdLineSatSolver lingelingSolver = new CmdLineSatSolver(lingeling_path);
            // add a cmd line parameter
            lingelingSolver.addOption("--reduce");
            String lingelingWitnessString = null;
            //Todo make this null safe..
            try{
                lingelingWitnessString = lingelingSolver.getWitness(satFormula).toString();
            } catch (NullPointerException e){
                System.out.println("Lingeling: not satisfiable");
            }
            if (lingelingWitnessString != null){
                System.out.println("Lingeling :");
                //System.out.println("Witness: " + lingelingWitnessString);
                System.out.println(formatter.parse(lingelingWitnessString));
            }


            // Using the SAT solver Kissat
            CmdLineSatSolver kissatSolver = new CmdLineSatSolver(kissat_path);
            // add a cmd line parameter
            //kissatSolver.addOption("--unsat");
            kissatSolver.addOption("--default");
            String kissatWitnessString = null;
            try {
                kissatWitnessString = kissatSolver.getWitness(satFormula).toString();
            } catch (NullPointerException e) {
                System.out.println("Kissat: not satisfiable");
            }
            if (kissatWitnessString != null){
                System.out.println("Kissat :");
                //System.out.println("Witness: " + kissatWitnessString);
                System.out.println(formatter.parse(kissatWitnessString));
            }
        } else {
            //Witnesses are not working for Windows OS?
            SatSolver.setDefaultSolver(new Sat4jSolver());
            SatSolver defaultSolver = SatSolver.getDefaultSolver();
            Object witness = defaultSolver.getWitness(satFormula);
            String witnessString = "UNSAT";
            if (witness != null){
                if (!witness.toString().equals("[]")){
                    witnessString = formatter.parse(witness.toString());
                }
            }
            //String witnessString = defaultSolver.getWitness(satFormula).toString();
            //System.out.println("\n" + defaultSolver.isSatisfiable(satFormula));
            System.out.println("Default Solver: " + defaultSolver.getClass());
            System.out.println("Witness:\n" + witnessString);
        }

         */
    }
}
