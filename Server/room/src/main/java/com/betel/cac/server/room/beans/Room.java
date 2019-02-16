package com.betel.cac.server.room.beans;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.betel.cac.core.consts.Game;
import com.betel.cac.core.consts.RoomRoleState;
import com.betel.cac.core.consts.RoomState;
import com.betel.common.Monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ROOM
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2019/1/30 22:36
 */
public class Room
{
    private int id;
    private Game game;
    private String gameMode;
    private int maxRoleNum;
    private RoomState roomState;
    private RoomRole[] roleList;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Game getGame()
    {
        return game;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public String getGameMode()
    {
        return gameMode;
    }

    public void setGameMode(String gameMode)
    {
        this.gameMode = gameMode;
    }

    public int getMaxRoleNum()
    {
        return maxRoleNum;
    }

    public void setMaxRoleNum(int maxRoleNum)
    {
        this.maxRoleNum = maxRoleNum;
    }

    public RoomState getRoomState()
    {
        return roomState;
    }

    public void setRoomState(RoomState roomState)
    {
        this.roomState = roomState;
    }

    public RoomRole[] getRoleList()
    {
        return roleList;
    }


    public boolean isAllState(RoomRoleState state)
    {
        for (int i = 0; i < roleList.length; i++)
        {
            RoomRole role = roleList[i];
            if (role != null && role.getRoleState() != state)
                return false;
        }
        return true;
    }

    public boolean pushAll(Monitor monitor, String serverName, String action, JSONObject json)
    {
        for (int i = 0; i < roleList.length; i++)
        {
            RoomRole role = roleList[i];
            if (role != null && role.getChannelId() != null)
            {
                monitor.pushToClient(role.getChannelId(), serverName, action, json);
            }
        }
        return true;
    }

    //只推送真实玩家
    public boolean pushRole(Monitor monitor, String serverName, String action, JSONObject json)
    {
        for (int i = 0; i < roleList.length; i++)
        {
            RoomRole role = roleList[i];
            if (role != null && !role.isRobot())
            {
                monitor.pushToClient(role.getChannelId(), serverName, action, json);
            }
        }
        return true;
    }

    public void fromJson(JSONObject json)
    {
        this.setGame(Game.valueOf(json.getString("game")));
        this.setGameMode(json.getString("gameMode"));
        this.setMaxRoleNum(4);
        this.setRoomState(RoomState.Preparing);
        roleList = new RoomRole[4];

//        JSONArray roleJsonArray = json.getJSONArray("roleList");
//        for (int i = 0; i < roleJsonArray.size(); i++)
//        {
//            RoomRole roomRole = new RoomRole();
//            roomRole.fromJson(roleJsonArray.getJSONObject(i));
//            roomRole.setRoleState(RoomRoleState.UnReady);
//            roleList.add(roomRole);
//        }
    }

    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("game", game.toString());
        json.put("gameMode", gameMode);

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < roleList.length; i++)
        {
            if (roleList[i] != null)
            {
                jsonArray.add(roleList[i].toJson());
            }
        }
        json.put("roleList", jsonArray);
        return json;
    }


}
