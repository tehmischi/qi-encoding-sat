public record Literal(String atomName, boolean negated) {

    @Override
    public String toString() {
        String returnString;
        if (negated) {
            returnString = "-" + atomName;
        } else {
            returnString = atomName;
        }
        return returnString;
    }

    public boolean isNegated (){
        return negated;
    }
}
