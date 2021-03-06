---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zheng.
--- DateTime: 2018/6/29 23:58
---

function World.EnterScene(sceneInfo, callback)
    World.ins:EnterScene(sceneInfo, callback)
end

local BaseMediator = require("Game.Core.Ioc.BaseMediator")
---@class Game.Modules.Notice.View.WorldMdr : Game.Core.Ioc.BaseMediator
---@field public currScene Game.Modules.World.Scenes.BaseScene
local WorldMdr = class("WorldMdr",BaseMediator)

function WorldMdr:Ctor()
    BaseMediator.Ctor(self)
    self.tempLevel = nil;
    self.currLevel = "";
    self.currScene = nil;
    self.nextScene = nil;

    World.ins = self
end

function WorldMdr:OnInit()
    World.EnterScene(WorldConfig.Login)
end

function WorldMdr:GetTempLevel()
    if self.tempLevel == WorldConfig.TempA then
        return WorldConfig.TempB.level
    else
        return WorldConfig.TempA.level
    end
end

function WorldMdr:EnterScene(sceneInfo, callback)
    if string.isValid(sceneInfo.level) then
        if self.currScene then
            self.currScene:OnExitScene()
        end
        log("Will enter "..sceneInfo.level)
        if self.currLevel == sceneInfo.level then
            logError("you can not load then same scene - " .. sceneInfo.level)
        elseif sceneInfo.level == "Temp" then
            self.tempLevel = self:GetTempLevel()
            self:LoadLevel(self.tempLevel, sceneInfo, callback)
        elseif sceneInfo.needLoading then
            self.nextScene = sceneInfo
            self:EnterScene(WorldConfig.Loading)
        else
            self:LoadLevel(sceneInfo.level,sceneInfo, callback)
        end
    end
end

function WorldMdr:EnterNextScene()
    self.nextScene.needLoading = false --临时关闭加载需求
    self:EnterScene(self.nextScene,function ()
        self.nextScene.needLoading = true
        self.nextScene = nil
    end)
end

function WorldMdr:LoadLevel(level,sceneInfo, callback)
    sceneMgr:LoadSceneAsync(level, function (unityScene)
        local sceneType = require(string.format("Game.Modules.World.Scenes.%sScene",sceneInfo.sceneName,sceneInfo.sceneName))
        if sceneType == nil then
            logError("can not find scene "..sceneInfo.sceneName)
            logStack()
            return
        end
        local scene = sceneType.New()
        scene.unityScene = unityScene
        log("进入场景:"..sceneInfo.debugName)
        self.currScene = scene
        self.currLevel = level
        scene:OnEnterScene()
        if callback ~= nil then
            callback()
        end
    end)
end

return WorldMdr