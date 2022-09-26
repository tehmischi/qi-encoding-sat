import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;


public class QiSatConfiguration {

    private String filePath;
    private String solver;
    private boolean debugMode;

    public QiSatConfiguration(){
        //setting default values
        this.filePath = "examples/RuleBase.txt";
        this.solver = "glucose";
        this.debugMode = false;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
            default -> solverName = solver;
        }
        return solverName;
    }

    public void setSolverName (String solverName){
        this.solver = solverName.toLowerCase();
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}
