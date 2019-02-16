package com.betel.cac.server.robot.beans;

import com.alibaba.fastjson.JSONObject;
import com.betel.cac.core.consts.RoomRoleState;

public class Robot
{
    private String roleId;
    private String roleName;    //角色名
    private int sex;
    private int headIcon;
    private RoomRoleState roleState;    //角色当前状态
    private boolean isRobot;                //角色是否是机器人

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
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

    public RoomRoleState getRoleState()
    {
        return roleState;
    }

    public void setRoleState(RoomRoleState roleState)
    {
        this.roleState = roleState;
    }

    public boolean isRobot()
    {
        return isRobot;
    }

    public void setRobot(boolean robot)
    {
        isRobot = robot;
    }

    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        json.put("roleId", roleId);
        json.put("roleName", roleName);
        json.put("sex", sex);
        json.put("headIcon", headIcon);
        json.put("isRobot", isRobot);
        json.put("roleState", roleState.toString());
        return json;
    }
}
