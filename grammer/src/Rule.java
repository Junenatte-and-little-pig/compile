import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Rule {
    private Map<Character, String> rules;
    private Set<Character> nonTerminal;
    private Set<Character> terminal;

    public Rule() {
        rules = new HashMap<>();
        nonTerminal = new HashSet<>();
        terminal = new HashSet<>();
    }

    Rule(String path) {
        rules = new HashMap<>();
        nonTerminal = new HashSet<>();
        terminal = new HashSet<>();
        try {
            File f = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while (true) {
                line = br.readLine();
                if (line == null)
                    break;
                String[] ls = line.split("->");
                rules.put(ls[0].charAt(0), ls[1]);
            }
            nonTerminal = rules.keySet();
            for (Character c : nonTerminal) {
                String rule = rules.get(c);
                for (int i = 0; i < rule.length(); i++) {
                    char ch = rule.charAt(i);
                    if (!nonTerminal.contains(ch) && ch != '@' && ch != '|')
                        terminal.add(ch);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printRules() {
        for (Character c : nonTerminal)
            System.out.println(c.toString() + "->" + rules.get(c));
    }
    public void printTerminal(){
        for(Character c:terminal)
            System.out.println(c);
    }
}
