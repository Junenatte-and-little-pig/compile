import java.util.*;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

class Scanner4C {
    private List<String> KEYS;
    private List<String> OPS;
    private List<String> TIPS;
    private Deque<List<String>> words;
    private Deque<List<String>> res;
    private StringBuilder word = new StringBuilder();
    private StringBuilder op = new StringBuilder();

    Scanner4C() {
        KEYS = asList("auto", "break", "case", "char", "const", "continue", "default", "do", "double",
                "else", "enum", "extern", "float", "for", "goto", "if", "int", "long",
                "register", "return", "short", "signed", "sizeof", "static", "struct", "switch", "typedef",
                "union", "unsigned", "void", "volatile", "while", "inline", "restrict", "_Bool", "_Complex",
                "_Imaginary", "_Alignas", "_Alignof", "_Atomic", "_Strict_assert", "_Noreturn", "_Thread_local", "_Generic")
        ;
        OPS = asList(
                "(", ")", "[", "]", "->", ".", "!", "~", "++", "--", "+", "-", "*", "&", "/",
                "%", "<<", ">>", "<", "<=", ">=", ">", "==", "!=", "^", "|", "&&", "||", "?",
                ":", "=", "+=", "-=", "*=", "/=", "&=", "^=", "<<=", ">>=", ",", "{", "}",
                "\"", "\'", ";", "#", "//", "/*", "*/"
        );
        TIPS = asList(
                "LSB", "RSB", "LMB", "RMB", "To", "De", "NonOp", "BitnonOp", "DoublePlus", "DoubleMinus", "PlusOp", "MinusOp", "MultiOp", "BitandOp", "DivOp",
                "MOD", "LS", "RS", "LT", "LE", "GE", "GT", "EQUAL", "UNEQUAL", "XOR", "BitorOp", "AndOp", "OrOp", "QUES",
                "COLON", "ASSIGN", "PlusAss", "MinusAss", "MultiAss", "DivAss", "AndAss", "XorAss", "LSE", "RSE", "COMMA", "LBB", "RBB",
                "DoubleQuote", "SingleQuote", "SEMI", "SHARP", "EXP", "EXPStart", "EXPEnd"
        );
        words = new ArrayDeque<>();
        res = new ArrayDeque<>();
    }

    void scanLine(String line, int cnt) {
        if (line == null) return;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (Character.isLetterOrDigit(ch) || '_' == ch) {
                if (!words.isEmpty())
                    if (Character.isLetterOrDigit(line.charAt(i - 1)) || '_' == line.charAt(i - 1))
                        word = new StringBuilder(Objects.requireNonNull(words.pollLast()).get(0));
                word.append(ch);
                isWord(cnt);
            } else if (!Character.isWhitespace(ch)) {
                if (!words.isEmpty())
                    if (Pattern.matches("(\\W)+", String.valueOf(line.charAt(i - 1))))
                        op = new StringBuilder(Objects.requireNonNull(words.pollLast()).get(0));
                op.append(ch);
                isOp(cnt);
            }
        }
        while (!words.isEmpty())
            res.offer(words.pop());
    }

    void printAll() {
        words = Lex.refresh(res);
        for (List<String> al : words) {
            System.out.println(al.get(0) + "\t" + al.get(1) + "\t" + al.get(2));
        }
    }

    private void isOp(int cnt) {
        if (!op.toString().equals("")) {
            List<String> map = new ArrayList<>();
            if (3 <= op.length()) {
                divideOp(3, cnt);
            } else if (2 == op.length()) {
                divideOp(2, cnt);
            } else if (1 == op.length()) {
                divideOp(1, cnt);
            } else {
                map.add(op.toString());
                map.add(String.valueOf(cnt));
                map.add("WRONG OP");
                words.offer(map);
            }
            op = new StringBuilder();
        }
    }

    private void divideOp(int pos, int cnt) {
        List<String> map = new ArrayList<>();
        int index = -1;
        for (; pos > 0; pos--) {
            index = OPS.indexOf(op.substring(0, pos));
            if (index != -1) {
                map.add(op.substring(0, pos));
                map.add(String.valueOf(cnt));
                map.add(TIPS.get(index));
                op = new StringBuilder(op.substring(pos, op.length()));
                words.offer(map);
                return;
            }
        }
    }

    private void isWord(int cnt) {
        List<String> map = new ArrayList<>();
        if (!word.toString().equals("")) {
            map.add(word.toString());
            map.add(String.valueOf(cnt));
            if (Character.isDigit(word.charAt(0)))
                try {
                    Double.parseDouble(word.toString());
                    map.add("DIGIT");
                } catch (NumberFormatException e) {
                    map.add("WRONG WORD");
                }
            else if (KEYS.contains(word.toString()))
                map.add("KEY");
            else
                map.add("WORD");
            words.offer(map);
            word = new StringBuilder();
        }
    }
}
