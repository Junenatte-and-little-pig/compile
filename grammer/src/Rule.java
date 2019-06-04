import java.io.*;
import java.util.*;

class Rule {

    private Map<Character, List<String>> rules;
    private Set<Character> nonTerminal;
    private Set<Character> terminal;
    private Map<Character, Boolean> toEmpty;
    private Map<Character, Set<Character>> firsts;
    private Map<Character, Set<Character>> follows;

    Rule(String path) {
        rules = new HashMap<>();
        nonTerminal = new HashSet<>();
        terminal = new HashSet<>();
        toEmpty = new HashMap<>();
        firsts = new HashMap<>();
        follows = new HashMap<>();
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
        initFirsts();
        initFollows();
    }

    Map<Character, List<String>> getRules() {
        return rules;
    }

    Set<Character> getNonTerminal() {
        return nonTerminal;
    }

    Set<Character> getTerminal() {
        return terminal;
    }

    Map<Character, Set<Character>> getFirsts() {
        return firsts;
    }

    Map<Character, Set<Character>> getFollows() {
        return follows;
    }

    private int getLength(Map<Character, Set<Character>> map) {
        int len = 0;
        for (Character c : map.keySet())
            len += map.get(c).size();
        return len;
    }

    private void getFirst(char nonT) {
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
                    if (firsts.keySet().contains(ch))
                        first.addAll(firsts.get(ch));
                    break;
                } else if ('@' == ch) {
                    first.add(ch);
                    i++;
                }
            }
        }
        firsts.put(nonT, first);
    }

    private Set<Character> getFirstByRule(String rule) {
        Set<Character> first = new HashSet<>();
        int i = 0;
        while (true) {
            Character c = rule.charAt(i);
            if (nonTerminal.contains(c)) {
                first.addAll(firsts.get(c));
                if (toEmpty.get(c))
                    i++;
                else
                    break;
            } else if (terminal.contains(c)) {
                first.add(c);
                break;
            } else if (c.equals('@')) {
                break;
            }
        }
        return first;
    }

    private void getFollow(Character nonT) {
        Set<Character> follow = new HashSet<>();
        if ('Z' == nonT)
            follow.add('#');
        for (Character c : nonTerminal)                     //对于每个非终结符
            for (String s : rules.get(c)) {                 //的每条产生规则
                int i = s.indexOf(nonT);                    //查找是否包含要的非终结符
                if (i >= 0) {                               //如果包含
                    i++;                                    //对后续内容进行判断
                    if (i == s.length())                       //如果后续为空
                        if (follows.keySet().contains(c))
                            follow.addAll(follows.get(c));        //将产生式规则左边的非终结符的follow集加进去
                    while (i < s.length()) {                //如果不为空
                        char ch = s.charAt(i);
                        if (terminal.contains(ch)) {       //判断是不是终结符
                            follow.add(ch);                 //是就加进去
                            break;
                        } else if (nonTerminal.contains(ch)) {
                            if (firsts.keySet().contains(ch)) {
                                follow.addAll(firsts.get(ch));
                                if (toEmpty.get(ch)) {
                                    i++;
                                    if (i == s.length())
                                        if (follows.keySet().contains(c))
                                            follow.addAll(follows.get(c));
                                } else
                                    break;
                            }
                        }
                    }
                }
            }
        follow.remove('@');
        follows.put(nonT, follow);
    }

    private void initFirsts() {
        boolean changed = true;
        while (changed) {
            changed = false;
            int length = getLength(firsts);
            for (Character c : nonTerminal) {
                if (!toEmpty.keySet().contains(c))
                    toEmpty.put(c, false);
                getFirst(c);
            }
            if (length < getLength(firsts))
                changed = true;
        }
    }

    private void initFollows() {
        boolean changed = true;
        while (changed) {
            changed = false;
            int length = getLength(follows);
            for (Character c : nonTerminal)
                getFollow(c);
            if (length < getLength(follows))
                changed = true;
        }
    }

    void printTable() {
        for (Character c : nonTerminal) {
            for (String s : rules.get(c)) {
                if (s.equals("@"))
                    for (Character ch : follows.get(c)) {
                        System.out.println("M[" + c + ", " + ch + "]:" + c + "->" + s);
                    }
                for (Character ch : getFirstByRule(s)) {
                    System.out.println("M[" + c + ", " + ch + "]:" + c + "->" + s);
                }
            }
        }
    }
}
