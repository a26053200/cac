package com.betel.cac.server.room.business;

import com.betel.asd.interfaces.ICommunicationFilter;
import com.betel.cac.beans.Role;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.betel.asd.Business;
import com.betel.cac.core.consts.*;
import com.betel.cac.beans.Room;
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

    private void createRoom(Session session)
    {
        Room room = new Room(monitor,ServerName.JSON_GATE_SERVER,ServerName.ROBOT_SERVER);
        room.fromJson(session.getRecvJson());
        room.setId(roomCounter++);
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
            monitor.sendToServer(ServerName.ROBOT_SERVER,"robotClient@" + Action.ROBOT_ENTER_ROOM,otherRobotJson);
        }
    }

    private void exitRoom(Session session)
    {
        int roomId = session.getRecvJson().getIntValue(Field.ROOM_ID);
        String roleId = session.getRecvJson().getString(Field.ROLE_ID);
        Room room = roomMap.get(roomId);
        if (room != null)
        {
            //RoomRole roomRole = room.getRoleList()[index];
            //room.getRoleList()[index] = null;
            roomMap.remove(roomId);//移除房间
            //if(room.getRoleList().size() == 0)
                //roomMap.remove(roomId);
            rspdMessage(session, ReturnCode.Room_disband);
            JSONObject robotJson = new JSONObject();
            robotJson.put(Field.ROOM_ID, roomId);
            //通知机器人解散房间
            room.pushAll("robotClient@" + Action.DISBAND_ROOM, robotJson, new ICommunicationFilter<Role>(){
                @Override
                public boolean filter(Role role)
                {
                    return !roleId.equals(role.getId());
                }
            });
        }
    }

    private void enterRoom(Session session)
    {
        int roomId = session.getRecvJson().getIntValue(Field.ROOM_ID);
        Room room = roomMap.get(roomId);
        if (room != null)
        {
            Role roomRole = new Role();
            roomRole.fromJson(session.getRecvJson());
            roomRole.setChannelId(session.getChannelId());
            roomRole.setRoleState(RoomRoleState.UnReady);
            roomRole.setRoomPos(room.getRoleList().size());
            roomRole.setRoomPos(room.getRoleList().size());
            room.getRoleList().add(roomRole);
            JSONObject roomJson = room.toJson();
            roomJson.put(Field.ROBOT_CLIENT_ID, roomRole.getId());
            //首次进入房间的推送整个房间信息
            if(roomRole.isRobot())
                monitor.sendToServer(ServerName.ROBOT_SERVER, Push.ROOM_INFO, roomJson);
            else
                monitor.pushToClient(roomRole.getChannelId(), ServerName.JSON_GATE_SERVER, Push.ROOM_INFO, room.toJson());
            //广播给其他玩家,除了自己
            JSONObject roleJson = roomRole.toRoomRoleJson();
            room.pushAll(Push.ENTER_ROOM, roleJson, new ICommunicationFilter<Role>(){
                @Override
                public boolean filter(Role role)
                {
                    return !roomRole.getId().equals(role.getId());
                }
            });
            //room.pushRole(monitor,ServerName.JSON_GATE_SERVER, Push.ENTER_ROOM,roleJson, roomRole.getId());
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
            String roleId = session.getRecvJson().getString(Field.ROLE_ID);
            int progress = session.getRecvJson().getIntValue(Field.PROGRESS);
            Role role = room.getRole(roleId);
            role.setRoleState(state); // 切换状态
            if(state == RoomRoleState.Loading)
                room.setRoomState(RoomState.Loading);
            if(progress >= 100)
                role.setRoleState(RoomRoleState.LoadComplete);//加载完成
            // 推送已经准备好的玩家
            JSONObject stateJson = new JSONObject();
            stateJson.put(Field.ROLE_STATE,state);
            stateJson.put(Field.PROGRESS,progress);
            stateJson.put(Field.POS,role.getRoomPos());
            stateJson.put(Field.ROLE_ID,role.getId());
            room.pushAll(Push.ROOM_ROLE_STATE, stateJson);

            //所有玩家都已经准备,开始推送加载
            if (room.isAllState(RoomRoleState.Ready))
            {
                room.setRoomState(RoomState.Ready);
                room.pushAll( Push.ROOM_LOAD_START, new JSONObject());
            }
            //所有玩家都已经加载完成，开始推送游戏开始
            if (room.isAllState(RoomRoleState.LoadComplete))
            {
                room.setRoomState(RoomState.PlayingGame);
                room.pushAll( Push.ROOM_GAME_START, new JSONObject());
            }
        } else
        {
            rspdMessage(session, ReturnCode.Error_room_not_found);
        }
    }
}
