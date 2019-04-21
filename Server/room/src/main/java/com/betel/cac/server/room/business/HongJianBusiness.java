package com.betel.cac.server.room.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.betel.asd.Business;
import com.betel.cac.beans.Role;
import com.betel.cac.beans.Room;
import com.betel.cac.core.consts.*;
import com.betel.cac.core.utils.Handler;
import com.betel.cac.server.room.beans.CardSlot;
import com.betel.cac.server.room.beans.HongJianDeck;
import com.betel.cac.server.room.beans.HongJianRoom;
import com.betel.session.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * @ClassName: HongJianBusiness
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2019/3/10 23:46
 */
public class HongJianBusiness extends Business<HongJianDeck>
{
    final static Logger logger = LogManager.getLogger(HongJianBusiness.class);

    private HashMap<Integer, HongJianRoom> roomMap;

    public HongJianBusiness()
    {
        super();
        roomMap = new HashMap<>();
    }


    @Override
    public void Handle(Session session, String method)
    {
        switch (method)
        {
            case Action.TURN_OPERATE:
                turnOperate(session);
                break;
            default:
                logger.error("Unknown action:" + method);
                break;
        }
    }


    public void startRound(Room normalRoom)
    {
        normalRoom.setRoomState(RoomState.PlayingGame);
        normalRoom.pushAll(Push.ROOM_GAME_START, new JSONObject());

        //发牌
        HongJianDeck deck = new HongJianDeck(normalRoom.getId());
        HongJianRoom room = new HongJianRoom(normalRoom, deck, 5000);
        roomMap.put(normalRoom.getId(), room);
        room.getDeck().NewDeck();//新牌
        room.getDeck().Shuffle();//洗牌
        //发牌给每个角色
        for (int i = 0; i < room.getRoom().getRoleList().length; i++)
        {
            Role role = room.getRoom().getRoleList()[i];
            if (role != null)
            {
                //CardSlot slot = deck.Deal(i, i == 0 ? 52 : 0);
                CardSlot slot = deck.Deal(i, 13);
                JSONObject cardJson = new JSONObject();
                cardJson.put(Field.CLIENT_ROLE_ID, role.getId());
                cardJson.put(Field.CARD_SLOT, slot.toJsonArray());
                cardJson.put(Field.ROOM_POS, role.getRoomPos());
                monitor.pushToClient(role.getChannelId(), ServerName.JSON_GATE_SERVER, Push.HJ_CARD_SLOT, cardJson);
            }
        }

        room.startTurn();
        sendWhoseTurn(room);
    }

    private void sendWhoseTurn(HongJianRoom room)
    {
        JSONObject turnJson = new JSONObject();
        turnJson.put(Field.TURN_DURATION,30);
        turnJson.put(Field.ROOM_POS,room.getCurrTurnPos());
        turnJson.put(Field.TURN_NUM,room.getCurrTurnNum());//当前轮数,首轮必须要出牌
        room.getRoom().pushAll(Push.HJ_WHOSE_TURN, turnJson);
        monitor.pushToClient(roomRole.getChannelId(), ServerName.JSON_GATE_SERVER, Push.ROOM_INFO, roomJson);
        room.nextTurn(new Handler()
        {
            @Override
            public void call()
            {
                sendWhoseTurn(room);
            }
        });
    }

    private void turnOperate(Session session)
    {
        int roomId = session.getRecvJson().getIntValue(Field.ROOM_ID);
        HongJianRoom room = roomMap.get(roomId);
        if (room != null)
        {
            room.stopTurnTimer();
        }
    }
}
