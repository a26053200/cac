---
--- Generated by Tools
--- Created by zhengnan.
--- DateTime: 2018-06-19-16:04:56
---



---@class Game.Modules.Login.Service.LoginService : Game.Core.Ioc.BaseService
local BaseService = require("Game.Core.Ioc.BaseService")
local LoginService = class("LoginService", BaseService)
local Url = "http://127.0.0.1:8081"      --本地测试服
--local Url = "http://118.31.3.216:8081"    --阿里云服务器

function LoginService:Ctor()
    nmgr:AddPush(LoginAction.PlayerInfo, handler(self,self.OnPlayerInfo))
end

function LoginService:HttpRegister(username, password, callback)
    nmgr:HttpRqst(Url, LoginAction.LoginRegister, {username, password},function(data)
        callback(data)
    end)
end

function LoginService:HttpLogin(username, password, callback)
    nmgr:HttpRqst(Url, LoginAction.LoginAccount, {username, password},function(data)
        self.loginModel.serverList = data.srvList.list
        self.loginModel.aid = data.aid
        self.loginModel.token = data.token
        callback(data)
    end)
end

function LoginService:LoginGameServer(aid, token, callback)
    nmgr:Request(LoginAction.LoginGameServer, function(data)
        --self.loginModel.serverList = data.srvList.list
        callback(data)
    end, aid, token)
end

function LoginService:FetchRandomName(callback)
    nmgr:Request(LoginAction.FetchRandomName, function(data)
        callback(data)
    end)
end

function LoginService:CreateRole(roleName, callback)
    nmgr:Request(LoginAction.CreateRole, function(data)
        callback(data)
    end,roleName)
end

function LoginService:SelectRoleEnterGame(roleIndex, callback)
    nmgr:Request(LoginAction.SelectRoleEnterGame, function(data)
        callback(data)
    end,roleIndex)
end

---push
function LoginService:OnPlayerInfo(response)
    self.loginModel.roleList = response.roleList
end

return LoginService
