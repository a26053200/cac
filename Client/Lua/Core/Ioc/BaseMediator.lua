---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zheng.
--- DateTime: 2018/6/14 0:16
---

local NetworkListener = require("Betel.Net.NetworkListener")
local Tips = require("Game.Modules.Common.View.Tips")
local LuaMonoBehaviour = require('Betel.LuaMonoBehaviour')
---@class Game.Core.Ioc.BaseMediator : Betel.LuaMonoBehaviour
---@field public gameObject UnityEngine.GameObject
---@field public scene Game.Modules.World.Scenes.BaseScene
local BaseMediator = class("BaseMediator",LuaMonoBehaviour)

function BaseMediator:Ctor()
    LuaMonoBehaviour.super.Ctor(self)
    self.layer = UILayer.depth --默认在深度排序层级
    self.listener = NetworkListener.New()
    self.removeCallback = nil
end

function BaseMediator:Start()
    self:OnInit()
    self:OnAutoRegisterEvent()
    self:RegisterListeners()
    nmgr:AddListener(self.listener)
end

function BaseMediator:OnInit()

end

function BaseMediator:RegisterListeners()

end

function BaseMediator:AddPush(action, callback)
    if callback ~= nil then
        self.listener:addPushCallback(action, callback)
    end
end

--自动注册事件
function BaseMediator:OnAutoRegisterEvent()
    local buttons = LuaHelper.GetChildrenButtons(self.gameObject)
    for i = 0,buttons.Length - 1 do
        local funName = "On_Click_"..buttons[i].gameObject.name
        if self[funName] then
            log("Auto Register Event:" .. funName)
            LuaHelper.AddButtonClick(buttons[i].gameObject,handler(self,self[funName]))
        end
    end
end

function BaseMediator:RegisterClick(go, clickFun)
    LuaHelper.AddButtonClick(go,handler(self,clickFun))
end

function BaseMediator:ShowTips(msg)
    Tips.ShowTips(msg)
end

function BaseMediator:DoRemove(callback)
    self.removeCallback = callback
end

function BaseMediator:OnDestroy()
    print("OnDestroy view: "..self.viewInfo.name)
    nmgr:RemoveListener(self.listener)
    self:OnRemove()
    if self.removeCallback ~= nil then
        self.removeCallback(self)
    end
    self.viewInfo.status = ViewStatus.Unloaded
end

function BaseMediator:OnRemove()

end

return BaseMediator