import java.util.*;

public class Vars {
    private List<List<String>> vars;
    private List<Vars> nextLevel;
    Vars(){
        vars=new ArrayList<List<String>>();
        nextLevel=new ArrayList<>();
    }
    public void addVar(List<String> var){
        vars.add(var);
    }
    public void addNextLevel(Vars nl){
        nextLevel.add(nl);
    }
    public List<List<String>> getVars(){
        return vars;
    }
    public List<Vars> getNextLevel(){
        return nextLevel;
    }
}
