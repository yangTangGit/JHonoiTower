package yangt.henoi_tower;

/**
 * 河内塔算法实现类
 */
public class HenoiTower {
	/**
	 * 递归移动盘子
	 * 
	 * @param n
	 *            盘子的数目
	 * @param origin
	 *            源座
	 * @param assist
	 *            辅助座
	 * @param destination
	 *            目的座
	 */
	public void hanoi(int n, String origin, String assist, String destination) {
		if (n == 1) {
			move(origin, destination);
		} else {
			hanoi(n - 1, origin, destination, assist);
			move(origin, destination);
			hanoi(n - 1, assist, origin, destination);
		}
	}

	/**
	 * Print the route of the movement.
	 * 
	 * @param origin
	 *            源柱子
	 * @param destination
	 *            目的柱子
	 */
	private void move(String origin, String destination) {
		System.out.println("Direction:" + origin + "--->" + destination);
	}

}