package com.xj.guanquan.common;

/**
 * Created by eric on 2015/8/31.
 */
public class ApiList {
    private static String BASE_API = "http://180.76.154.178/";

    public static String REQUEST_SUCCESS = "2000";
    public static String SERVER_ERROR = "5000";
    public static String CLIENT_ERROR = "4000";
    public static String REQUEST_LOGIN = "4100";

    public static String ACCOUNT_CHECK = BASE_API + "1/user/checkAccount";
    public static String ACCOUNT_REGIST = BASE_API + "1/user/save";
    public static String ACCOUNT_LOGIN = BASE_API + "/1/login";
    public static String ACCOUNT_AUTO_LOGIN = BASE_API + "/1/autoLogin";
    public static String ACCOUNT_FIND_PWD = BASE_API + "1/findPassword";
    public static String FIND_USER_LIST = BASE_API + "/1/find/userList";

    //吐槽
    public static String TUCAO_Friend = BASE_API + "1/tucao/friend";
    public static String TUCAO_Nearby = BASE_API + "1/tucao/nearby";
    public static String TUCAO_Detail = BASE_API + "1/tucao/detail";
    public static String TUCAO_Delete = BASE_API + "1/tucao/delete";
    public static String TUCAO_AddComment = BASE_API + "1/tucao/addComment";
    public static String TUCAO_AddComplain = BASE_API + "1/tucao/addComplain";
    public static String TUCAO_AddLike = BASE_API + "1/tucao/addLike";
}
