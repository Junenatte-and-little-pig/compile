import java.util.*;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

class Scanner4C{
	private List<String> KEYS;
	private List<String> OPS;
	private List<String> TIPS;
	private Deque<List<String>> words;
	private StringBuilder word = new StringBuilder();
	private StringBuilder op = new StringBuilder();

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
				"\"", "\'", ";", "#"
		);
		TIPS = asList(
				"(", ")", "[", "]", "->", ".", "!", "~", "++", "--", "+", "-", "*", "&", "/",
				"%", "<<", ">>", "<", "<=", ">=", ">", "==", "!=", "^", "|", "&&", "||", "?",
				":", "=", "+=", "-=", "*=", "/=", "&=", "^=", "<<=", ">>=", ",", "{", "}",
				"\"", "\'", ";", "#"
		);
		words = new ArrayDeque<>();
	}

	void scanLine( String line, int cnt ){
		if( line == null ) return;
		for( int i = 0; i < line.length(); i++ ){
			char ch = line.charAt(i);
			if( Character.isLetterOrDigit(ch) || '_' == ch ){
				if( !words.isEmpty() )
					if( Character.isLetterOrDigit(line.charAt(i - 1)) || '_' == line.charAt(i - 1) )
						word = new StringBuilder(Objects.requireNonNull(words.pollLast()).get(0));
				word.append(ch);
				isWord();
			}else if( !Character.isWhitespace(ch) ){
				if( !words.isEmpty() )
					if( Pattern.matches("(\\W)+", String.valueOf(line.charAt(i - 1))) )
						op = new StringBuilder(Objects.requireNonNull(words.pollLast()).get(0));
				op.append(ch);
				isOp();
			}
		}
		printAll(cnt);
		words.clear();
	}

	private void printAll( int cnt ){
		for( List<String> al : words ){
			System.out.println(al.get(0) + "\t" + cnt + "\t" + al.get(1));
		}
	}

	private void isOp(){
		if( !op.toString().equals("") ){
			List<String> map = new ArrayList<>();
			if( 3 <= op.length() ){
				divideOp(3);
			}else if( 2 == op.length() ){
				divideOp(2);
			}else if( 1 == op.length() ){
				divideOp(1);
			}else{
				map.add(op.toString());
				map.add("WRONG OP");
				words.offer(map);
			}
			op = new StringBuilder();
		}
	}

	private void divideOp( int cnt ){
		List<String> map = new ArrayList<>();
		int index = -1;
		for( ; cnt > 0; cnt-- ){
			index=OPS.indexOf(op.substring(0,cnt));
			if( index!=-1 ){
				map.add(op.substring(0, cnt));
				map.add(TIPS.get(index));
				op = new StringBuilder(op.substring(cnt, op.length()));
				words.offer(map);
				return;
			}
		}
	}


	private void isWord(){
		List<String> map = new ArrayList<>();
		if( !word.toString().equals("") ){
			map.add(word.toString());
			if( Character.isDigit(word.charAt(0)) )
				try{
					Double.parseDouble(word.toString());
					map.add("DIGIT");
				}catch( NumberFormatException e ){
					map.add("WRONG WORD");
				}
			else if( KEYS.contains(word.toString()) )
				map.add("KEY");
			else
				map.add("WORD");
			words.offer(map);
			word = new StringBuilder();
		}
	}
}
