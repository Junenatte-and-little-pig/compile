import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/*
 * 如果是var 认为是定义语句
 * 	如果是main 认为是主函数
 * 		如果是( 认为是主函数的左括号
 * 		如果是) 认为是主函数的右括号
 * 		如果是{ 认为是主函数体开始
 * 		如果是} 认为是主函数体结束
 * 	如果是WORD 认为是定义
 * 		如果是( 认为是函数定义
 * 			如果是var 认为是函数参数
 * 			如果是WORD 认为是参数名
 * 			如果是, 认为是等候下一个参数
 * 			如果是) 认为是参数结束
 * 			如果是; 认为是函数定义结束
 * 			如果是{ 认为是函数体开始
 * 			如果是} 认为是函数体结束
 * 		如果是; 认为是变量定义并赋初始值
 * 		如果是赋值运算符 认为是变量初始化
 */
class Lex {
    static Deque<List<String>> refresh(Deque<List<String>> res) {
        Deque<List<String>> words = new ArrayDeque<>();
        List<String> map = res.pop();
        String line = map.get(1);
        while (!res.isEmpty()) {
            switch (map.get(2)) {
                case "EXP":
                    line=map.get(1);
                    words.offer(map);
                    while (!res.isEmpty()) {
                        map = res.pop();
                        if (map.get(1).equals(line))
                            map.set(2, "EXPLANATION");
                        else
                            break;
                        words.offer(map);
                    }
                    break;
                case "EXPStart":
                    words.offer(map);
                    while (!res.isEmpty()) {
                        map = res.pop();
                        if (!map.get(2).equals("EXPEnd"))
                            map.set(2, "EXPLANATION");
                        else
                            break;
                        words.offer(map);
                    }
                    break;
                case "SHARP":
                    line=map.get(1);
                    words.offer(map);
                    if (!res.isEmpty()) {
                        map = res.pop();
                        if (map.get(0).equals("include")&&map.get(1).equals(line)) { //判断是否为头文件
                            map.set(2,"INCLUDE");
                            words.offer(map);
                            if (!res.isEmpty()) {
                                map = res.pop();
                                if (map.get(2).equals("LT")&&map.get(1).equals(line)) {
                                    words.offer(map);
                                    while (!res.isEmpty()) {
                                        map = res.pop();
                                        if (!map.get(2).equals("GT")&&map.get(1).equals(line)) {
                                            map.set(2, "HEADFILE");
                                            words.offer(map);
                                        } else
                                            break;
                                    }
                                } else {
                                    map.set(2, "ERROR");
                                    words.offer(map);
                                    map=res.pop();
                                }
                            } else {
                                map.set(2, "ERROR");
                                words.offer(map);
                                map=res.pop();
                            }
                        } else if (map.get(0).equals("define")&&map.get(1).equals(line)) {  //判断宏定义
                            map.set(2,"DEFINE");
                            words.offer(map);
                            if (!res.isEmpty()) {
                                map = res.pop();
                                if(map.get(1).equals(line))
                                map.set(2, "DEFINE KEY");
                                words.offer(map);
                            }
                            if (!res.isEmpty()) {
                                map = res.pop();
                                if(map.get(1).equals(line))
                                map.set(2, "DEFINE VALUE");
                                words.offer(map);
                            }
                            map = res.pop();
                        } else {
                            map.set(2, "ERROR");
                            words.offer(map);
                            map=res.pop();
                        }
                    }
                    break;
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
        return words;
    }
}
