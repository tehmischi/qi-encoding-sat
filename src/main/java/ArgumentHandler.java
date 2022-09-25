
public class ArgumentHandler {

    private final boolean unixOS;
    private String filePath;


    public ArgumentHandler (String[] args){
        //TODO oben wieder final, weitere fixes..
        String os = System.getProperty("os.name");
        this.unixOS = !os.contains("Windows");
        if (args != null) {
            if (args.length == 0){
                System.out.println("No arguments detected. Setting default settings");
                if (unixOS) {
                    this.filePath = "/home/michael/satSolvers/RuleBase.txt";
                    System.out.println("Rule Base File Location: /home/michael/satSolvers/RuleBase.txt");
                } else {
                    this.filePath = "C:\\sat\\RuleBase2.txt";
                    System.out.println("Rule Base File Location: /home/michael/satSolvers/RuleBase.txt");
                }
            } else {

            }
        }

    }


}


