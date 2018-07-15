package yangt.henoi_tower;

import java.util.Date;
import java.util.TimerTask;

import javax.swing.JLabel;

/**
 * 显示使用时间的任务类
 */
public class TimePassingTask extends TimerTask {

	/**
	 * 运行此任务的frame类
	 */
	private HenoiFrame henoiFrame;

	/**
	 * 显示时间的JLabel类
	 * 
	 * @see JLabel
	 */
	private JLabel showTime;

	public TimePassingTask(HenoiFrame henoiFrame, JLabel showTime) {
		this.henoiFrame = henoiFrame;
		this.showTime = showTime;
	}

	@Override
	public void run() {
		long usedTime = (new Date().getTime() - henoiFrame.getStartTime()) / 1000;
		String timeStr;
		if (usedTime < 3600) {
			timeStr = usedTime / 60 + "分" + usedTime % 60 + "秒";
		} else {
			timeStr = usedTime / 3600 + "时" + usedTime / 60 + "分" + usedTime % 60 + "秒";
		}
		showTime.setText("使用时间：" + timeStr);
	}

}