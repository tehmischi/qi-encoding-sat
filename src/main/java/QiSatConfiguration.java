import java.io.File;
import java.io.FileNotFoundException;

public class QiSatConfiguration {

    private String filePath;
    private final boolean minimalSearchActive;

    public QiSatConfiguration(String[] args) throws FileNotFoundException {
        //TODO argument handling, for now just for -f File parameter..
        minimalSearchActive = true;
        boolean unixOS = !System.getProperty("os.name").contains("Windows");
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
            autoFilePath= "C:\\sat\\RuleBase2.txt";
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

    public boolean isMinimalSearchActive() {
        return minimalSearchActive;
    }
}
