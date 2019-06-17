import java.util.Scanner;
import java.util.Set;

public class Operation {
    public static void main(String[] args) {
        Rule r = new Rule("E:\\Github\\compile\\operation_precedence\\src\\rules.txt");
        Set<Character> nonTerminal = r.getNonTerminal();
        System.out.println("firstVT集");
        for (Character c : nonTerminal) {
            System.out.print(c + ":{\t");
            for (Character ch : r.getFirstVTs().get(c))
                System.out.print(ch + "\t");
            System.out.println("}");
        }
        System.out.println("lastVT集");
        for (Character c : nonTerminal) {
            System.out.print(c + ":{\t");
            for (Character ch : r.getLastVTs().get(c))
                System.out.print(ch + "\t");
            System.out.println("}");
        }
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while (input.indexOf('#') != input.length() - 1) {
            System.out.println("请正确的以#结尾");
            input = sc.nextLine();
        }
        r.analyse(input);
    }
}
