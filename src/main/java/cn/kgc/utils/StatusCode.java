package cn.kgc.utils;

/**
 * 统一定义各个状态码所代表的含义
 */
public class StatusCode {
    public static final Integer OK = 20000;            //操作成功
    public static final Integer LOGIN_FAIL = 20001;    //登录失败
    public static final Integer SAVE_USER_FAIL = 20002; //保存用户失败
    public static final Integer UPDATE_USER_FAIL = 20003; //保存用户失败
    public static final Integer DELETE_USER_FAIL = 20004; //删除用户失败
    public static final Integer SAVE_MENU_FAIL = 20005;   // 保存菜单失败
    public static final Integer DELETE_MENU_FAIL = 20006; //删除菜单失败
    public static final Integer SAVE_ROLE_FAIL = 20007;   // 保存角色失败
    public static final Integer DELETE_ROLE_FAIL = 20008;  //删除角色失败
    public static final Integer UPDATE_PASSWORD_FAIL = 20009;  //修改密码失败
    public static final Integer ACCESS_DENIED = 20010;      //缺少权限

    public static final Integer EXPIRED_TOKEN = 50014;       //令牌过期
    public static final Integer ILLEGAL_TOKEN = 50008;       //令牌非法


}
