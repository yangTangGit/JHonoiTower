package yangt.henoi_tower;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import yangt.henoi_tower.MenuItemActionListener.ActionType;
import yangt.henoi_tower.sets.Rod;
import yangt.henoi_tower.widget.JHenoiDisk;
import yangt.henoi_tower.widget.JHenoiPlatform;
import yangt.henoi_tower.widget.JHenoiRod;

/**
 * 河内塔游戏窗口控件类
 */
public class HenoiFrame extends JFrame {

	/**
	 * 绘图区宽度
	 */
	private int paintWidth;
	/**
	 * 绘图区高度
	 */
	private int paintHeight;

	/* 辅助测量的变量. */

	/** 下标0~3的值依次对应：此JFrame的上内边距、右内边距、下内边距、左内边距. */
	private int[] framePadding = new int[4];

	/** 相邻盘片之间的间隔距离. */
	private int diskHorizontalInterval;

	/** 顶部留出来的高度，主要用来自动移动盘片时顶部所需的抛物线高度. */
	private int topUsedHeight;

	/** 柱子底端和文本顶端之间的间隔距离. */
	private int rodBottomTextInterval;

	/* 标准衡量值. */

	/** 允许设置的盘片最大半径. */
	private int diskMaxRadius;

	/** 盘片的厚度(高). */
	private int diskHigh;

	/** 盘片的最大厚度(高). */
	private int diskMaxHigh = 45;

	/** 盘片的最小厚度(高). */
	private int diskMinHigh = 3;

	/** 相邻盘片的竖直间隔. */
	public static int diskVerticalInterval = 1;

	/**
	 * 柱子宽度的一半
	 * <p>
	 * 柱子的宽度为 <code>rodHalfWidth</code> 的两倍
	 */
	private int rodHalfWidth;

	/** 柱子的中心x坐标. */
	private int[] rodCenterX = new int[3];

	/** 柱子顶端的y坐标值. */
	private int rodTopY;

	/** 柱子的高度. */
	private int rodHeight;

	/** 柱子底端的y坐标值. */
	private int rodBottomY;

	/** 可以看做是放柱子的桌面的厚度. */
	private int rodPlatformHeight;

	/** 底部用来显示文本（显示游戏进行中的状态信息）的高度. */
	private int bottomTextHeight;

	/* 河内塔元素. */

	/** 第一根柱子（A）的信息. */
	private Rod rodA;

	/** 第二根柱子（B）的信息. */
	private Rod rodB;

	/** 第三根柱子（C）的信息. */
	private Rod rodC;

	/**
	 * 盘片移动的步数.
	 * 
	 * @see #stepLabel
	 */
	private int steps = 0;

	/**
	 * 用来显示移动次数.
	 * 
	 * @see #steps
	 */
	private JLabel stepLabel;

	/* 时间、步数. */

	/** 开始时间(毫秒). */
	private long startTime;

	/** 定时器，显示使用时间. */
	private Timer timerShow;

	/** 显示使用时间的任务. */
	private TimePassingTask timePassingTask;

	/** 最大的盘片数量. */
	private int diskMaxNum;

	/** serialVersionUID. */
	private static final long serialVersionUID = 4809196733411051239L;

	public HenoiFrame() {
		initialBasic();
		initialMenuBar(this);
		calculate();
		startNewGame(3);
		this.setVisible(true);
	}

	/**
	 * 初始化基本属性
	 */
	private void initialBasic() {
		this.setLayout(null); // 设置为绝对定位
		this.setSize(640, 480);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) (dimension.getWidth() - getWidth()) / 2,
				(int) (dimension.getHeight() - getHeight()) / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(new ImageIcon("assets/icon/default_icon_96.png").getImage());
		this.setTitle("河内塔 - 益智游戏");
		this.setResizable(false);
	}

	/**
	 * 初始化菜单栏
	 * 
	 * @param henoiFrame
	 *            默认的frame
	 */
	private void initialMenuBar(HenoiFrame henoiFrame) {
		JMenuBar bar = new JMenuBar();
		// 游戏
		JMenu game = new JMenu("游戏(G)");
		game.setMnemonic((int) 'G');
		game.setHorizontalAlignment(SwingConstants.LEFT);
		JMenuItem[] gameItems = { new JMenuItem("初级"), new JMenuItem("中级"), new JMenuItem("高级"), null,
				new JMenuItem("自定义") };
		MenuItemActionListener gameListener = new MenuItemActionListener(this, ActionType.GAME);
		setListeners(gameItems, gameListener);
		addMenuItems(game, gameItems);
		bar.add(game);
		// 帮助
		JMenu help = new JMenu("帮助(H)");
		help.setMnemonic((int) 'H');
		JMenuItem[] helpItems = { null, new JMenuItem("关于...") };
		MenuItemActionListener helpListener = new MenuItemActionListener(this, ActionType.HELP);
		setListeners(helpItems, helpListener);
		addMenuItems(help, helpItems);
		bar.add(help);
		// 添加到菜单
		henoiFrame.setJMenuBar(bar);
	}

	/**
	 * 计算绘图大小，可以用计算出来的大小绘制各种图形
	 */
	private void calculate() {
		// 辅助值
		setFramePaddingTop(5); // 上内边距
		setFramePaddingRight(20); // 右内边距
		setFramePaddingBottom(getFramePadding()[0]); // 下内边距
		setFramePaddingLeft(getFramePadding()[1]); // 左内边距
		setDiskHorizontalInterval(15);
		setTopUsedHeight(60);
		setRodBottomTextInterval(10);
		// 标准值
		paintWidth = getWidth() - 8;
		paintHeight = getHeight() - 50;
		diskMaxRadius = (getPaintWidth() - getFramePadding()[3] - getFramePadding()[1]
				- getDiskHorizontalInterval() * 4) / 6;
		rodHalfWidth = 4;
		rodCenterX[0] = getFramePadding()[3] + getDiskHorizontalInterval() + getDiskMaxRadius();
		rodCenterX[2] = getPaintWidth() - getFramePadding()[1] - getDiskHorizontalInterval() - getDiskMaxRadius();
		rodCenterX[1] = (getRodCenterX()[0] + getRodCenterX()[2]) / 2;
		rodPlatformHeight = 8;
		bottomTextHeight = 70;
		rodTopY = getFramePadding()[0] + getTopUsedHeight();
		rodBottomY = getPaintHeight() - getFramePadding()[2] - getBottomTextHeight() - getRodBottomTextInterval()
				- getRodPlatformHeight();
		rodHeight = getRodBottomY() - getRodTopY();
	}

	/**
	 * 计算合法的、可以设置的最大盘片数量
	 */
	public void calculateMaxDiskNum() {
		int maxVertical = (getRodBottomY() - getRodTopY()) / (getDiskMinHigh() + getDiskVerticalInterval());
		int maxHorizontal = getDiskMaxRadius() / 3 - 3;
		diskMaxNum = maxVertical < maxHorizontal ? maxVertical : maxHorizontal;
	}

	/**
	 * 开始新的河内塔游戏，包含初始化图形界面、游戏状态数据
	 * 
	 * @param diskNum
	 *            盘片的总数量
	 */
	public void startNewGame(int diskNum) {
		Container container = this.getContentPane();
		container.removeAll();
		container.setBackground(Color.white);
		this.repaint();
		int avgHigh = (getRodBottomY() - getRodTopY()) / (diskNum + 2);
		diskHigh = (avgHigh <= getDiskMaxHigh() ? avgHigh : getDiskMaxHigh());
		diskHigh = (getDiskHigh() >= getDiskMinHigh() ? getDiskHigh() : getDiskMinHigh());
		steps = 0;
		timerShow = null;

		/* 添加柱子. */
		JHenoiRod jRodA = new JHenoiRod();
		jRodA.setBounds(getRodCenterX()[0] - getRodHalfWidth(), getRodTopY(), getRodHalfWidth() * 2,
				getRodBottomY() - getRodTopY());
		container.add(jRodA);
		JHenoiRod jRodB = new JHenoiRod();
		jRodB.setBounds(getRodCenterX()[1] - getRodHalfWidth(), getRodTopY(), getRodHalfWidth() * 2,
				getRodBottomY() - getRodTopY());
		container.add(jRodB);
		JHenoiRod jRodC = new JHenoiRod();
		jRodC.setBounds(getRodCenterX()[2] - getRodHalfWidth(), getRodTopY(), getRodHalfWidth() * 2,
				getRodBottomY() - getRodTopY());
		container.add(jRodC);

		/* 添加放柱子的平台. */
		JHenoiPlatform platform = new JHenoiPlatform();
		platform.setBounds(getFramePadding()[3], getRodBottomY(),
				getPaintWidth() - getFramePadding()[3] - getFramePadding()[1], getRodPlatformHeight());
		container.add(platform);

		/* 根据指定数量添加盘片. */
		int diskHorizontalStep = getDiskMaxRadius() / (diskNum + 1);// 相邻两个盘片的半径差
		int startDiskX = getRodCenterX()[0] - getDiskMaxRadius() + diskHorizontalStep; // 最大的一个盘片的x坐标
		rodA = new Rod(Rod.ROD_A_NAME, getRodCenterX()[0], getRodBottomY(), getDiskHigh(), getDiskVerticalInterval());
		rodB = new Rod(Rod.ROD_B_NAME, getRodCenterX()[1], getRodBottomY(), getDiskHigh(), getDiskVerticalInterval());
		rodC = new Rod(Rod.ROD_C_NAME, getRodCenterX()[2], getRodBottomY(), getDiskHigh(), getDiskVerticalInterval());
		for (int index = 0; index < diskNum; index++) {
			JHenoiDisk disk = new JHenoiDisk("");
			disk.setBounds(startDiskX + diskHorizontalStep * index, rodA.nextDiskY(),
					(getDiskMaxRadius() - diskHorizontalStep * (index + 1)) * 2, getDiskHigh());
			DiskDragListener dragListener = new DiskDragListener(HenoiFrame.this, disk, diskNum);
			dragListener.setParams(rodA, rodB, rodC, getRodCenterX());
			disk.addMouseMotionListener(dragListener);
			disk.addMouseListener(dragListener);
			disk.setVisible(true);
			rodA.getDiskStack().push(disk); // 默认将所有盘片添加到第一根柱子上
			container.add(disk);
		}

		/* 显示目标. */
		JLabel ruleLabel = new JLabel("目标：将第一根柱子上的盘片全部移动到第三根柱子");
		int x = this.getFramePadding()[3] + this.getDiskHorizontalInterval();
		int startY = this.getRodBottomY() + this.getRodPlatformHeight() + this.getRodBottomTextInterval();
		int labelWidth = this.getPaintWidth() - this.getFramePadding()[3] - this.getFramePadding()[1]
				- this.getDiskHorizontalInterval() * 2;
		ruleLabel.setBounds(x, startY, labelWidth, 22);
		container.add(ruleLabel);

		/* 显示移动次数. */
		stepLabel = new JLabel("移动次数：0");
		stepLabel.setBounds(x, startY + ruleLabel.getHeight(), labelWidth, 20);
		container.add(stepLabel);

		/* 显示使用时间. */
		JLabel timeLabel = new JLabel("使用时间：0");
		timeLabel.setBounds(x, startY + ruleLabel.getHeight() + stepLabel.getHeight(), labelWidth, 20);
		container.add(timeLabel);
		timePassingTask = new TimePassingTask(this, timeLabel);
	}

	/**
	 * 统一向 <code>JMenu</code> 中添加 <code>MenuItem</code>.
	 * 
	 * @param jMenu
	 *            菜单
	 * @param items
	 *            要添加的子项
	 */
	public static void addMenuItems(JMenu jMenu, JMenuItem[] items) {
		for (JMenuItem item : items) {
			if (item != null) {
				jMenu.add(item);
			} else {
				jMenu.addSeparator(); // 当MenuItem为空时，添加分割线
			}
		}
	}

	/**
	 * 为指定的对象集合设置指定的监听事件
	 * 
	 * @param menuItems
	 *            要设置监听的对象集合
	 * @param listener
	 *            监听事件类
	 */
	public static void setListeners(JMenuItem[] menuItems, ActionListener listener) {
		for (JMenuItem item : menuItems) {
			if (item == null) {
				continue;
			}
			item.addActionListener(listener);
		}
	}

	/* set、get方法. */

	/**
	 * 获取绘图区宽度
	 * 
	 * @return 绘图区宽度
	 */
	public int getPaintWidth() {
		return paintWidth;
	}

	/**
	 * 获取绘图区高度
	 * 
	 * @return 绘图区高度
	 */
	public int getPaintHeight() {
		return paintHeight;
	}

	/**
	 * 设置此JFrame的上内边距
	 * 
	 * @param framePaddingTop
	 *            上内边距
	 */
	public void setFramePaddingTop(int framePaddingTop) {
		this.framePadding[0] = framePaddingTop;
	}

	/**
	 * 设置此JFrame的右内边距
	 * 
	 * @param framePaddingRight
	 *            右内边距
	 */
	public void setFramePaddingRight(int framePaddingRight) {
		this.framePadding[1] = framePaddingRight;
	}

	/**
	 * 设置此JFrame的下内边距
	 * 
	 * @param framePaddingBottom
	 *            下内边距
	 */
	public void setFramePaddingBottom(int framePaddingBottom) {
		this.framePadding[2] = framePaddingBottom;
	}

	/**
	 * 设置此JFrame的左内边距
	 * 
	 * @param framePaddingLeft
	 *            左内边距
	 */
	public void setFramePaddingLeft(int framePaddingLeft) {
		this.framePadding[3] = framePaddingLeft;
	}

	/**
	 * 获取此JFrame的内边距数组，下标0~3的值依次对应上内边距、右内边距、下内边距、左内边距
	 * 
	 * @return 此JFrame的内边距数组
	 */
	public int[] getFramePadding() {
		return framePadding;
	}

	/**
	 * 相邻盘片之间的间隔距离，每个盘片的左边和最后一个盘片的右边，共4个这样的距离
	 * 
	 * @param diskHorizontalInterval
	 *            相邻盘片之间的间隔距离
	 */
	public void setDiskHorizontalInterval(int diskHorizontalInterval) {
		this.diskHorizontalInterval = diskHorizontalInterval;
	}

	/**
	 * 获取相邻盘片之间的间隔距离
	 * 
	 * @return 相邻盘片之间的间隔距离
	 */
	public int getDiskHorizontalInterval() {
		return diskHorizontalInterval;
	}

	/**
	 * 设置顶部留出来的高度，可提高扩充性（用于河内塔演示）
	 * 
	 * @param topUsedHeight
	 *            顶部留出来的高度
	 */
	public void setTopUsedHeight(int topUsedHeight) {
		this.topUsedHeight = topUsedHeight;
	}

	/**
	 * 获取顶部留出来的高度
	 * 
	 * @return 顶部留出来的高度
	 */
	public int getTopUsedHeight() {
		return topUsedHeight;
	}

	/**
	 * 设置柱子底端和文本顶端之间的间隔距离
	 * 
	 * @param rodBottomTextInterval
	 *            柱子底端和文本顶端之间的间隔距离
	 */
	public void setRodBottomTextInterval(int rodBottomTextInterval) {
		this.rodBottomTextInterval = rodBottomTextInterval;
	}

	/**
	 * 获取
	 * 
	 * @return 柱子底端和文本顶端之间的间隔距离
	 */
	public int getRodBottomTextInterval() {
		return rodBottomTextInterval;
	}

	/**
	 * 获取允许设置的盘片最大半径
	 * 
	 * @return 允许设置的盘片最大半径
	 */
	public int getDiskMaxRadius() {
		return diskMaxRadius;
	}

	/**
	 * 获取盘片的厚度(高)
	 * 
	 * @return 盘片的厚度(高)
	 */
	public int getDiskHigh() {
		return diskHigh;
	}

	/**
	 * 获取盘片的最大厚度(高)
	 * 
	 * @return 盘片的最大厚度(高)
	 */
	public int getDiskMaxHigh() {
		return diskMaxHigh;
	}

	/**
	 * 获取盘片的最小厚度(高)
	 * 
	 * @return 盘片的最小厚度(高)
	 */
	public int getDiskMinHigh() {
		return diskMinHigh;
	}

	/**
	 * 获取相邻盘片的竖直间隔
	 * 
	 * @return 相邻盘片的竖直间隔
	 */
	public static int getDiskVerticalInterval() {
		return diskVerticalInterval;
	}

	/**
	 * 获取柱子宽度的一半
	 * 
	 * @return 柱子宽度的一半
	 */
	public int getRodHalfWidth() {
		return rodHalfWidth;
	}

	/**
	 * 获取柱子C的中心x坐标，0~3分别对应柱子A、柱子B、柱子C
	 * 
	 * @return 柱子C的中心x坐标
	 */
	public int[] getRodCenterX() {
		return rodCenterX;
	}

	/**
	 * 获取柱子顶端的y坐标值
	 * 
	 * @return 柱子顶端的y坐标值
	 */
	public int getRodTopY() {
		return rodTopY;
	}

	/**
	 * 获取柱子的高度
	 * 
	 * @return 柱子的高度
	 */
	public int getRodHeight() {
		return rodHeight;
	}

	/**
	 * 获取柱子底端的y坐标值
	 * 
	 * @return 柱子底端的y坐标值
	 */
	public int getRodBottomY() {
		return rodBottomY;
	}

	/**
	 * 获取放柱子的桌面的厚度
	 * 
	 * @return 放柱子的桌面的厚度
	 */
	public int getRodPlatformHeight() {
		return rodPlatformHeight;
	}

	/**
	 * 获取底部用来显示文本（显示游戏进行中的状态信息）的高度
	 * 
	 * @return 底部用来显示文本（显示游戏进行中的状态信息）的高度
	 */
	public int getBottomTextHeight() {
		return bottomTextHeight;
	}

	/**
	 * 是否将步数加1，并将部数更新到界面
	 * 
	 * @param isAuto
	 *            是否将步数加一.
	 */
	public void setSteps(boolean isAuto) {
		if (isAuto) {
			this.steps++;
		}
		if (stepLabel != null) {
			stepLabel.setText("移动次数：" + steps);
		}
	}

	/**
	 * 获取盘片移动的步数
	 * 
	 * @return 盘片移动的步数
	 */
	public int getSteps() {
		return steps;
	}

	/**
	 * 游戏开始时，设置 开始时间(毫秒)
	 * 
	 * @param startTime
	 *            开始时间(毫秒)
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * 获取游戏开始时间(毫秒)
	 * 
	 * @return 开始时间(毫秒)
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * 游戏开始时，初始化并打开计时器，记录游戏时间
	 */
	public void startTimer() {
		this.timerShow = new Timer();
		timerShow.schedule(timePassingTask, 0, 1000);
	}

	/**
	 * 获取用于显示时间的定时器
	 * 
	 * @return 定时器对象
	 */
	public Timer getTimerShow() {
		return timerShow;
	}

	/**
	 * 获取允许的最大盘片数量（通过计算得出）
	 * 
	 * @see #calculateMaxDiskNum()
	 * @return 允许的最大盘片数量
	 */
	public int getDiskMaxNum() {
		return diskMaxNum;
	}

}