        /*
         *  This file is part of "TweetyProject", a collection of Java libraries for
         *  logical aspects of artificial intelligence and knowledge representation.
         *
         *  TweetyProject is free software: you can redistribute it and/or modify
         *  it under the terms of the GNU Lesser General Public License version 3 as
         *  published by the Free Software Foundation.
         *
         *  This program is distributed in the hope that it will be useful,
         *  but WITHOUT ANY WARRANTY; without even the implied warranty of
         *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
         *  GNU Lesser General Public License for more details.
         *
         *  You should have received a copy of the GNU Lesser General Public License
         *  along with this program. If not, see <http://www.gnu.org/licenses/>.
         *
         *  Copyright 2016 The TweetyProject Team <http://tweetyproject.org/contact/>
         */

        import org.tweetyproject.commons.ParserException;
        import org.tweetyproject.logics.pl.sat.CmdLineSatSolver;
        import org.tweetyproject.logics.pl.sat.Sat4jSolver;
        import org.tweetyproject.logics.pl.sat.SatSolver;
        import org.tweetyproject.logics.pl.syntax.*;

        import java.io.File;
        import java.io.FileNotFoundException;

        /**
 *
 */
public class MainTest {

    private static String lingeling_path;
    private static String kissat_path;

    public static void main(String[] args) throws ParserException, FileNotFoundException {
        //TODO argument handling, for now just for -f File parameter..
        System.out.println(args.length);
        String manualFilePath = null;
        if (args.length > 1) {
            System.out.println(args[0]);
            if (args[0].equals("-f")) {
                manualFilePath = args[1];
            }
        }
        String os = System.getProperty("os.name");
        boolean unixOS = !os.contains("Windows");
        BusinessRuleFileParser parser;
        String autoFilePath;
        String filePath = null;
        if (unixOS) {
            autoFilePath= "/home/michael/satSolvers/RuleBase.txt";
            MainTest.lingeling_path = "/home/michael/satSolvers/lingeling/lingeling";
            MainTest.kissat_path = "/home/michael/satSolvers/kissat/build/kissat";
        } else {
            autoFilePath= "C:\\sat\\RuleBase.txt";
            MainTest.lingeling_path = "C:/sat/lingeling/lingeling.exe";
            MainTest.kissat_path = "C:/sat/kissat/build/kissat.exe";
        }
        if (manualFilePath != null) {
            if (new File(manualFilePath).isFile()){
                filePath = manualFilePath;
            } else {
                System.err.println("The file path that was supplied with argument -f is not correct.");
                System.err.println(manualFilePath);
            }
        } else if (new File(autoFilePath).isFile()) {
            filePath = autoFilePath;
        } else {
            throw new FileNotFoundException("No input File Path was specified with argument -f and the automatic path is not correct: " + autoFilePath);
        }
        parser = new BusinessRuleFileParser(filePath);
        RuleBase base = parser.readFile();
        PlBeliefSet kb1 = new PlBeliefSet();
        SetInclusionEncoding setInclusion = new SetInclusionEncoding(base);
        ConsistencyEncoding consistencyEncoding = new ConsistencyEncoding(base);
        ActivationEncoding activationEncoding = new ActivationEncoding(base);
        //consistencyEncoding.addConsistencyRestraints(kb1);
        //setInclusion.addSetInclusionConstraints(kb1);
        activationEncoding.addActivationConstraints(kb1);
        System.out.println("Input: " + kb1);
        //System.out.println("CNF: " + kb1.toCnf() + "\n");
        OutputStringFormatter formatter = new OutputStringFormatter(base);

        //for outputting all return values and not just X1,X2,R1,R2
        formatter.setDebugMode(true);

        //String re = DimacsSatSolver.convertToDimacs(kb1);
        //System.out.println(re);

        if (unixOS) {
            // Using the SAT solver Lingeling
            CmdLineSatSolver lingelingSolver = new CmdLineSatSolver(lingeling_path);
            // add a cmd line parameter
            lingelingSolver.addOption("--reduce");
            String lingelingWitnessString = null;
            //Todo make this null safe..
            try{
                lingelingWitnessString = lingelingSolver.getWitness(kb1).toString();
            } catch (NullPointerException e){
                System.out.println("Lingeling: not satisfiable");
            }
            if (lingelingWitnessString != null){
                System.out.println("Lingeling : \n");
                System.out.println("Witness: " + lingelingWitnessString);
                System.out.println(formatter.parse(lingelingWitnessString));
            }


            // Using the SAT solver Kissat
            CmdLineSatSolver kissatSolver = new CmdLineSatSolver(kissat_path);
            // add a cmd line parameter
            //kissatSolver.addOption("--unsat");
            kissatSolver.addOption("--default");
            String kissatWitnessString = null;
            try {
                kissatWitnessString = kissatSolver.getWitness(kb1).toString();
            } catch (NullPointerException e) {
                System.out.println("Kissat: not satisfiable");
            }
            if (kissatWitnessString != null){
                System.out.println("Kissat : \n");
                System.out.println("Witness: " + kissatWitnessString);
                System.out.println(formatter.parse(kissatWitnessString));
            }
        } else {
            //Witnesses are not working for Windows OS?
            SatSolver.setDefaultSolver(new Sat4jSolver());
            SatSolver defaultSolver = SatSolver.getDefaultSolver();
            Object witness = defaultSolver.getWitness(kb1);
            String witnessString = "UNSAT";
            if (witness != null){
                if (!witness.toString().equals("[]")){
                    witnessString = formatter.parse(witness.toString());;
                }
            }
            //String witnessString = defaultSolver.getWitness(kb1).toString();
            //System.out.println("\n" + defaultSolver.isSatisfiable(kb1));
            CmdLineSatSolver kissatSolver = new CmdLineSatSolver(kissat_path);
            System.out.println("Default Solver: " + defaultSolver.getClass());
            System.out.println("Witness:\n" + witnessString);
        }
    }
}
