package yangt.henoi_tower.widget;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * 河内塔里面的圆盘
 */
public class JHenoiDisk extends JLabel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4386439989264095491L;

	public JHenoiDisk() {
		initSetting();
	}

	public JHenoiDisk(String text) {
		super(text);
		initSetting();
	}

	/**
	 * 初始化设置
	 */
	private void initSetting() {
		this.setOpaque(true);
		this.setBackground(new Color(0x808000)); // 背景颜色
	}

}