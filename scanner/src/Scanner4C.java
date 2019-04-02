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
        /*
         * 如果是// 认为是单行注释
         * 如果是/* 认为是注释开始
         * 	所有 认为是注释
         * 如果是* / 认为是注释结束
         * 如果是SHARP 认为是预处理语句
         * 	如果是include 认为是包含的头文件
         * 		如果是< 认为是头文件的开始
         * 		如果是WORD 认为是头文件的名字
         * 		如果是. 认为是头文件的符号
         * 		如果是h 认为是头文件的扩展名
         * 		如果是> 认为是头文件的结束
         * 	如果是define 认为是宏定义
         * 		如果是WORD 认为是宏名
         * 		如果是WORD||DIGIT 认为是宏值
         * 如果是var 认为是定义语句
         * 	如果是main 认为是主函数
         * 		如果是( 认为是主函数的左括号
         * 		如果是) 认为是主函数的右括号
         * 		如果是{ 认为是主函数体开始
         * 		如果是} 认为是主函数体结束
         * 	如果是WORd 认为是定义
         * 		如果是( 认为是函数定义
         * 			如果是var 认为是函数参数
         * 			如果是WORd 认为是参数名
         * 			如果是, 认为是等候下一个参数
         * 			如果是) 认为是参数结束
         * 			如果是; 认为是函数定义结束
         * 			如果是{ 认为是函数体开始
         * 			如果是} 认为是函数体结束
         * 		如果是; 认为是变量定义并赋初始值
         * 		如果是赋值运算符 认为是变量初始化
         *
         */
    }

    private void refresh() {
        List<String> map = res.pop();
        int line = Integer.parseInt(map.get(1));
        while (!res.isEmpty()) {
            switch (map.get(2)) {
                case "EXP":
                    words.offer(map);
                    while (res.isEmpty() || (map = res.pop()).get(1).equals(String.valueOf(line))) {
                        map.set(2, "EXPLANATION");
                        words.offer(map);
                    }
                    break;
                case "EXPStart":
                    words.offer(map);
                    while (res.isEmpty() || !(map = res.pop()).get(2).equals("EXPEnd")) {
                        map.set(2, "EXPLANATION");
                        words.offer(map);
                    }
                    break;
                case "SHARP":
                    words.offer(map);
                    if (!res.isEmpty()) {
                        map = res.pop();
                        if (map.get(0).equals("include")) {
                            words.offer(map);
                            if (!res.isEmpty()) {
                                map = res.pop();
                                if (map.get(2).equals("LT")) {
                                    words.offer(map);
                                    while (!res.isEmpty()) {
                                        map = res.pop();
                                        if (!map.get(2).equals("GT")) {
                                            map.set(2, "HEADFILE");
                                            words.offer(map);
                                        } else
                                            break;
                                    }
                                }
                            }
                        } else if (map.get(0).equals("define")) {
                            words.offer(map);
                            if (!res.isEmpty()) {
                                map = res.pop();
                                map.set(2, "DEFINE KEY");
                                words.offer(map);
                            }
                            if (!res.isEmpty()) {
                                map = res.pop();
                                map.set(2, "DEFINE VALUE");
                                words.offer(map);
                            }
                            map = res.pop();
                        }
                    }
                case "KEY":
                    switch (map.get(0)) {
                        case "int":
                        case "char":
                        case "double":
                        case "float":
                        case "long":
                        case "short":
                        case "signed":
                        case "unsigned":
                        case "void":
                            String var = map.get(0);
                            words.offer(map);
                            map = res.pop();
                            if (map.get(0).equals("main")) {
                                map.set(2, "MAIN");
                            } else if (map.get(2).equals("WORD")) {
                                map.set(2, var.toUpperCase());
                                //加入变量集
                                //如果在函数内部
                                //	新建变量集
                                //	直到函数结束 子变量集加入
                            }
                            words.offer(map);
                            break;
                        default:
                            words.offer(map);
                            break;
                    }
                    map = res.pop();
                    break;
                default:
                    words.offer(map);
                    map = res.pop();
                    break;
            }
        }
        words.offer(map);
    }

    void printAll() {
        refresh();
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
