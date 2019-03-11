---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zhengnan.
--- DateTime: 2018/6/12 10:37
---

local IocContext = require("Betel.Ioc.IocContext")
local MediatorContext = class("MediatorContext",IocContext)

function MediatorContext:Ctor(binder)
    self.binder = binder
end

function MediatorContext:GetMediator(viewName)
    local mdrClass = self.binder.typeDict[viewName]
    if mdrClass == nil then
        logError("View:{0} mediator has not register",viewName)
    end
    return mdrClass
end

function MediatorContext:Launch()
    --TODO
	self.binder:Bind(require("Game.Modules.HongJian.View.HongJianSceneMdr")):To(ViewConfig.HongJianScene.name)
	self.binder:Bind(require("Game.Modules.Loading.View.LoadingMdr")):To(ViewConfig.Loading.name)
	self.binder:Bind(require("Game.Modules.Lobby.View.LobbyMainMenuMdr")):To(ViewConfig.LobbyMainMenu.name)
	self.binder:Bind(require("Game.Modules.Lobby.View.LobbySceneMdr")):To(ViewConfig.LobbyScene.name)
	self.binder:Bind(require("Game.Modules.Login.View.LoginMdr")):To(ViewConfig.Login.name)
	self.binder:Bind(require("Game.Modules.Login.View.LoginSceneMdr")):To(ViewConfig.LoginScene.name)
	self.binder:Bind(require("Game.Modules.Login.View.NoticeMdr")):To(ViewConfig.Notice.name)
	self.binder:Bind(require("Game.Modules.Login.View.RoleCreateMdr")):To(ViewConfig.RoleCreate.name)
	self.binder:Bind(require("Game.Modules.Login.View.RoleSelectMdr")):To(ViewConfig.RoleSelect.name)
	self.binder:Bind(require("Game.Modules.Login.View.ServerListMdr")):To(ViewConfig.ServerList.name)
	self.binder:Bind(require("Game.Modules.Newbie.View.NewbieWelcomeMdr")):To(ViewConfig.NewbieWelcome.name)
	self.binder:Bind(require("Game.Modules.Robot.View.RobotCardMdr")):To(ViewConfig.RobotCard.name)
	self.binder:Bind(require("Game.Modules.Role.View.RoleInfoMdr")):To(ViewConfig.RoleInfo.name)
	self.binder:Bind(require("Game.Modules.Room.View.Room_HJSceneMdr")):To(ViewConfig.Room_HJScene.name)
	self.binder:Bind(require("Game.Modules.Room.View.SampleRoomMdr")):To(ViewConfig.SampleRoom.name)
	self.binder:Bind(require("Game.Modules.World.View.WorldMdr")):To(ViewConfig.World.name)
    --TODO
end

return MediatorContext
