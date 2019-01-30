package com.betel.cac.core.consts;

/**
 * @ClassName: Action
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/8 0:55
 */
public class Action
{
    //==================
    // Account
    //==================
    /**
     * 登陆帐号服务器
     */
    public final static String ACCOUNT_LOGIN = "account_login";
    /**
     * 登陆帐号服务器
     */
    public final static String ACCOUNT_REGISTER = "account_register";

    //==================
    // Player
    //==================
    /**
     * 登陆游戏服务器
     */
    public final static String PLAYER_LOGIN = "player_login";

    /**
     * 登出游戏服务器
     */
    public final static String PLAYER_LOGOUT = "player_logout";

    //==================
    // Role
    //==================
    /**
     * 获取随机角色名
     */
    public final static String ROLE_RANDOM_NAME = "role_random_name";
    /**
     * 角色创建
     */
    public final static String ROLE_CREATE = "role_create";
    /**
     * 选择角色并进入游戏
     */
    public final static String ROLE_ENTER_GAME = "role_enter_game";

    //==================
    // Match
    //==================
    /**
     * 加入匹配 (真实玩家)
     */
    public final static String JOIN_MATCH = "join_match";
    /**
     * 机器人加入匹配
     */
    public final static String ROBOT_JOIN_MATCH = "robot_join_match";


    //==================
    // Room
    //==================
    /**
     * 创建房间
     */
    public final static String ROOM_CREATE = "create_room";

}
