package yangt.henoi_tower;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Date;

import javax.swing.event.MouseInputListener;

import yangt.henoi_tower.sets.Rod;
import yangt.henoi_tower.widget.JHenoiDisk;

/**
 * 盘片拖动监听，盘片停止拖动时根据情况选择放置位置
 */
public class DiskDragListener implements MouseMotionListener, MouseInputListener {

	/** 记录当前河内塔的状况，用于计时和计数. */
	private HenoiFrame owner;

	/** 当前拖动的盘片. */
	private JHenoiDisk jHenoiDisk;

	/** 盘片的总数量. */
	private int diskNum;

	/** 第一根柱子（A）的信息. */
	private Rod rodA;

	/** 第二根柱子（B）的信息. */
	private Rod rodB;

	/** 第三根柱子（C）的信息. */
	private Rod rodC;

	/**
	 * 0~3分别对应柱子A、柱子B、柱子C的中心x坐标
	 */
	private int[] rodCenterX = new int[3];

	/** 拖拽起点的x坐标. */
	private Integer dragStartX = null;

	/** 拖拽起点的y坐标. */
	private Integer dragStartY = null;

	/**
	 * 鼠标释放前是否拖动鼠标
	 */
	private boolean isDragged = false;

	/**
	 * 拖放到柱子的有效识别半径值
	 */
	private int validDragRadius;

	/**
	 * 盘片未移动时的x坐标
	 */
	private int originalX;

	/**
	 * 盘片未移动时的y坐标
	 */
	private int originalY;

	public DiskDragListener(HenoiFrame owner, JHenoiDisk jHenoiDisk, int diskNum) {
		this.owner = owner;
		this.jHenoiDisk = jHenoiDisk;
		this.diskNum = diskNum;
		this.validDragRadius = (jHenoiDisk.getWidth() > 40) ? (jHenoiDisk.getWidth() / 2) : 20;
		setOriginalXY(jHenoiDisk);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!isDragged) {
			return; // 未拖动而释放鼠标时不往下执行
		}
		dragStartX = null;
		dragStartY = null;
		String rodName = findRodByDisk(jHenoiDisk).getRodName();
		int diskHalfWidth = jHenoiDisk.getWidth() / 2;
		int diskCenterX = jHenoiDisk.getX() + diskHalfWidth; // 盘片竖直中心线的x坐标
		switch (rodName) {
		case Rod.ROD_A_NAME:
			if (Math.abs(diskCenterX - rodCenterX[1]) < validDragRadius && rodB.canPush(jHenoiDisk)) {
				jHenoiDisk.setLocation(rodCenterX[1] - diskHalfWidth, rodB.nextDiskY());
				setOriginalXY(jHenoiDisk);
				rodB.getDiskStack().push(rodA.getDiskStack().pop());
				owner.setSteps(true);
			} else if (Math.abs(diskCenterX - rodCenterX[2]) < validDragRadius && rodC.canPush(jHenoiDisk)) {
				jHenoiDisk.setLocation(rodCenterX[2] - diskHalfWidth, rodC.nextDiskY());
				setOriginalXY(jHenoiDisk);
				rodC.getDiskStack().push(rodA.getDiskStack().pop());
				owner.setSteps(true);
			} else {
				jHenoiDisk.setLocation(originalX, originalY); // 放回原位置
			}
			break;
		case Rod.ROD_B_NAME:
			if (Math.abs(diskCenterX - rodCenterX[0]) < validDragRadius && rodA.canPush(jHenoiDisk)) {
				jHenoiDisk.setLocation(rodCenterX[0] - diskHalfWidth, rodA.nextDiskY());
				setOriginalXY(jHenoiDisk);
				rodA.getDiskStack().push(rodB.getDiskStack().pop());
				owner.setSteps(true);
			} else if (Math.abs(diskCenterX - rodCenterX[2]) < validDragRadius && rodC.canPush(jHenoiDisk)) {
				jHenoiDisk.setLocation(rodCenterX[2] - diskHalfWidth, rodC.nextDiskY());
				setOriginalXY(jHenoiDisk);
				rodC.getDiskStack().push(rodB.getDiskStack().pop());
				owner.setSteps(true);
			} else {
				jHenoiDisk.setLocation(originalX, originalY); // 放回原位置
			}
			break;
		case Rod.ROD_C_NAME:
			if (Math.abs(diskCenterX - rodCenterX[0]) < validDragRadius && rodA.canPush(jHenoiDisk)) {
				jHenoiDisk.setLocation(rodCenterX[0] - diskHalfWidth, rodA.nextDiskY());
				setOriginalXY(jHenoiDisk);
				rodA.getDiskStack().push(rodC.getDiskStack().pop());
				owner.setSteps(true);
			} else if (Math.abs(diskCenterX - rodCenterX[1]) < validDragRadius && rodB.canPush(jHenoiDisk)) {
				jHenoiDisk.setLocation(rodCenterX[1] - diskHalfWidth, rodB.nextDiskY());
				setOriginalXY(jHenoiDisk);
				rodB.getDiskStack().push(rodC.getDiskStack().pop());
				owner.setSteps(true);
			} else {
				jHenoiDisk.setLocation(originalX, originalY); // 放回原位置
			}
			break;
		}
		// 拖放结束时判断是否成功
		if (rodC.getDiskStack().size() == diskNum && rodA.getDiskStack().size() == 0
				&& rodB.getDiskStack().size() == 0) {
			owner.getTimerShow().cancel();
			new SuccessThread(owner).start();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!findRodByDisk(jHenoiDisk).getTopDisk().equals(jHenoiDisk)) {
			return; // 当拖动的不是顶端的盘片时，不往下执行
		}
		if (owner.getTimerShow() == null) {
			System.out.println("游戏开始");
			owner.setStartTime(new Date().getTime());
			owner.startTimer();
		}
		if (dragStartX == null || dragStartY == null) {
			isDragged = true;
			dragStartX = e.getX();
			dragStartY = e.getY();
		}
		jHenoiDisk.setLocation(jHenoiDisk.getX() + e.getX() - dragStartX, jHenoiDisk.getY() + e.getY() - dragStartY);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	/**
	 * 根据盘片找出所在的柱子信息变量
	 * 
	 * @param jHenoiDisk
	 *            指定盘片
	 * @return 盘片所在的柱子
	 */
	private Rod findRodByDisk(JHenoiDisk jHenoiDisk) {
		if (rodA.getDiskStack().indexOf(jHenoiDisk) != -1) {
			return rodA;
		} else if (rodB.getDiskStack().indexOf(jHenoiDisk) != -1) {
			return rodB;
		} else { // 只有三根柱子，而且每个盘片必须属于三根柱子之一
			return rodC;
		}
	}

	/**
	 * 设置盘片未移动时的X、Y坐标
	 * 
	 * @param jHenoiDisk
	 *            盘片
	 */
	private void setOriginalXY(JHenoiDisk jHenoiDisk) {
		this.originalX = jHenoiDisk.getX();
		this.originalY = jHenoiDisk.getY();
	}

	/**
	 * 批量传入参数
	 * 
	 * @param rodA
	 *            柱子A
	 * @param rodB
	 *            柱子B
	 * @param rodC
	 *            柱子C
	 * @param rodCenterX
	 *            0~3分别对应柱子A、柱子B、柱子C的中心x坐标
	 */
	public void setParams(Rod rodA, Rod rodB, Rod rodC, int[] rodCenterX) {
		this.rodA = rodA;
		this.rodB = rodB;
		this.rodC = rodC;
		this.rodCenterX = rodCenterX;
	}

}