---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zheng.
--- DateTime: 2018/8/14 0:03
---

---@class Game.Modules.World.Scenes.GuideNewbieScene : Game.Modules.World.Scenes.BaseScene
local BaseScene = require('Game.Modules.World.Scenes.BaseScene')
local GuideNewbieScene = class("GuideNewbieScene",BaseScene)

function GuideNewbieScene:Ctor()
    BaseScene.Ctor(self)
end

function GuideNewbieScene:OnEnterScene()
    vmgr:LoadView(ViewConfig.RoleInfo)
    vmgr:LoadView(ViewConfig.NewbieWelcome)
end

return GuideNewbieScene