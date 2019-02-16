package com.betel.cac.server.room.business;

import com.betel.cac.beans.Role;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.betel.asd.Business;
import com.betel.cac.core.consts.*;
import com.betel.cac.server.room.beans.Room;
import com.betel.cac.server.room.beans.RoomRole;
import com.betel.session.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: RoomBusiness
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2019/1/30 23:25
 */
public class RoomBusiness extends Business<Room>
{

    final static Logger logger = LogManager.getLogger(RoomBusiness.class);

    private int roomCounter = 1;

    private HashMap<Integer, Room> roomMap;

    private HashMap<String, Role> roleMap;

    public RoomBusiness()
    {
        super();
        roomMap = new HashMap<>();
    }

    @Override
    public void Handle(Session session, String method)
    {
        switch (method)
        {
            case Action.CREATE_ROOM:
                createRoom(session);
                break;
            case Action.ENTER_ROOM:
                enterRoom(session);
                break;
            case Action.EXIT_ROOM:
                exitRoom(session);
                break;
            case Action.ROOM_ROLE_STATE:
                updateState(session);
                break;
            default:
                logger.error("Unknown action:" + method);
                break;
        }
    }

    private void exitRoom(Session session)
    {
        int roomId = session.getRecvJson().getIntValue(Field.ROOM_ID);
        int index = session.getRecvJson().getIntValue(Field.POS);
        Room room = roomMap.get(roomId);
        if (room != null)
        {
            room.getRoleList()[index] = null;
            //if(room.getRoleList().size() == 0)
                //roomMap.remove(roomId);
        }
    }

    private void enterRoom(Session session)
    {
        int roomId = session.getRecvJson().getIntValue(Field.ROOM_ID);
        int index = session.getRecvJson().getIntValue(Field.POS);
        Room room = roomMap.get(roomId);
        if (room != null)
        {
            RoomRole roomRole = new RoomRole();
            roomRole.setChannelId(session.getChannelId());
            roomRole.fromJson(session.getRecvJson());
            roomRole.setRoleState(RoomRoleState.UnReady);
            room.getRoleList()[index] = roomRole;
            //首次进入房间的推送整个房间信息
            if (!roomRole.isRobot())
            {
                monitor.pushToClient(roomRole.getChannelId(), ServerName.JSON_GATE_SERVER, Push.ROOM_INFO, room.toJson());
            }
            //广播给其他玩家
            JSONObject roleJson = roomRole.toJson();
            room.pushRole(monitor,ServerName.JSON_GATE_SERVER, Push.ENTER_ROOM,roleJson);
        }
    }

    private void createRoom(Session session)
    {
        Room room = new Room();
        room.setId(roomCounter++);
        room.fromJson(session.getRecvJson());
        roomMap.put(room.getId(), room);
        int robotNum = session.getRecvJson().getInteger(Field.ROBOT_NUM);

        JSONObject sendJson = room.toJson();
        //推送房间信息
        action.rspdClient(session, sendJson);
        // 创建有机器人的房间
        if(robotNum > 0)
        {
            JSONObject otherRobotJson = room.toJson();
            JSONArray array = new JSONArray();
            for (int i = 0; i < robotNum; i++)
            {
                JSONObject robotJson = new JSONObject();
                robotJson.put(Field.POS,i + 1);
                array.add(robotJson);
            }
            otherRobotJson.put(Field.ROBOT_INFO,array);
            //通知机器人进入房间
            monitor.sendToServer(ServerName.ROBOT_SERVER,"robot@" + Action.ROBOT_ENTER_ROOM,otherRobotJson);
        }
    }

    //准备
    private void updateState(Session session)
    {
        int roomId = session.getRecvJson().getIntValue(Field.ROOM_ID);
        RoomRoleState state = RoomRoleState.valueOf(session.getRecvJson().getString(Field.ROLE_STATE));
        Room room = roomMap.get(roomId);
        if (room != null)
        {
            int index = session.getRecvJson().getIntValue(Field.POS);
            int progress = session.getRecvJson().getIntValue(Field.PROGRESS);
            RoomRole role = room.getRoleList()[index];
            role.setRoleState(state); // 切换状态
            if(state == RoomRoleState.Loading)
                room.setRoomState(RoomState.Loading);
            if(progress >= 100)
                role.setRoleState(RoomRoleState.LoadComplete);//加载完成
            // 推送已经准备好的玩家
            JSONObject stateJson = new JSONObject();
            stateJson.put(Field.POS,index);
            stateJson.put(Field.ROLE_STATE,state);
            stateJson.put(Field.PROGRESS,progress);
            room.pushAll(monitor, ServerName.JSON_GATE_SERVER, Push.ROOM_ROLE_STATE, stateJson);

            //所有玩家都已经准备,开始推送加载
            if (room.isAllState(RoomRoleState.Ready))
            {
                room.setRoomState(RoomState.Ready);
                JSONObject sendJson = new JSONObject();
                room.pushAll(monitor, ServerName.JSON_GATE_SERVER, Push.ROOM_LOAD_START, sendJson);
            }
            //所有玩家都已经加载完成，开始推送游戏开始
            if (room.isAllState(RoomRoleState.LoadComplete))
            {
                room.setRoomState(RoomState.Ingame);
                JSONObject sendJson = new JSONObject();
                room.pushAll(monitor, ServerName.JSON_GATE_SERVER, Push.ROOM_GAME_START, sendJson);
            }
        } else
        {
            rspdMessage(session, ReturnCode.Error_room_not_found);
        }
    }
}
