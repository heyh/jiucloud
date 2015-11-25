package sy.util;

public class ParaUtil {

	public static final int ERROY_CODE = 300;
	public static final int SUCC_CODE = 200;
	public static final int EXIT_CODE = 400;


	public static final String LOG_SUCC_MSG = "认证成功!";
	public static final String LOG_ERROY_MSG = "认证失败!";
	public static final String REGE_ERROY_MSG = "该用户已注册!";
	public static final String REGE_SUCC_MSG = "注册成功!";
	public static final String REGE_ERROY_MSG_302 = "用户ID为空!";
	public static final String CREATE_TASK_MSG = "创建任务成功!";
	public static final String CREATE_TASK_EROY_MSG = "创建任务失败!";
	public static final String CREATE_TALK_SUCC_MSG = "会话成功!";
	public static final String CREATE_TALK_EROY_MSG = "会话失败!";
	public static final String CREATE_TALK_SUS_MSG = "获取会话列表成功";
	public static final String CREATE_IMG_SUS_MSG = "S";
	public static final String CREATE_IMG_EROY_MSG = "N";
	public static final String CREATE_IMG_EXIT_MSG = "任务已经存在!";
	
	public static void main(String[] args) {
		String s="13900139001;13800138001;13600136001;13500135001";
		System.out.println(s.replace(s, "13800138001"));
	}
	
}
