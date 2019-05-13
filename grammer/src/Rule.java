import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Rule {
    private Map<Character, List<String>> rules;
    private Set<Character> nonTerminal;
    private Set<Character> terminal;
    private Map<Character, Boolean> toEmpty;
    private Map<Character,Set<Character>> firsts;
    private Map<Character,Set<Character>> follows;

    Rule(String path) {
        rules = new HashMap<>();
        nonTerminal = new HashSet<>();
        terminal = new HashSet<>();
        toEmpty = new HashMap<>();
        firsts=new HashMap<>();
        follows=new HashMap<>();
        try {
            File f = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while (true) {
                line = br.readLine();
                if (line == null)
                    break;
                String[] ls = line.split("->");
                String[] rs = ls[1].split("\\|");
                List<String> r = new ArrayList<>(Arrays.asList(rs));
                rules.put(ls[0].charAt(0), r);
            }
            nonTerminal = rules.keySet();
            for (Character c : nonTerminal) {
                List<String> rule = rules.get(c);
                for (String str : rule) {
                    if (str.equals("@")) {
                        toEmpty.put(c, true);
                        continue;
                    }
                    for (char ch : str.toCharArray())
                        if (!nonTerminal.contains(ch) && ch != '@')
                            terminal.add(ch);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Character c : nonTerminal) {
            if (!toEmpty.keySet().contains(c))
                toEmpty.put(c, false);
        }
    }

    void printRules() {
        for (Character c : nonTerminal)
            for (String s : rules.get(c))
                System.out.println(c.toString() + "->" + s);
    }

    void printTerminal() {
        for (Character c : terminal)
            System.out.println(c);
    }

    void printToEmpty() {
        for (Character c : toEmpty.keySet())
            System.out.println(c + ":\t" + toEmpty.get(c));
    }

    public Set<Character> getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(Set<Character> nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public Set<Character> getTerminal() {
        return terminal;
    }

    public void setTerminal(Set<Character> terminal) {
        this.terminal = terminal;
    }

    void getFirst(char nonT) {
        Set<Character> first = new HashSet<>();
        List<String> rule = rules.get(nonT);
        for (String s : rule) {
            int i = 0;
            while (i < s.length()) {
                char ch = s.charAt(i);
                if (terminal.contains(ch)) {
                    first.add(ch);
                    break;
                } else if (nonTerminal.contains(ch)) {
                    first.addAll(firsts.get(ch));
                    break;
                } else if ('@' == ch) {
                    first.add(ch);
                    i++;
                }
            }
        }
        firsts.put(nonT,first);
    }

    Set<Character> getFollow(Character nonT) {
        Set<Character> follow = new HashSet<>();
        if ('Z' == nonT)
            follow.add('#');
        for (Character c : nonTerminal)                     //对于每个非终结符
            for (String s : rules.get(c)) {                 //的每条产生规则
                int i = s.indexOf(nonT);                    //查找是否包含要的非终结符
                if (i >= 0) {                               //如果包含
                    i++;                                    //对后续内容进行判断
                    if (i == s.length())                       //如果后续为空
                        follow.addAll(getFollow(c));        //将产生式规则左边的非终结符的follow集加进去
                    while (i < s.length()) {                //如果不为空
                        char ch = s.charAt(i);
                        if (terminal.contains(ch)) {       //判断是不是终结符
                            follow.add(ch);                 //是就加进去
                            break;
                        } else if (nonTerminal.contains(ch)) {
                            follow.addAll(firsts.get(ch));
                            if (toEmpty.get(ch))
                                i++;
                            else
                                break;
                        }
                    }
                }
            }
        follow.remove('@');
        return follow;
    }
}
