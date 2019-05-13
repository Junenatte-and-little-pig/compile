import java.util.Set;

public class Grammer {
    public static void main(String[] args) {
        Rule r=new Rule("E:\\Github\\compile\\grammer\\src\\rules.txt");
        Set<Character> nonTerminal=r.getNonTerminal();
        for(Character c:nonTerminal)
        System.out.println(c+":\t"+r.getFollow(c));
    }
}
