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

        import java.io.IOException;


        import org.tweetyproject.commons.ParserException;
        import org.tweetyproject.logics.pl.sat.CmdLineSatSolver;
        import org.tweetyproject.logics.pl.sat.DimacsSatSolver;
        import org.tweetyproject.logics.pl.sat.Sat4jSolver;
        import org.tweetyproject.logics.pl.sat.SatSolver;
        import org.tweetyproject.logics.pl.syntax.*;

/**
 *
 */
public class MainTest {

    private static String lingeling_path;
    private static String kissat_path;

    public static void main(String[] args) throws ParserException, IOException {
        String os = System.getProperty("os.name");
        boolean unixOS = !os.contains("Windows");
        BusinessRuleFileParser parser;
        if (unixOS) {
            parser = new BusinessRuleFileParser("/home/michael/satSolvers/RuleBase.txt");
            MainTest.lingeling_path = "/home/michael/satSolvers/lingeling/lingeling";
            MainTest.kissat_path = "/home/michael/satSolvers/kissat/build/kissat";
        } else {
            parser = new BusinessRuleFileParser("C:\\sat\\RuleBase.txt");
            MainTest.lingeling_path = "C:/sat/lingeling/lingeling.exe";
            MainTest.kissat_path = "C:/sat/kissat/build/kissat.exe";
        }
        RuleBase base = parser.readFile();
        PlBeliefSet kb1 = new PlBeliefSet();
        SetInclusionEncoding setInclusion = new SetInclusionEncoding(base);
        ConsistencyEncoding consistencyEncoding = new ConsistencyEncoding(base);
        ActivationEncoding activationEncoding = new ActivationEncoding(base);
        kb1 = consistencyEncoding.addConsistencyRestraints(kb1);
        kb1 = setInclusion.addSetInclusionConstraints(kb1);
        activationEncoding.addActivationConstraints(kb1);
        kb1.add(new Negation(new Proposition("x_1,a")));
        kb1.add(new Proposition("r_1,1"));

        System.out.println("Input: " + kb1);
        System.out.println("CNF: " + kb1.toCnf() + "\n");

        String re = DimacsSatSolver.convertToDimacs(kb1);
        System.out.println(re);

        if (unixOS) {
            // Using the SAT solver Lingeling
            CmdLineSatSolver lingelingSolver = new CmdLineSatSolver(lingeling_path);
            // add a cmd line parameter
            lingelingSolver.addOption("--reduce");
            System.out.println("\n" + "Lingeling: " + lingelingSolver.isSatisfiable(kb1));
            //System.out.println("Witness: " + lingelingSolver.getWitness(kb1));

            // Using the SAT solver Kissat
            CmdLineSatSolver kissatSolver = new CmdLineSatSolver(kissat_path);
            // add a cmd line parameter
            kissatSolver.addOption("--unsat");
            System.out.println("\n" + "Kissat: " + kissatSolver.isSatisfiable(kb1));
            //System.out.println("Witness: " + kissatSolver.getWitness(kb1));
        } else {
            SatSolver.setDefaultSolver(new Sat4jSolver());
            SatSolver defaultSolver = SatSolver.getDefaultSolver();
            System.out.println("\n" + defaultSolver.isSatisfiable(kb1));
            System.out.println("Witness: " + defaultSolver.getWitness(kb1));
        }
    }
}
