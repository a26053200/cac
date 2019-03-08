---
--- Generated by Tools
--- Created by zheng.
--- DateTime: 2019-03-05-22:38:23
---

local BaseMediator = require("Game.Core.Ioc.BaseMediator")
local RoomVo = require("Game.Modules.Room.Vo.RoomVo")
---@class Game.Modules.Robot.View.RobotCardMdr : Game.Core.Ioc.BaseMediator
---@field robotModel Game.Modules.Robot.Model.RobotModel
---@field robotService Game.Modules.Robot.Service.RobotService
local RobotCardMdr = class("RobotCardMdr",BaseMediator)

function RobotCardMdr:OnInit()
    --红尖游戏 需要3个机器人
    if self.roomModel.room.game == GameName.Hong_Jian then
        self:StartCoroutine(function ()
            for i = 1, 3 do
                local robotClient = self.robotModel:CreateRobotClient()
                robotClient.roomRole.roomPos = i
                self.robotService:EnterRoom(robotClient.roomRole.id, i)--服务器下标从0开始
                coroutine.step(1)
            end
        end)
    end
end

function RobotCardMdr:RegisterListeners()
    self:AddPush(Action.PushRoomInfo, handler(self,self.OnPushRoomInfo))
    self:AddPush(Action.PushExitRoom, handler(self,self.OnPushExitRoom))
    self:AddPush(Action.PushRoomRoleState, handler(self,self.OnPushRoomRoleState))
    self:AddPush(Action.PushRoomLoadStart, handler(self,self.OnPushRoomLoadStart))
    self:AddPush(Action.PushRoomGameStart, handler(self,self.OnPushRoomGameStart))
end

function RobotCardMdr:OnPushRoomInfo(response)
    local robotClient = self.robotModel.robotClientMap[response.data.clientRoleId]
    if robotClient then
        robotClient.room = RoomVo.New()
        robotClient.room:FromJson(response.data)
        robotClient.room.roleList[1] = robotClient.roomRole
    end
end

function RobotCardMdr:OnPushExitRoom(response)
    self.robotModel.robotClientMap = {}
end

function RobotCardMdr:OnPushRoomRoleState(response)
    --判断这条指令是发给真实玩家的,机器人根据真实玩家的消息来做决策该做什么
    if self.roleModel.roleId == response.data.roleId and self.roleModel.roleId == response.data.clientRoleId then
        if response.data.roleState == RoomRoleState.Ready then
            self:ForEachClientDo(function (client)
                self.robotService:ChangeState(client.roomRole.id, RoomRoleState.Ready)
            end)
        elseif response.data.roleState == RoomRoleState.LoadComplete then
            self:ForEachClientDo(function (client)
                self.robotService:ChangeState(client.roomRole.id, RoomRoleState.LoadComplete)
            end)
        end
    end
end

function RobotCardMdr:OnPushRoomLoadStart(response)
    --self.roomModel:UpdateState(response.data.pos + 1,response.data.roleState)
end

function RobotCardMdr:OnPushRoomGameStart(response)
    --self.roomModel:UpdateState(response.data.pos + 1,response.data.roleState)
end

function RobotCardMdr:ForEachClientDo(doFun)
    for i, robotClient in pairs(self.robotModel.robotClientMap) do
        if doFun then
            doFun(robotClient)
        end
    end
end

return RobotCardMdr