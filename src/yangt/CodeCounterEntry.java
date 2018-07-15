package yangt;

/**
 * 计算代码量的入口函数
 * 
 * @author 杨唐
 */
public class CodeCounterEntry {

	public static void main(String[] args) {
		new CodeCounter("src/yangt").startCount(true);
	}

}