import org.logicng.formulas.FormulaFactory;

public class Application {

    private static FormulaFactory threadSafeFormulaFactory;
    private final static String negativeSign = "_neg_";

    private Application(){}

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
