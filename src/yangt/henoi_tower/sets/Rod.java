package yangt.henoi_tower.sets;

import java.util.NoSuchElementException;

import yangt.henoi_tower.HenoiFrame;
import yangt.henoi_tower.widget.JHenoiDisk;

/**
 * 保存柱子的状态（盘片的数量、大小、位置）
 */
public class Rod {

	/** 柱子A的名称（标识名）. */
	public static final String ROD_A_NAME = "rod_A";
	/** 柱子B的名称（标识名）. */
	public static final String ROD_B_NAME = "rod_B";
	/** 柱子C的名称（标识名）. */
	public static final String ROD_C_NAME = "rod_C";

	/**
	 * 柱子的名称标识
	 */
	private String rodName;

	/**
	 * 宽度中点的x坐标
	 */
	private int rodCenterX;

	/**
	 * 柱子底端的y坐标值
	 */
	private int rodBottomY;

	/**
	 * 盘片的厚度(高)
	 */
	private int diskHigh;

	/**
	 * @see HenoiFrame#diskVerticalInterval
	 */
	private int diskVerticalInterval;

	/**
	 * 存储柱子上的盘片信息
	 */
	JHenoiDiskStack diskStack;

	public Rod(String rodName, int rodCenterX, int rodBottomY, int diskHigh, int diskVerticalInterval) {
		this.rodName = rodName;
		this.rodCenterX = rodCenterX;
		this.rodBottomY = rodBottomY;
		this.diskHigh = diskHigh;
		this.diskVerticalInterval = diskVerticalInterval;
		initial();
	}

	/**
	 * 初始化参数
	 */
	private void initial() {
		diskStack = new JHenoiDiskStack();
	}

	/**
	 * 判断是否可以在柱子上放置指定的盘片
	 * 
	 * @param jHenoiDisk
	 *            指定的盘片
	 * @return 能放置时放回true，否则返回false
	 */
	public boolean canPush(JHenoiDisk jHenoiDisk) {
		try {
			return jHenoiDisk.getWidth() < this.getTopDisk().getWidth();
		} catch (NoSuchElementException e) {
			return true; // 当柱子上没有盘片时，说明可以放置
		}
	}

	/**
	 * 计算下一个盘片的y坐标
	 * 
	 * @return 下一个欲放置的盘片的y坐标
	 */
	public int nextDiskY() {
		return rodBottomY - (getDiskStack().size() + 1) * (diskHigh + diskVerticalInterval) + diskVerticalInterval;
	}

	/* set and get method. */

	/**
	 * 设置柱子的名称标识
	 * 
	 * @param rodName
	 *            柱子的名称标识
	 */
	public void setRodName(String rodName) {
		this.rodName = rodName;
	}

	/**
	 * 获取柱子的名称标识
	 * 
	 * @return 柱子的名称标识
	 */
	public String getRodName() {
		return rodName;
	}

	/**
	 * 设置柱子的中心x坐标
	 * 
	 * @param rodCenterX
	 *            柱子的中心x坐标
	 */
	public void setRodCenterX(int rodCenterX) {
		this.rodCenterX = rodCenterX;
	}

	/**
	 * 获取柱子的中心x坐标
	 * 
	 * @return 柱子的中心x坐标
	 */
	public int getRodCenterX() {
		return rodCenterX;
	}

	/**
	 * 获取当前柱子上的盘片栈
	 * 
	 * @see JHenoiDiskStack
	 * @return 柱子上的盘片栈
	 */
	public JHenoiDiskStack getDiskStack() {
		return diskStack;
	}

	/**
	 * 获取当前柱子底端的盘片
	 * 
	 * @return 当前柱子底端的盘片
	 */
	public JHenoiDisk getBottomDisk() {
		return diskStack.firstElement();
	}

	/**
	 * 获取当前柱子顶端的盘片
	 * 
	 * @return 当前柱子顶端的盘片
	 */
	public JHenoiDisk getTopDisk() {
		return diskStack.lastElement();
	}

}