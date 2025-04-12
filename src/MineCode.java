import java.io.*;
import java.util.*;
import java.nio.file.Files;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

public class MineCode {
    static HashMap<String, String> variables = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java MineCode <program.txt>");
            return;
        }
        runProgram(args[0]);
    }

    public static void runProgram(String filename) throws Exception {
        List<String> lines = new ArrayList<>(Files.readAllLines(new File(filename).toPath()));
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            if (line.startsWith("PLACE ")) {
                String key = line.substring(6).trim();
                System.out.println(variables.getOrDefault(key, key));
            } else if (line.startsWith("CRAFT ")) {
                String var = line.substring(6).trim();
                System.out.print("> ");
                variables.put(var, scanner.nextLine());
            } else if (line.startsWith("MINE ")) {
                String[] parts = line.substring(5).split("=");
                String var = parts[0].trim();
                String expr = parts[1].trim();
                variables.put(var, eval(expr));
            } else if (line.startsWith("OVERWORLD ")) {
                String[] parts = line.split(" ");
                String src = parts[1];
                String dest = parts[3];
                String reversed = new StringBuilder(variables.get(src)).reverse().toString();
                variables.put(dest, reversed);
            } else if (line.startsWith("BUILD ")) {
                String[] parts = line.split(" ");
                String ch = variables.getOrDefault(parts[1], parts[1]);
                int times = Integer.parseInt(variables.getOrDefault(parts[2], parts[2]));
                StringBuilder sb = new StringBuilder();
                sb.append(ch.repeat(times));
                variables.put(parts[4], sb.toString());
            }
        }
    }

    private static String eval(String expr) {
        for (String key : variables.keySet()) {
            expr = expr.replace(key, variables.get(key));
        }

        try {
            return Integer.toString((int) new ScriptEngineManager()
                    .getEngineByName("JavaScript")
                    .eval(expr));
        } catch (Exception e) {
            return expr;
        }
    }
}
