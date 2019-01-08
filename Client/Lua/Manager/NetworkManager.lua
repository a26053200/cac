---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zheng.
--- DateTime: 2018/7/9 23:35
--- 网络通信
---

---@class Game.Manager.NetworkManager : Betel.LuaMonoBehaviour
---@field public scene Game.Modules.World.Scenes.BaseScene
local LuaMonoBehaviour = require('Betel.LuaMonoBehaviour')
local NetworkManager = class("NetworkManager", LuaMonoBehaviour)
local json = require("cjson")
local NetworkListener = require("Game.Manager.NetworkListener")

function NetworkManager:Ctor()
    self.listenerList = List.New() ---@type Core.List
    self.listener = NetworkListener.New(true)
    self.listenerList:Add(self.listener)

    netMgr:SetLuaFun("OnReConnect", handler(self, self.OnReConnect))
    netMgr:SetLuaFun("OnHttpRspd", handler(self, self.OnHttpRspd))
    netMgr:SetLuaFun("OnJsonRspd", handler(self, self.OnJsonRspd))
end

function NetworkManager:Connect(host, port, onConnectSuccess, onConnectFail)
    netMgr:SetLuaFun("OnConnect", onConnectSuccess)
    netMgr:SetLuaFun("OnConnectFail", onConnectFail)
    netMgr:Connect(host, port)
end

function NetworkManager:OnConnectFail()
    print("OnConnectFail ")
end

function NetworkManager:OnReConnect(data)
    print("OnReConnect " .. json)
end

--添加监听器
function NetworkManager:AddListener(listener)
    if not self.listenerList:Contain(listener) then
        self.listenerList:Add(listener)
    end
end

--移除监听器
function NetworkManager:RemoveListener(listener)
    if self.listenerList:Contain(listener) then
        self.listenerList:Remove(listener)
    end
end

--添加推送监听
function NetworkManager:AddPush(action, callback)
    if callback ~= nil then
        self.listener:addPushCallback(action, callback)
    end
end

--添加Http请求
function NetworkManager:HttpRqst(url, data, callback, ...)
    if callback ~= nil then
        self.listener:addCallback(data.action, callback)
    end
    data.data = string.format(data.data, ...)
    local jsonStr = json.encode(data)
    print("[HttpRqst]" .. jsonStr)
    netMgr:HttpRequest(url, jsonStr)
end

--异步发送
function NetworkManager:Send(json)
    netMgr:SendJson(json)
end

--同步请求
function NetworkManager:Request(data, callback, ...)
    if callback ~= nil then
        self.listener:addCallback(data.action, callback)
    end
    data.data = string.format(data.data, ...)
    local jsonStr = json.encode(data)
    --print("[Send]" .. jsonStr)
    netMgr:SendJson(jsonStr)
end

function NetworkManager:OnHttpRspd(jsonStr)
    local jsonData = json.decode(jsonStr)
    for i = 1, self.listenerList:Size() do
        self.listenerList[i]:handlerRqstCallback(jsonData.action, jsonData)
    end
end

function NetworkManager:OnJsonRspd(jsonStr)
    local jsonData = json.decode(jsonStr)
    --print("OnJsonRspd " .. jsonStr)
    for i = 1, self.listenerList:Size() do
        if string.find(jsonData.action,"push@") ~= nil then
            self.listenerList[i]:handlerPushCallback(jsonData.action, jsonData.data)
        else
            self.listenerList[i]:handlerRqstCallback(jsonData.action, jsonData.data)
        end
    end
end


return NetworkManager