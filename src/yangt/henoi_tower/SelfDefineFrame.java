package yangt.henoi_tower;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * 自定义盘片数量的对话框类，当盘片数量验证通过后可以开始一局新的游戏
 */
public class SelfDefineFrame extends JDialog implements ActionListener {
	/**
	 * 用于设置当前frame的位置、计算合法的最大盘片数量
	 * 
	 * @see #init(SelfDefineFrame)
	 * @see #addUI()
	 */
	private HenoiFrame henoiFrame;

	/**
	 * 用于输入整形数量的输入框控件
	 */
	private JTextField text;

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5272952070570131385L;

	public SelfDefineFrame(HenoiFrame henoiFrame) {
		this.henoiFrame = henoiFrame;
		init(this);
		addUI();
		setVisible(true);
	}

	/**
	 * 初始化设置当前frame
	 * 
	 * @param frame
	 *            需要初始化的frame
	 */
	private void init(SelfDefineFrame frame) {
		frame.setLayout(null);
		frame.setSize(300, 200);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocation((henoiFrame.getWidth() - getWidth()) / 2, (henoiFrame.getHeight() - getHeight()) / 2);
		frame.setLocationRelativeTo(henoiFrame);
		frame.setIconImage(new ImageIcon("assets/icon/default_icon_96.png").getImage());
		frame.setTitle("自定义盘片数量");
		frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		frame.getContentPane().setBackground(Color.white);
	}

	/**
	 * 添加UI界面
	 */
	private void addUI() {
		henoiFrame.calculateMaxDiskNum();

		/* label. */
		JLabel label = new JLabel("输入盘片数量");
		label.setBounds(40, 40, 90, 25);
		this.add(label);
		/* 输入框. */
		text = new JTextField();
		text.setBounds(label.getX() + label.getWidth(), 40, 100, 25);
		text.addKeyListener(new InputKeyListener(text));
		this.add(text);

		/* 盘片范围值. */
		JLabel range = new JLabel("(2~" + henoiFrame.getDiskMaxNum() + ")");
		range.setBounds(text.getX(), text.getY() + text.getHeight() + 2, 100, 20);
		range.setFont(new Font("Courier New", Font.PLAIN, 12));
		this.add(range);

		int btnWidth = 70;
		int btnHeight = 30;
		int y = getHeight() - 50 - btnHeight;
		/* 开始按钮. */
		JButton btnOk = new JButton("开始");
		btnOk.setBounds(getWidth() / 8, y, btnWidth, btnHeight);
		btnOk.addActionListener(this);
		this.add(btnOk);
		/* 取消按钮. */
		JButton btnCancel = new JButton("取消");
		btnCancel.setBounds(getWidth() - 16 - btnWidth - getWidth() / 8, y, btnWidth, btnHeight);
		btnCancel.addActionListener(this);
		this.add(btnCancel);
	}

	/**
	 * 单击开始、取消按钮的监听，并做相应的处理
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("开始")) {
			try {
				int num = Integer.valueOf(text.getText());
				henoiFrame.calculateMaxDiskNum();
				if (num >= 2 && num <= henoiFrame.getDiskMaxNum()) {
					dispose();
					henoiFrame.startNewGame(num);
				} else {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					text.requestFocus();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else if (event.getActionCommand().equals("取消")) {
			dispose();
		}
	}

	/**
	 * 数量 输入框的输入控制类
	 * <p>
	 * 只允许输入正整数，且不能以0开头
	 */
	public static class InputKeyListener extends KeyAdapter {
		/**
		 * 输入框控件
		 */
		private JTextField input;

		public InputKeyListener(JTextField input) {
			this.input = input;
		}

		@Override
		public void keyTyped(KeyEvent e) {
			super.keyTyped(e);
			int keyChar = e.getKeyChar();
			if (!(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9)) {
				e.consume(); // 不能输入非数字字符
			}
			if (keyChar == KeyEvent.VK_0 && input.getText().equals("")) {
				e.consume(); // 第一个字符不能为0
			}
		}
	}

}