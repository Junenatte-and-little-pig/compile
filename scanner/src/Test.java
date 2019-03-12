import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Test{
	public static void main(String[] args){
		Scanner4C sc4c = new Scanner4C();
		int cnt = 0;
		/*Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine())
			sc4c.scanLine(sc.nextLine(), cnt++);*/
		String path="E:\\Github\\compile\\scanner\\test.c";
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			String line;
			while(true){
				line=br.readLine();
				if(line==null)
					break;
				sc4c.scanLine(line,cnt++);
			}
			br.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.exit(0);
	}
}
