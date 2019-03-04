package com.betel.cac.beans;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.betel.asd.BaseCommunication;
import com.betel.asd.interfaces.ICommunicationFilter;
import com.betel.cac.core.consts.Field;
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
public class Room extends BaseCommunication<Role>
{
    private int id;
    private Game game;
    private String gameMode;
    private int maxRoleNum;
    private RoomState roomState;
    private ArrayList<Role> roleList;

    public Room(Monitor monitor, String gatewayName)
    {
        super(monitor, gatewayName);
    }
    public Room(Monitor monitor, String gatewayName, String serverName)
    {
        super(monitor, gatewayName, serverName);
    }
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

    public ArrayList<Role> getRoleList()
    {
        return roleList;
    }

    public boolean isAllState(RoomRoleState state)
    {
        for (int i = 0; i < roleList.size(); i++)
        {
            Role role = roleList.get(i);
            if (role != null && role.getRoleState() != state)
                return false;
        }
        return true;
    }
    public Role getRole(String id)
    {
        for (int i = 0; i < roleList.size(); i++)
        {
            Role role = roleList.get(i);
            if (role != null && id.equals(role.getId()))
                return role;
        }
        return null;
    }

    public void fromJson(JSONObject json)
    {
        this.setId(json.getIntValue("id"));
        this.setGame(Game.valueOf(json.getString("game")));
        this.setGameMode(json.getString("gameMode"));
        this.setMaxRoleNum(4);
        this.setRoomState(RoomState.Preparing);
        roleList = new ArrayList();
        if (json.containsKey("roleList"))
        {
            JSONArray jsonArray = json.getJSONArray("roleList");
            for (int i = 0; i < jsonArray.size(); i++)
            {
                Role role = new Role();
                role.fromJson(jsonArray.getJSONObject(i));
                roleList.add(role);
            }
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
        {
            jsonArray.add(roleList.get(i).toRoomRoleJson());
        }
        json.put("roleList", jsonArray);
        return json;
    }

    @Override
    public void pushAll(String action, JSONObject json, ICommunicationFilter<Role> iCommunicationFilter)
    {
        for (int i = 0; i < roleList.size(); i++)
        {
            Role role = roleList.get(i);
            if (role != null && iCommunicationFilter.filter(role))
            {
                if(role.isRobot())
                {
                    json.put(Field.ROBOT_CLIENT_ID, role.getId());
                    monitor.sendToServer(serverName, action, json);
                }
                else
                    monitor.pushToClient(role.getChannelId(), gatewayName, action, json);
            }
        }
    }

    @Override
    public void pushAll(String action, JSONObject json)
    {
        for (int i = 0; i < roleList.size(); i++)
        {
            Role role = roleList.get(i);
            if (role != null)
            {
                if(role.isRobot())
                {
                    json.put(Field.ROBOT_CLIENT_ID, role.getId());
                    monitor.sendToServer(serverName, action, json);
                }
                else
                    monitor.pushToClient(role.getChannelId(), gatewayName, action, json);
            }
        }
    }
}
