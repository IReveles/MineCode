import java.io.*;
import java.util.*;
import java.nio.file.Files;

public class MineCode {
    static HashMap<String, String> variables = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java MineCode <program.mc>");
            return;
        }
        runProgram(args[0]);
    }

    public static void runProgram(String filename) throws Exception {
        List<String> lines = new ArrayList<>(Files.readAllLines(new File(filename).toPath()));
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            if (line.startsWith("PLACE ")) {
                String key = line.substring(6).trim();
                System.out.println(variables.getOrDefault(key, key));
            } else if (line.startsWith("CRAFT ")) {
                String var = line.substring(6).trim();
                System.out.print("> ");
                variables.put(var, scanner.nextLine());
            } else if (line.startsWith("SMELT ")) {
                String[] parts = line.split(" ");
                int a = Integer.parseInt(variables.get(parts[1]));
                int b = Integer.parseInt(variables.get(parts[2]));
                String dest = parts[4];
                variables.put(dest, Integer.toString(a + b));
            } else if (line.startsWith("COOK ")) {
                String[] parts = line.split(" ");
                int a = Integer.parseInt(variables.get(parts[1]));
                int b = Integer.parseInt(variables.get(parts[2]));
                String dest = parts[4];
                variables.put(dest, Integer.toString(a - b));
            } else if (line.startsWith("ENCHANT ")) {
                String[] parts = line.split(" ");
                int a = Integer.parseInt(variables.get(parts[1]));
                int b = Integer.parseInt(variables.get(parts[2]));
                String dest = parts[4];
                variables.put(dest, Integer.toString(a * b));
            } else if (line.startsWith("ANVIL ")) {
                String[] parts = line.split(" ");
                int a = Integer.parseInt(variables.get(parts[1]));
                int b = Integer.parseInt(variables.get(parts[2]));
                String dest = parts[4];
                int result = b != 0 ? a / b : 0;
                variables.put(dest, Integer.toString(result));
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
}
