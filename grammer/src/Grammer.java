import java.util.Set;

public class Grammer {
    public static void main(String[] args) {
        Rule r = new Rule("E:\\Github\\compile\\grammer\\src\\rules.txt");
        Set<Character> nonTerminal = r.getNonTerminal();
        for (Character c : nonTerminal) {
            System.out.print(c+":{\t");
            for (Character ch : r.getFirsts().get(c))
                System.out.print(ch + "\t");
            System.out.println("}");
        }
    }
}
