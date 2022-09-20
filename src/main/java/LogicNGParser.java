public class LogicNGParser {

    public String parse (String tweetyFormulaString){
        return "(" + tweetyFormulaString
                        .replaceAll("\\.", "")
                        .replaceAll(",", ")&(")
                        .replaceAll("\\{", "")
                        .replaceAll("}", "")
                        .replaceAll("\\|\\|", "|")
                        .replaceAll("&&", "&")
                        .replaceAll("-", "n")
                        .replaceAll(" ", "")
                        .replaceAll("!", "~") + ")";
    }
}
