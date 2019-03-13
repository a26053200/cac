---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zheng.
--- DateTime: 2019/3/12 0:05
---

local CardEvent = require("Game.Modules.Common.Events.CardEvent")
local LuaMonoBehaviour = require("Betel.LuaMonoBehaviour")
---@class Game.Modules.Common.View.Card : Betel.LuaMonoBehaviour
---@field public New function<obj:UnityEngine.GameObject,data:Game.Modules.Room.Vo.CardVo, selectable:boolean>
---@field public data Game.Modules.Room.Vo.CardVo
---@field public slot Game.Modules.Common.Utils.CardSlot
---@field public isSelected boolean
local Card = class("Card",LuaMonoBehaviour)

function Card:Ctor(gameObject, data, selectable)
    Card.super.Ctor(self, gameObject)
    self.data = data
    self.selectable = selectable

    self.gameObject:SetActive(true)
    self.transform.localPosition = Vector3.zero
    self.transform.localEulerAngles = Vector3.zero
    self.transform.localScale = Vector3.one

    self.front = self.gameObject:FindChild("CardFront")

    local mr = self.front:GetComponent(typeof(UnityEngine.MeshRenderer)) ---@type UnityEngine.MeshRenderer
    self.material = mr.material
    local suit = string.lower(self.data.suit)
    self.faceUrl = string.format("Atlas/Cards/%s_of_%ss.png",self.data.faceValue,suit)
    self.material:SetTexture("_MainTex", Res.LoadTexture(self.faceUrl))

    if self.selectable then
        LuaHelper.AddObjectClickEvent(self.gameObject, handler(self,self.OnClick))
    else
        local boxCollider = self.gameObject:GetComponent(typeof(UnityEngine.BoxCollider))
        destroy(boxCollider)
    end
    self.isSelected = false
end

function Card:UpdateOrg()
    self.orgPos = self.transform.localPosition
end

function Card:OnClick(event)
    print(self.faceUrl)
    self.isSelected = not self.isSelected
    self:UpdateSelected(event)
    edp:Dispatcher(CardEvent.Click, self)
end

function Card:UpdateSelected(event)
    if self.isSelected then
        self.transform:DOLocalMoveZ(self.orgPos.z + 0.035, FrameTime * 4)
        self.material:SetColor("_Color", Color.green)
    else
        self.transform:DOLocalMoveZ(self.orgPos.z, FrameTime * 4)
        self.material:SetColor("_Color", Color.white)
    end

end

return Card