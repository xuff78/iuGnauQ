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
    public static String ACCOUNT_LOGIN = BASE_API + "1/login";
    public static String ACCOUNT_LOGINOUT = BASE_API + "1/logout";
    public static String ACCOUNT_AUTO_LOGIN = BASE_API + "/1/autoLogin";
    public static String ACCOUNT_FIND_PWD = BASE_API + "1/findPassword";
    public static String ADD_FOLLOW = BASE_API + "1/relation/addFollow";
    public static String CANCE_FOLLOW = BASE_API + "1/relation/cancelFollow";
    public static String GET_ALL = BASE_API + "1/dict/getAll";
    public static String UPDATEPWD = BASE_API + "1/user/updatePassword";
    public static String UPDATEUSERDETAIL = BASE_API + "1/user/updateDetail";
    public static String UPDATEAVATAR = BASE_API + "1/user/uploadAvatar";


    //发现
    public static String FIND_USER_LIST = BASE_API + "1/find/userList";
    public static String FIND_GROUP_LIST = BASE_API + "1/find/groupList";
    public static String ADD_LIKE = BASE_API + "1/user/addLike";

    //联系人
    public static String CONTACT_FRIEND_LIST = BASE_API + "1/friend/list";
    public static String CONTACT_FOLLOW_LIST = BASE_API + "1/relation/followList";
    public static String CONTACT_FANS_LIST = BASE_API + "1/relation/fansList";
    public static String USER_DETAIL = BASE_API + "1/user/detail";
    public static String FRIEND_ADD = BASE_API + "1/friend/add";
    public static String BLACK_USER_ADD = BASE_API + "1/user/addBlackUser";

    //推送
    public static String PUSH_Bind = BASE_API + "1/user/bind"; //clientId

    //吐槽
    public static String TUCAO_Publish = BASE_API + "1/tucao/publish";
    public static String TUCAO_Friend = BASE_API + "1/tucao/friend";
    public static String TUCAO_Nearby = BASE_API + "1/tucao/nearby";
    public static String TUCAO_Detail = BASE_API + "1/tucao/detail/";
    public static String TUCAO_Delete = BASE_API + "1/tucao/delete/";
    public static String TUCAO_AddComment = BASE_API + "1/tucao/addComment";
    public static String TUCAO_AddComplain = BASE_API + "1/tucao/addComplain";
    public static String TUCAO_AddLike = BASE_API + "1/tucao/addLike";

    //约会
    public static String DATE_Publish = BASE_API + " 1/dating/publish";
    public static String DATE_Friend = BASE_API + "1/dating/friend";
    public static String DATE_Nearby = BASE_API + "1/dating/nearby";
    public static String DATE_Detail = BASE_API + "1/dating/detail/";
    public static String DATE_Delete = BASE_API + "1/dating/delete/";
    public static String DATE_AddComment = BASE_API + "1/dating/addComment";
    public static String DATE_AddComplain = BASE_API + "1/dating/addComplain";
    public static String DATE_AddLike = BASE_API + "1/dating/addLike";
    public static String DATE_Join = BASE_API + "1/dating/join";
    public static String DATE_JoinedList = BASE_API + "1/dating/joinedList";

    //秘密
    public static String SECRET_Avatar = BASE_API + "1/secret/avatarList";
    public static String SECRET_Publish = BASE_API + "1/secret/publish";
    public static String SECRET_Friend = BASE_API + "1/secret/friend";
    public static String SECRET_Nearby = BASE_API + "1/secret/nearby";
    public static String SECRET_Detail = BASE_API + "1/secret/detail/";
    public static String SECRET_Delete = BASE_API + "1/secret/delete/";
    public static String SECRET_AddComment = BASE_API + "1/secret/addComment";
    public static String SECRET_AddComplain = BASE_API + "1/secret/addComplain";
    public static String SECRET_AddLike = BASE_API + "1/secret/addLike";

    //圈子
    public static String GROUP_DETAIL = BASE_API + "1/group/detail";
    public static String GROUP_MYGROUP = BASE_API + "1/group/myGroup";
    public static String GROUP_MEMBER = BASE_API + "1/group/member";
    public static String GROUP_JOIN = BASE_API + "1/group/join";
    public static String GROUP_CREATE = BASE_API + "1/group/create";
}
