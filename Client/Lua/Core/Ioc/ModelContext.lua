---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zhengnan.
--- DateTime: 2018/6/12 10:37
---

local IocContext = require("Betel.Ioc.IocContext")
local ModelContext = class("ModelContext",IocContext)

function ModelContext:Ctor(binder)
    self.binder = binder
end

function ModelContext:Launch()
    --TODO
	self.binder:Bind(require("Game.Modules.HongJian.Model.HongJianModel")):ToSingleton()
	self.binder:Bind(require("Game.Modules.Lobby.Model.LobbyModel")):ToSingleton()
	self.binder:Bind(require("Game.Modules.Login.Model.LoginModel")):ToSingleton()
	self.binder:Bind(require("Game.Modules.Newbie.Model.NewbieModel")):ToSingleton()
	self.binder:Bind(require("Game.Modules.Robot.Model.RobotModel")):ToSingleton()
	self.binder:Bind(require("Game.Modules.Role.Model.RoleModel")):ToSingleton()
	self.binder:Bind(require("Game.Modules.Room.Model.RoomModel")):ToSingleton()
    --TODO
end

return ModelContext
