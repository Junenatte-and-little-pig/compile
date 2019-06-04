import java.util.Set;

public class Operation {
    public static void main(String[] args) {
        Rule r = new Rule("E:\\Github\\compile\\operation_precedence\\src\\rules.txt");
        Set<Character> nonTerminal = r.getNonTerminal();
        System.out.println("firstVT集");
        for (Character c : nonTerminal) {
            System.out.print(c+":{\t");
            for (Character ch : r.getFirstVTs().get(c))
                System.out.print(ch + "\t");
            System.out.println("}");
        }
        System.out.println("lastVT集");
        for (Character c : nonTerminal) {
            System.out.print(c+":{\t");
            for (Character ch : r.getlastVTs().get(c))
                System.out.print(ch + "\t");
            System.out.println("}");
        }
        //r.printTable();
        r.findUsages();
    }
}
