package yangt.henoi_tower.widget;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * “放”柱子使用的平台UI控件类
 */
public class JHenoiPlatform extends JLabel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7216938858241092446L;

	public JHenoiPlatform() {
		initSetting();
	}

	/**
	 * 初始化设置
	 */
	private void initSetting() {
		this.setOpaque(true);
		this.setBackground(new Color(0xFFD700)); // 背景颜色
	}

}