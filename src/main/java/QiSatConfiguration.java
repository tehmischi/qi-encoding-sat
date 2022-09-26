import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

import java.io.File;
import java.io.FileNotFoundException;

public class QiSatConfiguration {

    private String filePath;
    private final String solver;

    public QiSatConfiguration(String[] args) throws FileNotFoundException {
        //TODO argument handling, for now just for -f File parameter..
        boolean unixOS = !System.getProperty("os.name").contains("Windows");
        //TODO hier Option einfügen auch upper lowercase entfernen
        this.solver = "glucose";
        String manualFilePath = null;
        String autoFilePath;
        if (args.length > 1) {
            if (args[0].equals("-f")) {
                manualFilePath = args[1];
            }
        }
        if (unixOS) {
            autoFilePath= "/home/michael/satSolvers/RuleBase.txt";
        } else {
            autoFilePath= "C:\\sat\\RuleBase.txt";
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
    }

    public String getFilePath() {
        return filePath;
    }

    public SATSolver getSolver() {
        SATSolver satSolver;
        switch (solver) {
            case "glucose" -> satSolver = MiniSat.glucose(AppSettings.getFormulaFactory());
            case "minisat" -> satSolver = MiniSat.miniSat(AppSettings.getFormulaFactory());
            case "minicard" -> satSolver = MiniSat.miniCard(AppSettings.getFormulaFactory());
            default -> satSolver = MiniSat.glucose(AppSettings.getFormulaFactory());
        }
        return satSolver;
    }

    public String getSolverName() {
        String solverName;
        switch (solver) {
            case "glucose" -> solverName = "Glucose";
            case "minisat" -> solverName = "MiniSat";
            case "minicard" -> solverName = "MiniCARD";
            default -> solverName = "Glucose";
        }
        return solverName;
    }
}
