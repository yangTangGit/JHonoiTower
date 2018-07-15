package yangt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计指定目录下的代码数量
 * 
 * @author 杨唐
 */
public class CodeCounter {

	/** 类型 为java. */
	public static final String TYPE_JAVA = "java";
	/** 类型 为XML. */
	public static final String TYPE_XML = "xml";
	/** 类型 为HTML. */
	public static final String TYPE_HTML = "html";

	/** 通过构造方法传入的原始文件. */
	private File fileOrigional;

	/** 处理中的目录. */
	private File file;

	/** 存储扫描的、正在处理的文件列表. */
	private ArrayList<String> listFiles;

	/**
	 * 存储所有文件的信息
	 * <ul>
	 * <li>键：文件所在的绝对路径</li>
	 * <li>值：对应文件的所有代码行数</li>
	 * </ul>
	 */
	private Map<String, Integer> map_file_lineNum = new LinkedHashMap<>();
	private int null_line_num = 0;
	private int all_line_num = 0;

	/**
	 * 构造方法
	 * 
	 * @param filePath
	 *            需要扫描的根目录
	 */
	public CodeCounter(String filePath) {
		this.fileOrigional = new File(filePath);
		this.file = fileOrigional;
		listFiles = new ArrayList<>();
	}

	/**
	 * 开始统计指定目录下的代码文件信息，主要为行数
	 * 
	 * @param isOnlyPrintFileName
	 *            值为true只打印文件名，否则打印完整路径
	 */
	public void startCount(boolean isOnlyPrintFileName) {
		map_file_lineNum = new LinkedHashMap<>();
		null_line_num = 0;
		all_line_num = 0;
		if (!fileOrigional.exists()) {
			System.out.println("no such file or directory !");
			return;
		}
		ArrayList<String> listFiles = getFilesByType("java");
		for (String java : listFiles) {
			int thisNum = 0;
			try {
				FileReader fReader = new FileReader(new File(java));
				BufferedReader bReader = new BufferedReader(fReader);
				String str;
				while ((str = bReader.readLine()) != null) {
					thisNum++;
					all_line_num++;
					if (str.replaceAll(" ", "").equals("")) {
						null_line_num++;
					}
				}
				bReader.close();
				fReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			map_file_lineNum.put(java, thisNum);
		}
		printLineInfo(isOnlyPrintFileName);
	}

	/**
	 * 根据文件类型递归的扫描文件
	 * 
	 * @param type
	 *            文件类型
	 * @return 扫描的每个目录下的文件的集合
	 */
	public ArrayList<String> getFilesByType(String type) {
		if (!file.exists()) {
			return new ArrayList<String>();
		}
		List<File> files = Arrays.asList(this.file.listFiles());
		Collections.sort(files, new SortByName());
		for (File file : files) {
			if (file.isDirectory()) {
				this.file = file;
				getFilesByType(type);
			} else {
				if (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equals(type)) {
					listFiles.add(file.getPath());
				}
			}
		}
		return listFiles;
	}

	/**
	 * 向控制台输出统计信息，包括每个代码文件的行数、总的行数信息
	 * 
	 * @param isOnlyPrintFileName
	 *            是否只打印文件名，否则打印完整路径，默认值为false
	 */
	public void printLineInfo(boolean isOnlyPrintFileName) {
		String print = "details as follows :\n";

		String keys[] = new String[map_file_lineNum.keySet().size()];
		int vals[] = new int[map_file_lineNum.keySet().size()];
		int i = 0;
		if (isOnlyPrintFileName) {
			for (String key : map_file_lineNum.keySet()) {
				keys[i] = key.substring(key.lastIndexOf("\\") + 1);
				vals[i++] = map_file_lineNum.get(key);
			}
		} else {
			for (String key : map_file_lineNum.keySet()) {
				keys[i] = key;
				vals[i++] = map_file_lineNum.get(key);
			}
		}
		int maxStrLen = getMaxStringLength(keys);
		int maxNumLen = getMaxNumberLength(vals);
		int iLen = String.valueOf(i).length();

		String indent = "　　";
		i = 0;
		for (String key : map_file_lineNum.keySet()) {
			String strName;
			if (isOnlyPrintFileName) {
				strName = key.substring(key.lastIndexOf("\\") + 1);
			} else {
				strName = key;
			}
			print += indent + String.format("%-" + (iLen + 1) + "d", ++i) + String
					.format("%-" + (maxStrLen + 4) + "s%" + (maxNumLen + 1) + "d", strName, map_file_lineNum.get(key))
					+ " lines\n";
		}
		int len = String.format("%d", all_line_num).length() + 1;
		print += indent + "所有行数:  " + String.format("%-" + len + "d", all_line_num) + "lines\n";
		print += indent + "空行　数:  " + String.format("%-" + len + "d", null_line_num) + "lines\n";
		print += indent + "非空行数:  " + String.format("%-" + len + "d", all_line_num - null_line_num) + "lines";
		System.out.print(print);
	}

	/**
	 * 获取数组中最大值的对应字符串长度
	 * <p>
	 * 只针对正整数
	 * 
	 * @param ns
	 *            整数类型数组
	 * @return 数组中最大值的对应字符串长度
	 */
	public static int getMaxNumberLength(int[] ns) {
		Integer max = null;
		for (int n : ns) {
			if (max == null || n > max) {
				max = n;
			}
		}
		if (max != null) {
			return String.format("%d", max).length();
		} else {
			return 0;
		}
	}

	/**
	 * 获取数组中最长字符串的长度
	 * 
	 * @param strs
	 *            字符串数组
	 * @return 数组中最长字符串的长度
	 */
	public static int getMaxStringLength(String[] strs) {
		int maxLength = 0;
		for (String str : strs) {
			if (str != null && str.length() > maxLength) {
				maxLength = str.length();
			}
		}
		return maxLength;
	}

	/**
	 * 根据文件名称排序的类，仅适用于英文
	 * 
	 * @see Comparator
	 */
	public static class SortByName implements Comparator<File> {
		@Override
		public int compare(File f1, File f2) {
			String name1 = f1.isDirectory() ? (f1.getName() + "a")
					: (f1.isFile() ? (f1.getName() + "b") : (f1.getName() + "c"));
			String name2 = f2.isDirectory() ? (f2.getName() + "a")
					: (f2.isFile() ? (f2.getName() + "b") : (f2.getName() + "c"));
			if (name1.compareTo(name2) > 0) {
				return 1;
			} else if (name1.equals(name2)) {
				return 0;
			} else {
				return -1;
			}
		}
	}

}