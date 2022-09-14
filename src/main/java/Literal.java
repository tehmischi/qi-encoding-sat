
public class Literal {

    private final String atomName;
    private final boolean negated;


    public Literal(String atomName, boolean negated) {
        this.atomName = atomName;
        this.negated = negated;
    }

    public String getAtomName(){
        return atomName;
    }

    public boolean getNegated(){
        return negated;
    }

    @Override
    public String toString(){
        String returnString;
        if (negated) {
            returnString =  "-" + atomName;
        } else {
            returnString = atomName;
        }
        return returnString;
    }
}
