package cn.ehi.manage.status;


/**
 * luckyframe的测试用例状态
 * @Description:
 * @author:程文月
 * @time:2018年3月22日 上午9:48:28
 */
public enum CaseStatus {
	PASS(0),FAIL(1),LOCK(2),UNEXCUTE(4);
	private int  value=0;
	private CaseStatus(int value)
	{
		this.value = value;
	}
	public int value() {
		return value;
	}
}
