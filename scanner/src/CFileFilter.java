import javax.swing.filechooser.FileFilter;

import java.io.File;

public class CFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        String name = f.getName();
        return f.isDirectory() || name.endsWith(".c") || name.endsWith(".cpp") || name.endsWith(".h");
    }

    @Override
    public String getDescription() {
        return "*.c;*.cpp;*.h";
    }
}
