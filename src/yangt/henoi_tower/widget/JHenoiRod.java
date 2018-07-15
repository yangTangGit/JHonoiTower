package yangt.henoi_tower.widget;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * 河内塔里面的柱子（UI控件类）
 */
public class JHenoiRod extends JLabel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1298229000758372156L;

	public JHenoiRod() {
		initSetting();
	}

	/**
	 * 构造方法
	 * 
	 * @param text
	 *            控件内显示的文本
	 * @see JLabel#JLabel(String)
	 */
	public JHenoiRod(String text) {
		super(text);
		initSetting();
	}

	/**
	 * 初始化设置
	 */
	private void initSetting() {
		this.setOpaque(true);
		this.setBackground(new Color(0xFFD700));
	}

}