package com.betel.cac.server.room.beans;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.betel.cac.core.consts.Game;
import com.betel.cac.server.room.business.RoomBusiness;
import com.betel.cac.server.room.constants.RoomRoleState;
import com.betel.cac.server.room.constants.RoomState;
import com.betel.session.Session;
import com.betel.utils.JsonUtils;

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
    private List<RoomRole> roleList;

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

    public List<RoomRole> getRoleList()
    {
        return roleList;
    }

    public void setRoleList(List<RoomRole> roleList)
    {
        this.roleList = roleList;
    }

    public void fromJson(JSONObject json)
    {
        this.setGame(Game.valueOf(json.getString("game")));
        this.setGameMode(json.getString("gameMode"));
        this.setMaxRoleNum(4);
        this.setRoomState(RoomState.Preparing);

        JSONArray roleJsonArray = json.getJSONArray("roleList");
        roleList = new ArrayList<>();
        for (int i = 0; i < roleJsonArray.size(); i++)
        {
            RoomRole roomRole = new RoomRole();
            roomRole.fromJson(roleJsonArray.getJSONObject(i));
            roomRole.setRoomRoleState(RoomRoleState.UnReady);
            roleList.add(roomRole);
        }
    }
    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("game", game.toString());
        json.put("gameMode", gameMode);

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < roleList.size(); i++)
            jsonArray.add(roleList.get(i).toJson());
        json.put("roleList", jsonArray);
        return json;
    }
}
