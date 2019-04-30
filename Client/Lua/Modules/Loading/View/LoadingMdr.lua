---
--- Generated by Tools
--- Created by zheng.
--- DateTime: 2018-08-14-22:15:40
---


local BaseMediator = require("Game.Core.Ioc.BaseMediator")
---@class Game.Modules.Loading.View.LoadingMdr : Game.Core.Ioc.BaseMediator
local LoadingMdr = class("LoadingMdr",BaseMediator)

--测试用加载时间
local WaitTime = 1

function LoadingMdr:Ctor()
    LoadingMdr.super.Ctor(self)
    self.layer = UILayer.top
end

function LoadingMdr:OnInit()
    self.loadingBar = self.gameObject:GetSlider("LoadingBar")
    self:StartCoroutine(function ()
        local startTime = Time.time;
        while Time.time - startTime < WaitTime do
            coroutine.step(1)
            self.loadingBar.value = (Time.time - startTime) / WaitTime
        end
        World.ins:EnterNextScene()
        vmgr:UnloadView(ViewConfig.Loading)
    end)
end

return LoadingMdr
