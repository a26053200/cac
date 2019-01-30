---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zheng.
--- DateTime: 2019/1/28 0:04
---

local RectTransform     = "RectTransform"
local Image             = "Image"
local Button            = "Button"
local Toggle            = "Toggle"
local CanvasGroup       = "CanvasGroup"
local Slider            = "Slider"
local Text              = "Text"

---@param go UnityEngine.GameObject
---@return UnityEngine.RectTransform
function GetRectTransform(go)
    return go:GetCom(RectTransform)
end


---@param go UnityEngine.GameObject
---@return UnityEngine.UI.Text
function GetText(go)
    return go:GetCom(Text)
end


---@param go UnityEngine.GameObject
---@return UnityEngine.UI.Image
function GetImage(go)
    return go:GetCom(Image)
end


---@param go UnityEngine.GameObject
---@return UnityEngine.UI.Button
function GetButton(go)
    return go:GetCom(Button)
end

---@param go UnityEngine.GameObject
---@return UnityEngine.UI.Toggle
function GetToggle(go)
    return go:GetCom(Toggle)
end

---@param go UnityEngine.GameObject
---@return UnityEngine.CanvasGroup
function GetCanvasGroup(go)
    return go:GetCom(CanvasGroup)
end

---@param go UnityEngine.GameObject
---@return UnityEngine.UI.Slider
function GetSlider(go)
    return go:GetCom(Slider)
end
