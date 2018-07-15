package yangt.henoi_tower;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

/**
 * MenuBar的监听
 * 
 * @see HenoiFrame
 */
public class MenuItemActionListener implements java.awt.event.ActionListener {

	/**
	 * 用于在对象内重新开始游戏
	 */
	private HenoiFrame owner;

	/**
	 * 设定的响应类型
	 */
	private MenuItemActionListener.ActionType currentType;

	public MenuItemActionListener(HenoiFrame owner, MenuItemActionListener.ActionType actionType) {
		this.owner = owner;
		this.currentType = actionType;
	}

	/**
	 * action事件类型标志，用于分类处理不同的菜单项
	 */
	public static enum ActionType {
		/** 游戏. */
		GAME,

		/** 帮助. */
		HELP,

		/** 未知. */
		NONE
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (currentType.equals(ActionType.GAME)) {
			performedGame(e);
		} else if (currentType.equals(ActionType.HELP)) {
			performedHelp(e);
		}
	}

	/**
	 * “游戏”菜单下的监听
	 * 
	 * @param e
	 *            响应的事件
	 */
	private void performedGame(ActionEvent e) {
		if (e.getActionCommand().equals("打开")) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileFilter());
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				System.out.println(file);
			}
		} else if (e.getActionCommand().equals("初级")) {
			owner.startNewGame(3);
		} else if (e.getActionCommand().equals("中级")) {
			owner.startNewGame(5);
		} else if (e.getActionCommand().equals("高级")) {
			owner.startNewGame(6);
		} else if (e.getActionCommand().equals("自定义")) {
			new SelfDefineFrame(owner);
		}
	}

	/**
	 * “帮助”菜单下的监听
	 * 
	 * @param e
	 *            响应的事件
	 */
	private void performedHelp(ActionEvent e) {
		if (e.getActionCommand().equals("关于...")) {
			new HelpFrame();
		}
	}

}