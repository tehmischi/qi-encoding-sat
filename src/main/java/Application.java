import org.logicng.formulas.FormulaFactory;

public class Application {

    private static FormulaFactory threadSafeFormulaFactory;

    private Application(){}

    public static synchronized FormulaFactory getFormulaFactory(){
        if(threadSafeFormulaFactory == null){
            threadSafeFormulaFactory = new FormulaFactory();
        }
        return threadSafeFormulaFactory;
    }
}
