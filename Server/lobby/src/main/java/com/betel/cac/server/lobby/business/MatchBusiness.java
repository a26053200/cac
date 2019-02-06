package com.betel.cac.server.lobby.business;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.betel.asd.Business;
import com.betel.cac.core.consts.*;
import com.betel.cac.core.utils.Handler;
import com.betel.cac.core.utils.MathUtils;
import com.betel.cac.server.lobby.beans.Match;
import com.betel.cac.server.lobby.beans.Role;
import com.betel.consts.FieldName;
import com.betel.servers.forward.ForwardMonitor;
import com.betel.session.Session;
import com.betel.session.SessionState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;


/**
 * @ClassName: MatchBusiness
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2019/1/27 1:39
 */
public class MatchBusiness extends Business<Match>
{
    private class Field
    {
        static final String RoleId = "roleId";
        static final String Game = "game";
        static final String GameMode = "gameMode";
        static final String RoleList = "roleList";
    }
    final static Logger logger = LogManager.getLogger(MatchBusiness.class);

    private int matchCounter = 1;
    private int robotCounter = 1;

    private LinkedList<Match> matchQueue;

    public MatchBusiness() {
        super();
        matchQueue = new LinkedList<>();
    }

    @Override
    public void Handle(Session session, String method)
    {
        switch (method)
        {
            case Action.JOIN_MATCH:
                joinMatch(session);
                break;
            case Action.ROBOT_JOIN_MATCH:
                joinMatch(session);
                break;
            default:
                logger.error("Unknown action:" + method);
                break;
        }
    }

    private void joinMatch(Session session)
    {
        String channelId = session.getChannelId();
        String roleId = session.getRecvJson().getString(Field.RoleId);
        Game game = Game.valueOf(session.getRecvJson().getString(Field.Game));
        String gameMode = session.getRecvJson().getString(Field.GameMode);
        Match match = getBeanByChannelId(channelId);
        if(match == null)
        {
            match = new Match();
            match.setSession(session);
            match.setId(matchCounter++);
            match.setRoleId(roleId);
            match.setGame(game);
            match.setGameMode(gameMode);
            match.setTimeoutTime(3000);
            match.start(new Handler()
            {
                @Override
                public void call()
                {
                    Match match = getBeanByChannelId(channelId);
                    session.setState(SessionState.Fail);//匹配超时
                    rspdMessage(match.getSession(),ReturnCode.Error_match_timeout);
                    removeBean(channelId);
                    matchQueue.remove(match);//移出队列
                }
            });
            this.putBean(session.getChannelId(),match);
            addMatch(match);
            //加3个机器人
            robotJoinMatch("robot_" + robotCounter++ ,game,gameMode);
            robotJoinMatch("robot_" + robotCounter++ ,game,gameMode);
            robotJoinMatch("robot_" + robotCounter++ ,game,gameMode);
        }else{
            session.setState(SessionState.Fail);//匹配失败
            rspdMessage(session,ReturnCode.Error_already_matching);
        }
    }

    //机器人加入匹配
    private void robotJoinMatch(String robotId,Game game,String gameMode)
    {
        Match match = new Match();
        match.setId(matchCounter++);
        match.setRoleId(robotId);
        match.setRobot(true);
        match.setGame(game);
        match.setGameMode(gameMode);
        addMatch(match);
    }

    private void addMatch(Match newMatch)
    {
        matchQueue.add(newMatch);
        if(matchQueue.size() == 4)
        {//已经凑足一个牌桌了,转发给RoomServer创建房间,并返回给客户端,准备开始游戏

            //创建房间,第一个为房主
            JSONObject sendJson = new JSONObject();
            sendJson.put(FieldName.ACTION,"room@create_room");
            sendJson.put(Field.Game,matchQueue.get(0).getGame().toString());
            sendJson.put(Field.GameMode,matchQueue.get(0).getGameMode());
            JSONArray array = new JSONArray();
            for (int i = 0; i < matchQueue.size(); i++)
            {
                Match match = matchQueue.get(i);
                match.stop();//停止超时机制
                if (match.getSession() != null)
                {
                    removeBean(match.getSession().getChannelId());
                    rspdMessage(match.getSession(),ReturnCode.Match_success);
                }
                JSONObject roleJson;
                if (match.isRobot())
                {
                    roleJson = new JSONObject();
                    roleJson.put("id", match.getRoleId());
                    roleJson.put("roleName", match.getRoleId());
                    roleJson.put("sex", 0);
                    roleJson.put("headIcon", MathUtils.getRandom(6));
                    roleJson.put("isRobot",true);
                    array.add(i,roleJson);
                }else{
                    Role role = (Role) monitor.getAction(Bean.ROLE).getService().getEntryById(match.getRoleId());
                    roleJson = role.toJson();
                    roleJson.put("isRobot",false);
                    roleJson.put(FieldName.CHANNEL_ID,match.getSession().getChannelId());
                    array.add(i,roleJson);
                }
            }
            sendJson.put(Field.RoleList,array);

            //通知机器人服务器创建其他机器人玩家
            monitor.sendToServer(ServerName.ROBOT_SERVER,"robot@" + Action.ROBOT_CREATE_ROLE,sendJson);
            //通知房间服务器创建房间
            monitor.sendToServer(ServerName.ROOM_SERVER,"room@" + Action.ROOM_CREATE,sendJson);
            matchQueue.clear();//清除队列
        }else{

        }
    }
}
