package yangt.henoi_tower;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 菜单栏-帮助-关于：窗口控件类
 */
public class HelpFrame extends JDialog {

	/**
	 * 用于间隔设置颜色，每设置一次就取一次反
	 */
	private boolean odd = false;

	/**
	 * serialVersionUID，标识版本信息
	 */
	private static final long serialVersionUID = -1661835134779297621L;

	public HelpFrame() {
		this.setLayout(null);
		this.setSize(400, 300);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) (dimension.getWidth() - getWidth()) / 2,
				(int) (dimension.getHeight() - getHeight()) / 2);
		this.setIconImage(new ImageIcon("assets/icon/default_icon_96.png").getImage());
		this.setTitle("帮助 - 河内塔");
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.getContentPane().setBackground(Color.white);
		ArrayList<String> lines = new ArrayList<>();
		lines.add("Developer：杨唐（Yang Tang）");
		lines.add("Date：2017-12-19 ~ 2017-12-28");
		for (int i = 0; i < lines.size(); i++) {
			addJLabel(lines.get(i), 10, 10 + 30 * i);
		}
		this.setVisible(true);
	}

	/**
	 * 添加JLabel
	 * 
	 * @param text
	 *            JLabel显示的文本
	 * @param x
	 *            x坐标
	 * @param y
	 *            y坐标
	 */
	public void addJLabel(String text, int x, int y) {
		JLabel label = new JLabel(text);
		label.setBounds(x, y, getWidth() - 8, 30);
		label.setFont(new Font("宋体, Courier New", Font.PLAIN, 15));
		label.setOpaque(true);
		if (odd) {
			label.setBackground(new Color(0xf5f5f5));
		} else {
			label.setBackground(new Color(0xefefef));
		}
		odd = !odd;
		this.getContentPane().add(label);
	}

}