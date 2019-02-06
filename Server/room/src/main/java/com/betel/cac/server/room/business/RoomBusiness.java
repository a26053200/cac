package com.betel.cac.server.room.business;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.Business;
import com.betel.cac.core.consts.Action;
import com.betel.cac.core.consts.Push;
import com.betel.cac.core.consts.ReturnCode;
import com.betel.cac.core.consts.ServerName;
import com.betel.cac.server.room.beans.Room;
import com.betel.cac.server.room.beans.RoomRole;
import com.betel.cac.server.room.constants.RoomRoleState;
import com.betel.cac.server.room.constants.RoomState;
import com.betel.session.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * @ClassName: RoomBusiness
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2019/1/30 23:25
 */
public class RoomBusiness extends Business<Room>
{
    private class Field
    {
        static final String RoomId = "roomId";
        static final String IndexInRoom = "indexInRoom";
        static final String Game = "game";
        static final String GameMode = "gameMode";
        static final String RoleList = "roleList";
        static final String RoomInfo = "roomInfo";
        static final String RoleState = "roleState";
        static final String Progress = "progress";
    }

    final static Logger logger = LogManager.getLogger(RoomBusiness.class);

    private int roomCounter = 1;

    private HashMap<Integer, Room> roomMap;

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
            case Action.ROOM_CREATE:
                createRoom(session);
                break;
            case Action.ROOM_ROLE_STATE:
                updateState(session);
                break;
            default:
                logger.error("Unknown action:" + method);
                break;
        }
    }

    private void createRoom(Session session)
    {
        Room room = new Room();
        room.setId(roomCounter++);
        room.fromJson(session.getRecvJson());
        roomMap.put(room.getId(), room);

        JSONObject sendJson = room.toJson();

        for (int i = 0; i < room.getRoleList().size(); i++)
        {
            RoomRole role = room.getRoleList().get(i);
            //如果是机器人，直接设置为准备状态
            role.setRoomRoleState(RoomRoleState.Ready);
            if (role.getChannelId() != null)
            {
                monitor.pushToClient(role.getChannelId(), ServerName.JSON_GATE_SERVER, Push.ROOM_CREATE, sendJson);
            }
        }
    }

    //准备
    private void updateState(Session session)
    {
        int roomId = session.getRecvJson().getIntValue(Field.RoomId);
        RoomRoleState state = RoomRoleState.valueOf(session.getRecvJson().getString(Field.RoleState));
        Room room = roomMap.get(roomId);
        if (room != null)
        {
            int index = session.getRecvJson().getIntValue(Field.IndexInRoom);
            int progress = session.getRecvJson().getIntValue(Field.Progress);
            RoomRole role = room.getRoleList().get(index);
            role.setRoomRoleState(state); // 切换状态
            if(state == RoomRoleState.Loading)
                room.setRoomState(RoomState.Loading);
            if(progress >= 100)
                role.setRoomRoleState(RoomRoleState.LoadComplete);//加载完成
            // 推送已经准备好的玩家
            JSONObject stateJson = new JSONObject();
            stateJson.put(Field.IndexInRoom,index);
            stateJson.put(Field.RoleState,state);
            stateJson.put(Field.Progress,progress);
            room.allPush(monitor, ServerName.JSON_GATE_SERVER, Push.ROOM_ROLE_STATE, stateJson);

            //所有玩家都已经准备,开始推送加载
            if (room.isAllState(RoomRoleState.Ready))
            {
                room.setRoomState(RoomState.Ready);
                JSONObject sendJson = new JSONObject();
                room.allPush(monitor, ServerName.JSON_GATE_SERVER, Push.ROOM_LOAD_START, sendJson);
            }
            //所有玩家都已经加载完成，开始推送游戏开始
            if (room.isAllState(RoomRoleState.LoadComplete))
            {
                room.setRoomState(RoomState.Ingame);
                JSONObject sendJson = new JSONObject();
                room.allPush(monitor, ServerName.JSON_GATE_SERVER, Push.ROOM_GAME_START, sendJson);
            }
        } else
        {
            rspdMessage(session, ReturnCode.Error_room_not_found);
        }
    }
}
