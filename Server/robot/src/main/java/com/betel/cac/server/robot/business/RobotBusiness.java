package com.betel.cac.server.robot.business;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.betel.asd.Business;
import com.betel.cac.core.consts.Action;
import com.betel.cac.core.consts.Field;
import com.betel.cac.core.consts.RoomRoleState;
import com.betel.cac.core.consts.ServerName;
import com.betel.cac.core.utils.MathUtils;
import com.betel.cac.server.robot.beans.Robot;
import com.betel.consts.FieldName;
import com.betel.session.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class RobotBusiness extends Business<Robot>
{


    final static Logger logger = LogManager.getLogger(RobotBusiness.class);

    private HashMap<String, Robot> robotMap;
    //机器人计数器
    private int robotCounter = 1;

    public RobotBusiness()
    {
        super();
        robotMap = new HashMap<>();
    }
    @Override
    public void Handle(Session session, String method)
    {
        switch (method)
        {
            case Action.ROBOT_ENTER_ROOM:
                robotEnterRoom(session);
                break;
            default:
                logger.error("Unknown action:" + method);
                break;
        }
    }



    private Robot createRobotRole()
    {
        Robot robot = new Robot();
        robot.setRoleState(RoomRoleState.Ready);
        robot.setRoleId("robot_" + robotCounter++);
        robot.setRoleName(robot.getRoleId());
        robot.setSex(MathUtils.randomInt(0,2));
        robot.setHeadIcon(MathUtils.randomInt(1,6));
        robot.setRobot(true);
        robotMap.put(robot.getRoleId(),robot);
        return robot;
    }

    //通知机器人进入房间
    private void robotEnterRoom(Session session)
    {
        int roomId = session.getRecvJson().getInteger(FieldName.ID);
        JSONArray array = session.getRecvJson().getJSONArray(Field.ROBOT_INFO);
        for (int i = 0; i < array.size(); i++)
        {
            Robot robot = createRobotRole();
            JSONObject sendJson = robot.toJson();
            sendJson.put(Field.ROOM_ID,roomId);
            sendJson.put(FieldName.CHANNEL_ID, session.getChannelId());
            sendJson.put(Field.POS,array.getJSONObject(i).getInteger(Field.POS));
            //机器人进入房间
            monitor.sendToServer(ServerName.ROOM_SERVER,"room@" + Action.ENTER_ROOM,sendJson);
        }
    }
}
