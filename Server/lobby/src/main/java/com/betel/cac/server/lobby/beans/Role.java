package com.betel.cac.server.lobby.beans;

/**
 * @ClassName: Role
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2019/1/19 22:50
 */
public class Role
{
    private String id;
    private String playerId;
    private String registerTime; //第一次创建角色时间
    private String lastLoginTime;//最后一次登陆时间
    private String lastLogoutTime;//最后一次登出时间
    private String roleName;    //角色名
    private int sex; //性别 0男 1女 其他未知
    private int headIcon;//头像 0使用微信头像 其他 自定义

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(String playerId)
    {
        this.playerId = playerId;
    }

    public String getRegisterTime()
    {
        return registerTime;
    }

    public void setRegisterTime(String registerTime)
    {
        this.registerTime = registerTime;
    }

    public String getLastLoginTime()
    {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime)
    {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLogoutTime()
    {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(String lastLogoutTime)
    {
        this.lastLogoutTime = lastLogoutTime;
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
}
