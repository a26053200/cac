package com.betel.cac.server.room.constants;

/**
 * @ClassName: 房间状态
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2019/1/30 22:50
 */
public enum RoomState
{
    /**
     * 准备中
     */
    Preparing,

    /**
     * 准备好了
     */
    Ready,

    /**
     * 加载游戏
     */
    Loading,

    /**
     * 游戏中
     */
    Ingame,

    /**
     * 结束
     */
    Over,
}
