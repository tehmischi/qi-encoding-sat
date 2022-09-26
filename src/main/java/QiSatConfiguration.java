import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;


public class QiSatConfiguration {

    private String filePath;
    private String solver;
    private boolean debugMode;
    private boolean csvMode;

    public QiSatConfiguration(){
        //setting default values
        this.filePath = "examples/RuleBase.txt";
        this.solver = "glucose";
        this.debugMode = false;
        this.csvMode = false;
    }

    public String getFilePath() {
        return filePath;
    }

    public InputFileParser getFileParser(){
        InputFileParser returnParser;
        if (csvMode){
            returnParser = new DeclareModelParser(filePath);
        } else {
            returnParser = new BusinessRuleFileParser(filePath);
        }
        return returnParser;
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

    public void setCsvMode (boolean active){
        this.csvMode = active;
    }
}
