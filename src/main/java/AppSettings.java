import org.logicng.formulas.FormulaFactory;

public class AppSettings {

    private static FormulaFactory threadSafeFormulaFactory;
    private final static String negativeSign = "_neg_";

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
}
