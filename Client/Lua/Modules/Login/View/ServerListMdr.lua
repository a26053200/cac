---
--- Generated by Tools
--- Created by zheng.
--- DateTime: 2018-07-13-00:00:21
---

---@class Game.Modules.Login.View.ServerListMdr : Game.Core.Ioc.BaseMediator
local BaseMediator = require("Game.Core.Ioc.BaseMediator")
local ServerListMdr = class("ServerListMdr",BaseMediator)

function ServerListMdr:OnInit()
    self:InitSrvList()
end

function ServerListMdr:RegisterListeners()
    self:AddPush(LoginAction.PlayerInfo, handler(self,self.onPlayerInfo));
end

function ServerListMdr:InitSrvList()
    self.srvList = UITools.CreateVScrollList(self.gameObject,"ScrollList",
            function(index, item)
                local server = self.loginModel.serverList[(index + 1) % 2 + 1 ]
                local itemObj = item.gameObject
                itemObj:SetText("Text", (index + 1).."服 " .. server.name)
                itemObj:SetText("Toggle/Label", server.host..":"..server.port)
                self:RegisterClick(itemObj:FindChild("Button"),function ()
                    nmgr:Connect(server.host, tonumber(server.port),
                            handler(self,self.onConnectSuccess),
                            handler(self,self.onConnectFail))
                end)
            end
    )
    self.srvList.ChildCount = 10 * #self.loginModel.serverList
end

function ServerListMdr:onConnectSuccess()
    print("onConnectSuccess")
    self.loginService:LoginGameServer(self.loginModel.aid, self.loginModel.token, handler(self,self.onLoginGameSuccess))
end

function ServerListMdr:onConnectFail()
    print("onConnectFail")
end

function ServerListMdr:onLoginGameSuccess(data)
    print("onLoginGameSuccess")
end

function ServerListMdr:onPlayerInfo(data)
    vmgr:UnloadView(ViewConfig.ServerList)
    if #data.roleList == 0 then
        vmgr:LoadView(ViewConfig.RoleCreate)--创建角色
    else
        vmgr:LoadView(ViewConfig.RoleSelect)--选择角色进入游戏
    end
end

return ServerListMdr
