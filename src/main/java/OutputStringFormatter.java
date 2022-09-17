import javax.ws.rs.core.Link;
import java.util.*;

public class OutputStringFormatter {

    private boolean debugMode = false;
    private boolean fullRules = true;
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
            value = value.trim();
            String id = value.substring(0,3);
            String item = value.substring(3);
            item = item.trim().replaceAll("[\\[\\]\\.]","");
            if (id.equals("x_1")) {
                returnList.get("X1").add(item);
            } else if (id.equals("x_2")){
                returnList.get("X2").add(item);
            } else if (id.equals("r_1")){
                String rule = ruleBase.getRuleBase().get(item).toString();
                returnList.get("R1").add(rule);
            } else if (id.equals("r_2")){
                String rule = ruleBase.getRuleBase().get(item).toString();
                returnList.get("R2").add(rule);
            } else {
                returnList.get("debug").add(value.trim().replaceAll("[\\[\\]]",""));
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
        String result = line.toString();
        return result;
    }

    private void formatLine (String id, TreeSet<String> tree, StringBuilder line) {
        line.append(id + ": {");
        tree.forEach(value ->{
            if (id.startsWith("X")) {
                line.append(value + ", ");
            } else {
                line.append(value + "; ");
            }
        });
        line.setLength(line.length() - 2);
        line.append("}" + "\n");
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void setFullRules(boolean fullRules) {
        this.fullRules = fullRules;
    }


}
