import java.util.*;

public class OutputStringFormatter {

    private boolean debugMode = false;
    private final RuleBase ruleBase;

    public OutputStringFormatter (RuleBase ruleBase) {
        this.ruleBase = ruleBase;
    }

    public String parse (String inputString) {
        HashMap<String, TreeSet<String>> returnList = new HashMap<>();
        returnList.put("X1", new TreeSet<>());
        returnList.put("X2", new TreeSet<>());
        returnList.put("R1", new TreeSet<>());
        returnList.put("R2", new TreeSet<>());
        returnList.put("debug", new TreeSet<>());
        String[] seperated = inputString.split(",");
        for (String value : seperated){
            value = value.trim().replaceAll("[\\[\\]]", "");
            String id = (value.length()>3)? value.substring(0,3):"";
            String item = (value.length()>3)? value.substring(3):"";
            item = item.trim().replaceAll("[\\[\\].]","");
            switch (id) {
                case "x_1" -> returnList.get("X1").add(item);
                case "x_2" -> returnList.get("X2").add(item);
                case "r_1" -> {
                    String rule = ruleBase.getRuleBase().get(item).toString();
                    returnList.get("R1").add(rule);
                }
                case "r_2" -> {
                    String rule = ruleBase.getRuleBase().get(item).toString();
                    returnList.get("R2").add(rule);
                }
                default -> returnList.get("debug").add(value);
            }
        }
        StringBuilder line = new StringBuilder();
        formatLine("X1", returnList.get("X1"), line);
        formatLine("X2", returnList.get("X2"), line);
        formatLine("R1", returnList.get("R1"), line);
        formatLine("R2", returnList.get("R2"), line);
        if (debugMode){
            formatLine("debug", returnList.get("debug"), line);
        }
        return line.toString();
    }

    private void formatLine (String id, TreeSet<String> tree, StringBuilder line) {
        line.append(id).append(": {");
        tree.forEach(value ->{
            if (id.startsWith("X")) {
                line.append(value).append(", ");
            } else {
                line.append(value).append("; ");
            }
        });
        boolean empty = line.charAt(line.length()-1) == '{';
        if (!empty) {
            line.setLength(line.length() - 2);
        }
        line.append("}" + "\n");
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

}
