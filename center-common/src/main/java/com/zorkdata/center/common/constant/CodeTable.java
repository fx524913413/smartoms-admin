package com.zorkdata.center.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuzhigang
 * @date 2018/3/21 15:18
 */
public class CodeTable {

    private static final Map<String, String> CODE_TABLE = new HashMap<>(500);

    /**
     * 正常 key+value
     */
    public final static String SUCCESS = "000000";

    /**
     * 失败
     */
    public static final String FAILED = "100001";
    /**
     * master数量超过限制
     */
    public static final String MasterTooMuch = "100002";
    /**
     * 没有可以安装的机器 "
     */
    public static final String NoComputerToInstall = "100003";
    /**
     * 查询成功，无结果
     */
    public final static String SUCCESS_NODATA = "000001";
    /**
     * 查询成功，返回的JSON有误
     */
    public final static String SUCCESS_JSON_ERROR = "000002";
    /**
     * 查询成功，标准化时，部分属性转换不成功，但是还能返回数据
     */
    public final static String STANDARD_ISSUE = "000003";
    /**
     * 缓存有效期内无记录
     */
    public final static String SUCCESS_CACHE_NOTEXIST = "000004";

    /**
     * 执行结束,部分成功,部分失败
     */
    public final static String SUCCESS_WITH_FAILURE = "000005";

    /**
     * 已存在
     */
    public static final String ALREADY_EXIST = "000010";

    /**
     * 账号不存在
     */
    public final static String ACOUNT_NOTEXIST = "000100";
    /**
     * 账户被禁用
     */
    public final static String ACOUNT_DISABLED = "000101";


    /**
     * 用户未登陆
     */
    public final static String NOT_LOGIN = "000102";

    /**
     * 没有该接口的访问权限
     */
    public final static String INTERFACE_AUTH_ERROR = "000103";
    /**
     * 参数错误
     */
    public final static String PARAM_ERROR = "000104";
    /**
     * 非法IP请求
     */
    public final static String ILLEGAL_IP_ERROR = "000107";

    /**
     * 账户者密码错误
     */
    public final static String ACOUNT_ERROR = "000108";

    /**
     * 用户不属于当前产品
     */
    public final static String NOT_IN_PRODUCT = "000109";

    /**
     * 系统异常
     */
    public final static String UNKNOWN_ERROR = "000999";

    /**
     * agent认证失败
     */
    public static final String AGENT_AUTHENTICATION_ERROR = "999989";

    /**
     * agent执行失败
     */
    public static final String AGENT_EXCUTE_ERROR = "999988";

    public static final String PROCESS_NOT_DEPLOY = "999899";

    public static final String ENCODING_ERROR = "999799";

    public static final String DELETE_ERROR = "999699";

    // token
    public static final String TOKEN_ERROR_CODE = "040101";
    public static final String TOKEN_FORBIDDEN_CODE = "040301";
    // 用户token异常
    public static final String EX_USER_INVALID_CODE = "040101";

    /**
     * 操作分组失败
     */
    public static final String OPERATION_GROUP = "999599";

    /**
     * 权限不足，无法操作
     */
    public static final String NOT_ALLOW = "111111";
    public static final String IP_DUPLICATION = "111112";

    static {
        CODE_TABLE.put(SUCCESS, "正常");
        CODE_TABLE.put(FAILED, "失败");

        CODE_TABLE.put(SUCCESS_NODATA, "查询成功，无结果");
        CODE_TABLE.put(SUCCESS_JSON_ERROR, "查询成功，返回的JSON有误");
        CODE_TABLE.put(STANDARD_ISSUE, "查询成功，标准化时，部分属性转换不成功，但是还能返回数据");
        CODE_TABLE.put(SUCCESS_CACHE_NOTEXIST, "缓存有效期内无记录");

        CODE_TABLE.put(ACOUNT_NOTEXIST, "账户不存在");
        CODE_TABLE.put(ALREADY_EXIST, "已经存在");
        CODE_TABLE.put(ACOUNT_DISABLED, "账户被禁用");
        CODE_TABLE.put(NOT_LOGIN, "用户未登陆");
        CODE_TABLE.put(INTERFACE_AUTH_ERROR, "没有该接口的访问权限");
        CODE_TABLE.put(PARAM_ERROR, "args参数错误");
        CODE_TABLE.put(ILLEGAL_IP_ERROR, "非法IP请求");

        CODE_TABLE.put(UNKNOWN_ERROR, "provider端系统异常");
        CODE_TABLE.put(AGENT_AUTHENTICATION_ERROR, "Agent认证失败");
        CODE_TABLE.put(AGENT_EXCUTE_ERROR, "Agent执行异常");

        CODE_TABLE.put(PROCESS_NOT_DEPLOY, "流程未发布");
        CODE_TABLE.put(ENCODING_ERROR, "编码错误");
        CODE_TABLE.put(DELETE_ERROR, "删除错误");
        CODE_TABLE.put(OPERATION_GROUP, "操作分组失败");
        CODE_TABLE.put(ACOUNT_ERROR, "用户名或者密码错误");
        CODE_TABLE.put(TOKEN_ERROR_CODE, "TOKEN错误");
        CODE_TABLE.put(TOKEN_FORBIDDEN_CODE, "TOKEN不被允许");
        CODE_TABLE.put(EX_USER_INVALID_CODE, "登录超时请重新登录");
        CODE_TABLE.put(NOT_ALLOW, "权限不足，无法操作");
        CODE_TABLE.put(IP_DUPLICATION, "该IP已在数据库存在或者插入出错");
        CODE_TABLE.put(MasterTooMuch, "此域内的Master数量已经超过或达到规定数量（可能是由于别的用于已经在此域内添加了Master）");
        CODE_TABLE.put(SUCCESS_WITH_FAILURE, "执行结束,部分成功,部分失败");
        CODE_TABLE.put(NoComputerToInstall, "没有可以安装的机器，请先添加机器");
        CODE_TABLE.put(NOT_IN_PRODUCT, "用户不属于当前产品");
    }

    public static String getDescription(String code) {
        return CODE_TABLE.get(code);
    }
}
