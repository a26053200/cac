---
--- Generated by Tools
--- Created by zhengnan.
--- DateTime: 2018-06-19-16:04:54
---

---@class Game.Modules.Login.Model.LoginModel : Game.Core.Ioc.BaseModel
local BaseModel = require("Game.Core.Ioc.BaseModel")
local LoginModel = class("LoginModel",BaseModel)

function LoginModel:Ctor()
    self.serverList = nil
    self.aid = ""
    self.token = ""
    self.roleList = nil
end

return LoginModel