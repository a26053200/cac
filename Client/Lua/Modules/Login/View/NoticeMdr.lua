---
--- Generated by Tools
--- Created by zhengnan.
--- DateTime: 2018-07-13-17:00:35
---

---@class Game.Modules.Login.View.NoticeMdr : Game.Core.Ioc.BaseMediator
local BaseMediator = require("Game.Core.Ioc.BaseMediator")
local NoticeMdr = class("NoticeMdr",BaseMediator)

function NoticeMdr:OnInit()
    
end

function NoticeMdr:OnRemove()
    vmgr:LoadView(ViewConfig.ServerList)
end

function NoticeMdr:On_Click_BtnClose()
    vmgr:UnloadView(ViewConfig.Notice)
end

return NoticeMdr