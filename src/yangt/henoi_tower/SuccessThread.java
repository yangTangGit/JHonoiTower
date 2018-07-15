package yangt.henoi_tower;

import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * 当河内塔游戏的目标达到时，运行的线程类
 */
public class SuccessThread extends Thread {

	/**
	 * 打开此线程的frame类
	 */
	private JFrame owner;

	public SuccessThread(JFrame owner) {
		this.owner = owner;
	}

	@Override
	public void run() {
		super.run();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/* 显示对话框. */
		JDialog dialog = new JDialog(owner, "结束", true);
		dialog.setLayout(null);
		dialog.setSize(300, 180);
		dialog.setLocation(owner.getWidth() / 2 - dialog.getWidth() / 2,
				owner.getHeight() / 2 - dialog.getHeight() / 2);
		dialog.setLocationRelativeTo(owner);
		JLabel label = new JLabel("--成功--");
		label.setFont(new Font("宋体", Font.PLAIN, 18));
		label.setSize(100, 30);
		label.setLocation(dialog.getWidth() / 2 - 8 - label.getWidth() / 2, dialog.getHeight() / 10);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		dialog.getContentPane().add(label);
		dialog.setVisible(true);
	}

}