package com.betel.cac.server.room.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.betel.asd.Business;
import com.betel.cac.beans.Role;
import com.betel.cac.beans.Room;
import com.betel.cac.core.consts.*;
import com.betel.cac.server.room.beans.CardSlot;
import com.betel.cac.server.room.beans.HongJianDeck;
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

    private HashMap<Integer, HongJianDeck> cardDeckMap;

    public HongJianBusiness()
    {
        super();
        cardDeckMap = new HashMap<>();
    }


    //游戏开具
    public void startRound(Room room)
    {
        room.setRoomState(RoomState.PlayingGame);
        room.pushAll(Push.ROOM_GAME_START, new JSONObject());

        //发牌
        HongJianDeck deck = new HongJianDeck(room.getId());
        cardDeckMap.put(room.getId(), deck);
        deck.NewDeck();//新牌
        deck.Shuffle();//洗牌
        //发牌给每个角色
        for (int i = 0; i < room.getRoleList().length; i++)
        {
            Role role = room.getRoleList()[i];
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
        JSONObject turnJson = new JSONObject();
        turnJson.put(Field.TURN_DURATION,30);
        turnJson.put(Field.ROLE_ID,1);
        turnJson.put(Field.ROLE_ID,1);
        room.pushAll(Push.HJ_WHOSE_TURN, new JSONObject());
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

    private void turnOperate(Session session)
    {

    }
}
