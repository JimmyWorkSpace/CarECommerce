package com.ruoyi.common.constant;

/**
 * 用戶常量資訊
 * 
 * @author ruoyi
 */
public class UserConstants
{
    /**
     * 平台內系統用戶的唯一標誌
     */
    public static final String SYS_USER = "SYS_USER";

    /** 正常狀態 */
    public static final String NORMAL = "0";

    /** 異常狀態 */
    public static final String EXCEPTION = "1";

    /** 用戶封禁狀態 */
    public static final String USER_DISABLE = "1";

    /** 角色封禁狀態 */
    public static final String ROLE_DISABLE = "1";

    /** 部門正常狀態 */
    public static final String DEPT_NORMAL = "0";

    /** 部門停用狀態 */
    public static final String DEPT_DISABLE = "1";

    /** 字典正常狀態 */
    public static final String DICT_NORMAL = "0";

    /** 是否為系統預設（是） */
    public static final String YES = "Y";

    /** 是否選單外鏈（是） */
    public static final String YES_FRAME = "0";

    /** 是否選單外鏈（否） */
    public static final String NO_FRAME = "1";

    /** 菜单類型（目录） */
    public static final String TYPE_DIR = "M";

    /** 菜单類型（菜单） */
    public static final String TYPE_MENU = "C";

    /** 菜单類型（按钮） */
    public static final String TYPE_BUTTON = "F";

    /** Layout组件标识 */
    public final static String LAYOUT = "Layout";
    
    /** ParentView组件标识 */
    public final static String PARENT_VIEW = "ParentView";

    /** InnerLink组件标识 */
    public final static String INNER_LINK = "InnerLink";

    /** 校验返回结果码 */
    public final static String UNIQUE = "0";
    public final static String NOT_UNIQUE = "1";

    /**
     * 用户名长度限制
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 20;
}
