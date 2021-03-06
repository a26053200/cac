---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zheng.
--- DateTime: 2019/3/14 23:34
---


local BaseVo = require("Game.Core.BaseVo")
---@class Game.Config.Card.CardBaseVo : Game.Core.BaseVo
---@field faceValue number
---@field suit string
local CardBaseVo = class("Game.Config.Card.CardBaseVo",BaseVo)

function CardBaseVo:Ctor()

end

function CardBaseVo:ToString()
    return string.format("[%s:%s]",ChineseCardSuit[self.suit],self.faceValue)
end

return CardBaseVo