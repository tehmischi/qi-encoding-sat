import javax.ws.rs.core.Link;
import java.util.*;

public class OutputStringFormatter {

    private boolean debugMode = false;

    public OutputStringFormatter () {

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
            System.out.println("Value ist: " + value);
            String id = value.substring(0,3);
            String item = value.substring(3);
            item = item.trim().replaceAll("[\\[\\]\\.]","");
            System.out.println("ID ist: " + id);//removes all brackets, points, etc..
            if (id.equals("x_1")) {
                returnList.get("X1").add(item);
            } else if (id.equals("x_2")){
                returnList.get("X2").add(item);
            } else if (id.equals("r_1")){
                returnList.get("R1").add("r" + item);
            } else if (id.equals("r_2")){
                returnList.get("R2").add("r" + item);
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
        return line.toString();
    }

    private void formatLine (String id, TreeSet<String> tree, StringBuilder line) {
        line.append(id + ": {");
        tree.forEach(value ->{
            line.append(value + ", ");
        });
        line.setLength(line.length() - 2);
        line.append("}" + "\n");
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}
