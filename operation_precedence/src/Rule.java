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
import java.util.Stack;

class Rule {
    private Map<Character, List<String>> rules;
    private Set<Character> nonTerminal;
    private Set<Character> terminal;
    private Map<Character, Boolean> toEmpty;
    private Map<Character, Set<Character>> firstVTs;
    private Map<Character, Set<Character>> lastVTs;
    private Map<Character, Map<Character, Character>> OPTable;

    Rule(String path) {
        rules = new HashMap<>();
        nonTerminal = new HashSet<>();
        terminal = new HashSet<>();
        toEmpty = new HashMap<>();
        firstVTs = new HashMap<>();
        lastVTs = new HashMap<>();
        OPTable = new HashMap<>();
        try {
            File f = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while (true) {
                line = br.readLine();
                if (line == null)
                    break;
                System.out.println(line);
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
                        if (!nonTerminal.contains(ch) && ch != '@') {
                            terminal.add(ch);
                            OPTable.put(ch, new HashMap<>());
                        }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        initFirstVT();
        initLastVT();
        generateTable();
    }

    Set<Character> getNonTerminal() {
        return nonTerminal;
    }

    Map<Character, Set<Character>> getFirstVTs() {
        return firstVTs;
    }

    Map<Character, Set<Character>> getLastVTs() {
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

    private void getLastVT(Character nonT) {
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

    private void initFirstVT() {
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

    private void initLastVT() {
        boolean changed = true;
        while (changed) {
            changed = false;
            int length = getLength(lastVTs);
            for (Character c : nonTerminal)
                getLastVT(c);
            if (length < getLength(lastVTs))
                changed = true;
        }
    }

    private Character findUsage(Character ch) {
        for (Character c : nonTerminal)
            for (String s : rules.get(c))
                if (s.indexOf(ch) >= 0)
                    return c;
        return null;
    }

    private int findUsageLength(Character ch) {
        for (Character c : nonTerminal)
            for (String s : rules.get(c))
                if (s.indexOf(ch) >= 0)
                    return s.length();
        return 0;
    }

    private Character rollback(Character ch) {
        if (ch == 'E')
            return 'E';
        for (Character c : nonTerminal)
            for (String s : rules.get(c))
                if (s.length() == 1 && s.indexOf(ch) == 0)
                    return rollback(c);
        return ch;
    }

    private void generateTable() {
        for (Character c : nonTerminal)
            for (String s : rules.get(c))
                for (Character ch : s.toCharArray())
                    if (terminal.contains(ch)) {
                        int index = s.indexOf(ch);
                        if (index + 1 < s.length()) {
                            char after = s.charAt(index + 1);
                            if (nonTerminal.contains(after)) {
                                for (Character first : firstVTs.get(after)) {
                                    Map<Character, Character> temp = OPTable.get(ch);
                                    temp.put(first, '<');
                                    OPTable.put(ch, temp);
                                }
                                if (index + 2 < s.length() && terminal.contains(s.charAt(index + 2))) {
                                    after = s.charAt(index + 2);
                                    if (terminal.contains(after)) {
                                        Map<Character, Character> temp = OPTable.get(ch);
                                        temp.put(after, '=');
                                        OPTable.put(ch, temp);
                                    }
                                }
                            } else if (terminal.contains(after)) {
                                Map<Character, Character> temp = OPTable.get(ch);
                                temp.put(after, '=');
                                OPTable.put(ch, temp);
                            }
                        }
                    } else if (nonTerminal.contains(ch)) {
                        int index = s.indexOf(ch);
                        if (index + 1 < s.length() && terminal.contains(s.charAt(index + 1)))
                            for (Character last : lastVTs.get(ch)) {
                                Map<Character, Character> temp = OPTable.get(last);
                                temp.put(s.charAt(index + 1), '>');
                                OPTable.put(last, temp);
                            }
                    }
        OPTable.put('#', new HashMap<>());
        for (Character c : terminal) {
            Map<Character, Character> m = OPTable.get(c);
            m.put('#', '>');
            OPTable.put(c, m);
            m = OPTable.get('#');
            m.put(c, '<');
            OPTable.put('#', m);
        }
        Map<Character, Character> m = OPTable.get('#');
        m.put('#', '=');
        OPTable.put('#', m);
    }

    void analyse(String input) {
        Stack<Character> state = new Stack<>();
        state.push('#');
        int top = state.size() - 1;
        for (int index = 0; index < input.length(); index++) {
            Character c = input.charAt(index);
            if (c != '#' && !terminal.contains(c)) {
                System.out.println("非标准语法");
                return;
            }
            if (OPTable.get(state.get(top)).get(c) == '<') {
                state.push(c);
                top = state.size() - 1;
            } else if (OPTable.get(state.get(top)).get(c) == '=')
                state.push(c);
            else if (OPTable.get(state.get(top)).get(c) == '>') {
                Character re = findUsage(state.get(top));
                int pos;
                while (true) {
                    if (state.size() == 1)
                        break;
                    if (nonTerminal.contains(state.peek()))

                        top = state.size() - 2;
                    else
                        top = state.size() - 1;
                    pos = top - 1;
                    if (!terminal.contains(state.get(pos)) && state.get(pos) != '#')
                        pos--;
                    Character tc = state.get(top);
                    Character pc = state.get(pos);
                    int l = findUsageLength(tc);
                    if (l == state.size() - pos - 1)
                        for (int i = 0; i < l; i++)
                            state.pop();
                    else {
                        System.out.println("ERROR");
                        return;
                    }
                    if (OPTable.get(pc).get(tc) == '<')
                        break;
                }
                state.push(re);
                index--;
                top = state.size() - 2;
            } else {
                System.out.println("ERROR");
            }
        }
        if (state.size() == 3 && rollback(state.get(1)) == 'E')
            System.out.println("accept");
        else
            System.out.println("decline");
    }
}
