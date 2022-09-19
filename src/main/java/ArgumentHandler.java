import java.util.HashMap;

public class ArgumentHandler {

    private final boolean unixOS;
    private String filePath;
    private boolean minimalActivationDetection;
    private final HashMap<String, String> solverLocations;


    public ArgumentHandler (String args[]){
        //TODO oben wieder final, weitere fixes..
        String os = System.getProperty("os.name");
        this.unixOS = !os.contains("Windows");
        solverLocations = new HashMap<>();
        if (args != null) {
            if (args.length == 0){
                System.out.println("No arguments detected. Setting default settings");
                minimalActivationDetection = false;
                System.out.println("Minimal Activation Set detection disabled.");
                if (unixOS) {
                    this.filePath = "/home/michael/satSolvers/RuleBase.txt";
                    System.out.println("Rule Base File Location: /home/michael/satSolvers/RuleBase.txt");
                    solverLocations.put("lingeling", "/home/michael/satSolvers/lingeling/lingeling");
                    solverLocations.put("kissat", "/home/michael/satSolvers/kissat/build/kissat");
                    solverLocations.forEach((id, path) ->{
                        System.out.println("Solver " + id + " at location " + path);
                    });
                } else {
                    this.filePath = "C:\\sat\\RuleBase2.txt";
                    System.out.println("Rule Base File Location: /home/michael/satSolvers/RuleBase.txt");
                    solverLocations.put("lingeling", "/home/michael/satSolvers/lingeling/lingeling");
                    solverLocations.put("kissat", "/home/michael/satSolvers/kissat/build/kissat");
                    solverLocations.forEach((id, path) ->{
                        System.out.println("Solver " + id + " at location " + path);
                    });
                }
            } else {

            }
        }

    }


}


