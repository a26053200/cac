---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zheng.
--- DateTime: 2019/4/21 23:35
--- 头像管理
---

---@class Game.Modules.Common.View.ImageHelper
---@field SetHeadIcon fun(spriteObject:UnityEngine.GameObject, headIcon:string)
local ImageHelper = {}

local headIconUrl = "Textures/HeadIcon/%s.jpg"

---@param spriteObject UnityEngine.GameObject
---@param headIcon string
function ImageHelper.SetHeadIcon(spriteObject, headIcon)
    spriteObject:GetImage().sprite = Res.LoadSprite(string.format(headIconUrl,tostring(headIcon)))
end

return ImageHelper