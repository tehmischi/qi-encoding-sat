
import java.io.File;
import java.util.Optional;

public class ArgumentHandler {

    private final QiSatConfiguration config = AppSettings.getConfig();

    public ArgumentHandler (String[] args){
        if (args.length == 0) {
            System.out.println("No input arguments detected. Setting default settings");
        }
        else {
            for (int i = 0; i < args.length; i++){
                switch (args[i]) {
                    case "-f" -> {
                        i++;
                        handleFile(args[i]);
                    }
                    case "-dimacs" -> {
                        i++;
                        cnfGen(args[i]);
                    }
                    case "-solver" -> {
                        i++;
                        handleSolver(args[i]);
                    }
                    case "--debug" -> AppSettings.getConfig().setDebugMode(true);
                    default -> {
                        System.err.println(args[i] + "is not a valid argument and was ignored!");
                        System.err.println("Valid arguments are: -f FilePath, -solver SolverName, --debug, -dimacs FilePath");
                    }
                }
            }

        }
        System.out.println("SAT solver: " + config.getSolverName());
        System.out.println("File path: " + config.getFilePath());
        if (config.isDebugMode()){
            System.out.println("Debug mode: " + config.isDebugMode());
        }
        System.out.println();
    }


    private void handleFile (String fileName){
        String fileExtension = getFileExtension(fileName).get();
        if (new File(fileName).isFile()){
            config.setFilePath(fileName);
            if (fileExtension.equals("csv")){
                config.setCsvMode(true);
            }
        } else {
            System.err.println("The specified file path " + fileName + " does not exist. Reverting to default.");
        }
    }

    private void handleSolver (String solver){
        solver = solver.toLowerCase();
        if (solver.equals("glucose") || solver.equals("minisat") || solver.equals("minicard")){
            config.setSolverName(solver);
        } else {
            System.err.println("The specified solver " + solver + " does not exist. Reverting to default (Glucose).");
        }
    }

    private Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    private void cnfGen(String filename){
        config.setCnfMode(true);
        config.setOutputFilePath(filename);
    }
}


