package yangt.henoi_tower;

import java.io.File;

/**
 * 文件过滤器
 */
public class FileFilter extends javax.swing.filechooser.FileFilter {

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}
		return file.getName().endsWith(".txt");
	}

	@Override
	public String getDescription() {
		return ".txt";
	}

}