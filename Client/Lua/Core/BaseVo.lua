---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zhengnan.
--- DateTime: 2018/6/19 18:18
---


local LuaObject = require('Betel.LuaObject')
---@class Game.Core.BaseVo : Betel.LuaObject
local BaseVo = class("BaseVo",LuaObject)

function BaseVo:Ctor()

end

function BaseVo:FromJson(json)
    for k,v in pairs(json) do
        self[k] = v
    end
end
return BaseVo