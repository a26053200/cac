---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zheng.
--- DateTime: 2018/6/30 0:02
--- 场景的基类
---

---@class Game.Modules.World.Scenes.BaseScene : Betel.LuaMonoBehaviour
local LuaMonoBehaviour = require('Betel.LuaMonoBehaviour')
local BaseScene = class("BaseScene",LuaMonoBehaviour)

function BaseScene:Ctor()
    LuaMonoBehaviour.Ctor(self)
    self:Init()
end

function BaseScene:Init()
    local prefab = Res.LoadPrefab("Prefabs/UI/Common/UICanvas.prefab")
    self.uiCanvas = GameObject.Instantiate(prefab)
    vmgr:SetScene(self)
end

function BaseScene:OnEnterScene()

end

return BaseScene