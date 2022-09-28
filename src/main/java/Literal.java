import java.util.Objects;

public record Literal(String atomName, boolean negated) {

    @Override
    public String toString() {
        String returnString;
        if (negated) {
            returnString = "n" + atomName;
        } else {
            returnString = atomName;
        }
        return returnString;
    }

    @Override
    public boolean equals(Object object){
        boolean returnValue = false;
        if (object instanceof Literal lit){
            returnValue = (lit.negated == this.negated) && (this.atomName.equals(lit.atomName));
        }
        return returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(atomName, negated);
    }
}
