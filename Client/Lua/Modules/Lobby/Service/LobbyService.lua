---
--- Generated by Tools
--- Created by zheng.
--- DateTime: 2019-01-28-23:25:17
---

local BaseService = require("Game.Core.Ioc.BaseService")
---@class Game.Modules.Lobby.Service.LobbyService : Game.Core.Ioc.BaseService
---@field lobbyModel Game.Modules.Lobby.Model.LobbyModel
local LobbyService = class("LobbyService",BaseService)

function LobbyService:Ctor()
    nmgr:AddPush(Action.RoomCreate, handler(self,self.OnRoomCreate))
end

function LobbyService:JoinMatch(game, gameMode, callback, failCallback)
    NetModal.Show()
    self:JsonRequest(Action.JoinMatch, {self.roleModel.roleId, game, gameMode }, function(data)
        NetModal.Hide()
        callback(data)
    end, function (data)
        NetModal.Hide()
        failCallback(data)
    end)
end

function LobbyService:OnRoomCreate(response)

end

return LobbyService
