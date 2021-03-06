import javax.swing.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Test {
    public static void main(String[] args) {
        Scanner4C sc4c = new Scanner4C();
        int cnt = 0;
		/*Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine())
			sc4c.scanLine(sc.nextLine(), cnt++);*/
        //String path="E:\\Github\\compile\\scanner\\test.c";
        JFileChooser jfc = new JFileChooser("..");
        //jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setFileFilter(new CFileFilter());
        jfc.showDialog(null, "选择");
        try {
            File f = jfc.getSelectedFile();
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while (true) {
                line = br.readLine();
                if (line == null)
                    break;
                sc4c.scanLine(line, cnt++);
            }
            sc4c.printAll();
            br.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "文件错误！", "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.exit(0);
    }
}
