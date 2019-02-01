package com.betel.cac.server.room.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.betel.asd.Business;
import com.betel.cac.core.consts.Action;
import com.betel.cac.core.consts.ServerName;
import com.betel.cac.server.room.beans.Room;
import com.betel.cac.server.room.beans.RoomRole;
import com.betel.session.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;

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
        static final String Game = "game";
        static final String GameMode = "gameMode";
        static final String RoleList = "roleList";
        static final String RoomInfo = "roomInfo";
    }

    final static Logger logger = LogManager.getLogger(RoomBusiness.class);

    private int roomCounter = 1;

    private LinkedList<Room> roomList;

    public RoomBusiness()
    {
        super();
        roomList = new LinkedList<>();
    }

    @Override
    public void Handle(Session session, String method)
    {
        switch (method)
        {
            case Action.ROOM_CREATE:
                createRoom(session);
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

        JSONObject sendJson = room.toJson();

        for (int i = 0; i < room.getRoleList().size(); i++)
        {
            RoomRole role = room.getRoleList().get(i);
            if (role.getChannelId() != null)
            {
                monitor.pushToClient(role.getChannelId(), ServerName.JSON_GATE_SERVER, "push@" + Action.ROOM_CREATE, sendJson);
            }
        }
    }
}
