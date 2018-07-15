package yangt.henoi_tower;

import java.lang.ClassNotFoundException;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 河内塔游戏执行入口
 */
public class Entry {

	/**
	 * 主方法
	 * 
	 * @param args
	 *            控制台参数
	 */
	public static void main(String[] args) {
		try {
			// 设置皮肤
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new HenoiFrame();
		// new HenoiTower().move(3, "A", "B", "C");
	}

}