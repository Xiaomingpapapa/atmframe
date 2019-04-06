package cn.ehi.manage.status;


/**
 * luckyframe的任务状态
 * @Description:TODO
 * @author:程文月
 * @time:2018年3月22日 上午9:48:06
 */
public enum TaskStatus {
	EXECUTING(1),SUCCESS(2),FAIL(4),UNEXCUTE(0);
	private int  value=0;
	private TaskStatus(int value)
	{
		this.value = value;
	}
	public int value() {
		return value;
	}
}
