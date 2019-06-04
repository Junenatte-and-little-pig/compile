import java.io.*;
import java.util.*;

class Rule {
    private Map<Character, List<String>> rules;
    private Set<Character> nonTerminal;
    private Set<Character> terminal;
    private Map<Character, Boolean> toEmpty;
    private Map<Character, Set<Character>> firstVTs;
    private Map<Character, Set<Character>> lastVTs;

    Rule(String path) {
        rules = new HashMap<>();
        nonTerminal = new HashSet<>();
        terminal = new HashSet<>();
        toEmpty = new HashMap<>();
        firstVTs = new HashMap<>();
        lastVTs = new HashMap<>();
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
        initFirstsVT();
        initFollowsVT();
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

    Map<Character, Set<Character>> getFirstVTs() {
        return firstVTs;
    }

    Map<Character, Set<Character>> getlastVTs() {
        return lastVTs;
    }

    private int getLength(Map<Character, Set<Character>> map) {
        int len = 0;
        for (Character c : map.keySet())
            len += map.get(c).size();
        return len;
    }

    private void getFirstVT(char nonT) {
        Set<Character> firstVT = new HashSet<>();
        List<String> rule = rules.get(nonT);
        for (String s : rule) {
            int i = 0;
            if (i < s.length()) {
                char ch = s.charAt(i);
                if (terminal.contains(ch)) {
                    firstVT.add(ch);
                } else if (nonTerminal.contains(ch)) {
                    if (firstVTs.keySet().contains(ch))
                        firstVT.addAll(firstVTs.get(ch));
                    if (i + 1 < s.length()) {
                        ch = s.charAt(i + 1);
                        if (terminal.contains(ch))
                            firstVT.add(ch);
                    }
                }
            }
        }
        firstVTs.put(nonT, firstVT);
    }

    private Set<Character> getFirstVTByRule(String rule) {
        Set<Character> firstVT = new HashSet<>();
        int i = 0;
        while (true) {
            Character c = rule.charAt(i);
            if (nonTerminal.contains(c)) {
                firstVT.addAll(firstVTs.get(c));
                if (toEmpty.get(c))
                    i++;
                else
                    break;
            } else if (terminal.contains(c)) {
                firstVT.add(c);
                break;
            } else if (c.equals('@')) {
                break;
            }
        }
        return firstVT;
    }

    private void getlastVT(Character nonT) {
        Set<Character> lastVT = new HashSet<>();
        List<String> rule = rules.get(nonT);
        for (String s : rule) {
            int i = s.length() - 1;
            if (i >= 0) {
                char ch = s.charAt(i);
                if (terminal.contains(ch)) {
                    lastVT.add(ch);
                } else if (nonTerminal.contains(ch)) {
                    if (lastVTs.keySet().contains(ch))
                        lastVT.addAll(lastVTs.get(ch));
                    if (i - 1 >= 0) {
                        ch = s.charAt(i - 1);
                        if (terminal.contains(ch))
                            lastVT.add(ch);
                    }
                }
            }
        }
        lastVTs.put(nonT, lastVT);
    }

    private void initFirstsVT() {
        boolean changed = true;
        while (changed) {
            changed = false;
            int length = getLength(firstVTs);
            for (Character c : nonTerminal) {
                if (!toEmpty.keySet().contains(c))
                    toEmpty.put(c, false);
                getFirstVT(c);
            }
            if (length < getLength(firstVTs))
                changed = true;
        }
    }

    private void initFollowsVT() {
        boolean changed = true;
        while (changed) {
            changed = false;
            int length = getLength(lastVTs);
            for (Character c : nonTerminal)
                getlastVT(c);
            if (length < getLength(lastVTs))
                changed = true;
        }
    }

    void findUsages() {
        for (Character c : nonTerminal)
            for (String s : rules.get(c))
                for (Character ch : s.toCharArray())
                    if (terminal.contains(ch))
                        System.out.println(ch + " in " + s);
    }

    void printTable() {
        for (Character c : nonTerminal)
            for (String s : rules.get(c))
                for (Character ch : s.toCharArray())
                    if (terminal.contains(ch)) {
                        int index = s.indexOf(ch);
                        if (index + 1 < s.length()) {
                            char after = s.charAt(index - 1);
                            if (nonTerminal.contains(after)) {
                                for(Character first:firstVTs.get(after))
                                    System.out.println(ch+"<"+first);
                                if(index+2<s.length()&&terminal.contains(s.charAt(index+2)))
                                    System.out.println(ch+"="+after);
                            } else if (terminal.contains(after))
                                System.out.println(ch+"="+after);
                        }
                    }else if(nonTerminal.contains(ch)){
                        int index=s.indexOf(ch);
                        if(index+1<s.length()&&terminal.contains(s.charAt(index+1)))
                            for(Character last:lastVTs.get(ch))
                                System.out.println(last+">"+ch);
                    }
    }
}
