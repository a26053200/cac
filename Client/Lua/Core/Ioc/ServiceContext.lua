---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zhengnan.
--- DateTime: 2018/6/12 10:37
---

local IocContext = require("Betel.Ioc.IocContext")
local ServiceContext = class("ServiceContext",IocContext)

function ServiceContext:Ctor(binder)
    self.binder = binder
end

function ServiceContext:Launch()
    --TODO
	self.binder:Bind(require("Game.Modules.HongJian.Service.HongJianService")):ToSingleton()
	self.binder:Bind(require("Game.Modules.Lobby.Service.LobbyService")):ToSingleton()
	self.binder:Bind(require("Game.Modules.Login.Service.LoginService")):ToSingleton()
	self.binder:Bind(require("Game.Modules.Newbie.Service.NewbieService")):ToSingleton()
	self.binder:Bind(require("Game.Modules.Robot.Service.RobotService")):ToSingleton()
	self.binder:Bind(require("Game.Modules.Role.Service.RoleService")):ToSingleton()
	self.binder:Bind(require("Game.Modules.Room.Service.RoomService")):ToSingleton()
    --TODO
end

return ServiceContext
