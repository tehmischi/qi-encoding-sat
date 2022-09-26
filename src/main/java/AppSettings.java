import org.logicng.formulas.FormulaFactory;

public class AppSettings {

    private static FormulaFactory threadSafeFormulaFactory;
    //TODO Implement negativeSign
    private final static String negativeSign = "_neg_";
    private static final boolean debugMode = false;

    private AppSettings(){}

    public static synchronized FormulaFactory getFormulaFactory(){
        if(threadSafeFormulaFactory == null){
            threadSafeFormulaFactory = new FormulaFactory();
        }
        return threadSafeFormulaFactory;
    }

    public static String getNegativeSign(){
        return negativeSign;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }
}
