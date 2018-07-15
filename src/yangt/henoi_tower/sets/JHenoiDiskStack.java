package yangt.henoi_tower.sets;

import java.util.Stack;

import yangt.henoi_tower.widget.JHenoiDisk;

/**
 * 存储盘片时用的栈类
 * 
 * @see JHenoiDisk
 * @see java.util.Stack
 */
public class JHenoiDiskStack extends Stack<JHenoiDisk> {

	public JHenoiDiskStack() {
	}

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = 6503189629447890741L;

	/**
	 * 返回最后添加的元素，即 栈顶元素
	 * 
	 * @see Stack#lastElement()
	 */
	@Override
	public synchronized JHenoiDisk lastElement() {
		return super.lastElement();
	}

	@Override
	public JHenoiDisk push(JHenoiDisk item) {
		return super.push(item);
	}

	@Override
	public synchronized JHenoiDisk pop() {
		return super.pop();
	}

	@Override
	public synchronized JHenoiDisk get(int index) {
		return super.get(index);
	}

}