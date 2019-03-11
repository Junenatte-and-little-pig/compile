import java.util.HashSet;

class Scanner4C{
	private HashSet<String> keys;
	private HashSet<String> ops;
	private StringBuilder word=new StringBuilder("");
	private StringBuilder op=new StringBuilder("");
	int cnt;
	//private HashSet<String> bounds;

	Scanner4C(){
		keys = new HashSet<String>(){{
			add("auto");add("break");add("case");add("char");add("const");add("continue");add("default");add("do");add("double");
			add("else");add("enum");add("extern");add("float");add("for");add("goto");add("if");add("int");add("long");
			add("register");add("return");add("short");add("signed");add("sizeof");add("static");add("struct");add("switch");add("typedef");
			add("union");add("unsigned");add("void");add("volatile");add("while");add("inline");add("restrict");add("_Bool");add("_Complex");
			add("_Imaginary");add("_Alignas");add("_Alignof");add("_Atomic");add("_Strict_assert");add("_Noreturn");add("_Thread_local");add("_Generic");
		}};
		ops = new HashSet<String>(){{
			add("(");add(")");add("[");add("]");add("->");add(".");add("!");add("~");add("++");add("--");add("+");add("-");add("*");add("&");add("/");
			add("%");add("<<");add(">>");add("<");add("<=");add(">=");add(">");add("==");add("!=");add("^");add("|");add("&&");add("||");add("?");
			add(":");add("=");add("+=");add("-=");add("*=");add("/=");add("&=");add("^=");add("<<=");add(">>=");add(",");add("{");add("}");
			add("\"");add("\'");add(";");
		}};
	}

	void scanLine(String line, int cnt){
		if(line == null) return;
		for(int i = 0; i < line.length(); i++){
			char ch = line.charAt(i);
			if(Character.isWhitespace(ch)){
				isWord(cnt);
				isOp(cnt);
			}
			else if(Character.isLetterOrDigit(ch) || ch == '_'){
				isOp(cnt);
				word.append( ch);
			}else
			{
				isWord(cnt);
				isOp(cnt);
				op.append(ch);
			}
		}
	}

	private void isOp(int cnt){
		if(!op.toString().equals("")){
			if(ops.contains(op.toString()))
				System.out.println(op + "\t" + cnt + "\tOP");
			else
				System.out.println(op + "\t" + cnt + "\tERROR OP");
			op=new StringBuilder("");
		}
	}

	private void isWord(int cnt){
		if(!word.toString().equals("")){
			if(Character.isDigit(word.charAt(0)))
				try{
					double d = Double.parseDouble(word.toString());
					System.out.println(d+"\t"+cnt+"\tDIGIT");
				}catch(NumberFormatException e){
					System.out.println(word + "\t" + cnt + "\tERROR WORD");
				}
			else if(keys.contains(word.toString()))
				System.out.println(word + "\t" + cnt + "\tKEY");
			else
				System.out.println(word + "\t" + cnt + "\tWORD");
			word=new StringBuilder("");
		}
	}
}
