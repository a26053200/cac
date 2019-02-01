package com.betel.cac.server.room.beans;

import com.alibaba.fastjson.JSONObject;
import com.betel.cac.server.room.constants.RoomRoleState;

/**
 * @ClassName: 房间中的角色
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2019/1/30 23:17
 */
public class RoomRole
{
    private String roleId;
    private String channelId;               //频道ID 用于推送
    private String roleName;                //角色名
    private int sex;                        //性别 0男 1女 其他未知
    private int headIcon;                   //头像 0使用微信头像 其他 自定义
    private RoomRoleState roomRoleState;    //角色当前状态
    private boolean isRobot;                //角色是否是机器人

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getChannelId()
    {
        return channelId;
    }

    public void setChannelId(String channelId)
    {
        this.channelId = channelId;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public int getSex()
    {
        return sex;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public int getHeadIcon()
    {
        return headIcon;
    }

    public void setHeadIcon(int headIcon)
    {
        this.headIcon = headIcon;
    }

    public RoomRoleState getRoomRoleState()
    {
        return roomRoleState;
    }

    public void setRoomRoleState(RoomRoleState roomRoleState)
    {
        this.roomRoleState = roomRoleState;
    }

    public boolean isRobot()
    {
        return isRobot;
    }

    public void setRobot(boolean robot)
    {
        isRobot = robot;
    }

    public void fromJson(JSONObject json)
    {
        roleId      = json.getString("roleId");
        channelId   = json.getString("channelId");
        roleName    = json.getString("roleName");
        sex         = json.getInteger("sex");
        headIcon    = json.getInteger("headIcon");
        roleId      = json.getString("roleId");
        isRobot     = json.getBoolean("isRobot");
        if (isRobot)
            roomRoleState = RoomRoleState.Reaady;//机器人默认就是准备的
    }
    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        json.put("roleId", roleId);
        json.put("roleName", roleName);
        json.put("sex", sex);
        json.put("headIcon", headIcon);
        json.put("roomRoleState", roomRoleState.toString());
        return json;
    }
}
