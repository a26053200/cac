---
--- Generated by Tools
--- Created by zheng.
--- DateTime: 2018-07-22-22:17:24
---

local BaseMediator = require("Game.Core.Ioc.BaseMediator")
---@class Game.Modules.Login.View.RoleCreateMdr : Game.Core.Ioc.BaseMediator
---@field loginService Game.Modules.Login.Service.LoginService
---@field loginModel Game.Modules.Login.Model.LoginModel
local RoleCreateMdr = class("RoleCreateMdr",BaseMediator)

function RoleCreateMdr:OnInit()

end

function RoleCreateMdr:RegisterListeners()
    --self:AddPush(Action.PlayerInfo, handler(self,self.onPlayerInfo));
end

function RoleCreateMdr:On_Click_BtnRandom()
    self.loginService:FetchRandomName(handler(self,self.onFetchRandomName))
end

function RoleCreateMdr:On_Click_BtnBack()
    if #self.loginModel.serverList > 0 then
        vmgr:UnloadView(ViewConfig.RoleCreate)
        vmgr:LoadView(ViewConfig.RoleSelect)--选择角色进入游戏
    end
end

function RoleCreateMdr:On_Click_BtnCreate()
    local roleName = self.gameObject:GetText("H/InputField/Text").text
    if not string.isNullOrEmpty(roleName) then
        self.loginService:CreateRole(roleName, handler(self,self.onCreateRole))
    end
end

function RoleCreateMdr:onFetchRandomName(data)
    log("角色随机名字:"..data.roleName)
    self.gameObject:SetInputField("H/InputField", data.roleName)
end

function RoleCreateMdr:onCreateRole(data)
    log("角色创建成功:"..data.roleInfo.roleName)
    World.EnterScene(WorldConfig.Lobby)
    --World.EnterScene(WorldConfig.GuideScene)
end

function RoleCreateMdr:onPlayerInfo(data)
    vmgr:UnloadView(ViewConfig.RoleCreate)
    vmgr:LoadView(ViewConfig.RoleSelect)--选择角色进入游戏
end

return RoleCreateMdr
