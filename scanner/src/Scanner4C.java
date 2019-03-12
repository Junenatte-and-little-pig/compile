import java.util.*;

import static java.util.Arrays.asList;

class Scanner4C{
	private List<String> KEYS;
	private List<String> OPS;
	private Deque<List<String>> maps;
	private StringBuilder word = new StringBuilder("");
	private StringBuilder op = new StringBuilder("");
	int cnt;

	Scanner4C(){
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
				"\"", "\'", ";"
		);
		maps = new ArrayDeque<>();
	}

	void scanLine(String line, int cnt){
		if(line == null) return;
		for(int i = 0; i < line.length(); i++){
			char ch = line.charAt(i);
			if(Character.isLetterOrDigit(ch) || '_' == ch){
				if(! maps.isEmpty())
					if(0 == i || (Character.isLetterOrDigit(line.charAt(i - 1)) || '_' == line.charAt(i - 1)))
						word = new StringBuilder(maps.pollLast().get(0));
				word.append(ch);
				isWord();
			}else if(! Character.isWhitespace(ch)){
				op.append(ch);
				isOp();
			}
		}
		printAll(cnt);
		maps.clear();
	}

	private void printAll(int cnt){
		for(List<String> al : maps){
			System.out.println(al.get(0) + "\t" + cnt + "\t" + al.get(1));
		}
	}

	private void isOp(){
		List<String> map = new ArrayList<>();
		if(! op.toString().equals("")){
			map.add(op.toString());
			if(OPS.contains(op.toString()))
				map.add("OP");
			else
				map.add("OP");
			maps.offer(map);
			op = new StringBuilder();
		}
	}

	private void isWord(){
		List<String> map = new ArrayList<>();
		if(! word.toString().equals("")){
			map.add(word.toString());
			if(Character.isDigit(word.charAt(0)))
				try{
					Double.parseDouble(word.toString());
					map.add("DIGIT");
				}catch(NumberFormatException e){
					map.add("WRONG WORD");
				}
			else if(KEYS.contains(word.toString()))
				map.add("KEY");
			else
				map.add("WORD");
			maps.offer(map);
			word = new StringBuilder();
		}
	}
}
