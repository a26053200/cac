---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by zheng.
--- DateTime: 2019/3/14 23:59
--- 红尖牌型提取器
---

local CardGroup = require("Game.Config.Card.CardGroup")
local CardExtract = require('Game.Config.Card.Extracts.CardExtract')
---@class Game.Config.Card.Extracts.HongJianCardExtract : Game.Config.Card.Extracts.CardExtract
local HongJianCardExtract = class("Game.Config.Card.Extracts.HongJianCardExtract", CardExtract)

---@param cards table<number, Game.Config.Card.CardBaseVo>
---@param hasAlreadyTianZha boolean 是否已经出了最大的牌
function HongJianCardExtract:Ctor(cards, hasAlreadyShowMasterCard)
    self.hasAlreadyShowMasterCard = hasAlreadyShowMasterCard == nil and false or hasAlreadyShowMasterCard
    HongJianCardExtract.super.Ctor(self,cards)
end

---@return Game.Config.Card.CardGroup
function HongJianCardExtract:GetCardGroup()
    if self.group == nil then
        self.group = CardGroup.New(self.cards)
        local group = self.group

        if self:isStraight(1,5) then
            if self:isSameSuit() then
                --同花顺
                group:SetGroupType(HongJianCardGroupType.TongHuaShun, 1)
            else
                --单顺
                group:SetGroupType(HongJianCardGroupType.ShunZi, 1)
            end
            group.continuousNum = self.cardNum
        elseif self:isStraight(2, 2) then
            if self.cardNum / 2 >= 4 then --排炸
                group:SetGroupType(HongJianCardGroupType.PaiZha, 2)
            else--双顺(连对)
                group:SetGroupType(HongJianCardGroupType.ShunZi, 2)
            end
            group.continuousNum = self.cardNum / 2
        elseif self:isTakeN(4,0) then
            --四炸(四带N,N=0)
            group.groupType = HongJianCardGroupType.SiZha
        elseif self:isTakeN(3,2) then
            --三带N
            group.groupType = HongJianCardGroupType.SanDaiN
        elseif self:isTakeN(4,3) then
            --四带N
            group.groupType = HongJianCardGroupType.SiDaiN
        elseif self:isDouble() then
            if self:isRedColor(self.cards) then
                group.groupType = HongJianCardGroupType.TianZha--天炸
            elseif self.hasAlreadyShowMasterCard and self:isBlackColor(self.cards) then
                group.groupType = HongJianCardGroupType.DiZha --地炸
            else
                group.groupType = HongJianCardGroupType.DuiZi--对子
            end
        elseif self:isSingle() then
            --单张
            group.groupType = HongJianCardGroupType.Single
        elseif self:isFiveTenK() then
            --五十K
            if self:isSameSuit() then
                group.groupType = HongJianCardGroupType.WuShiK_Zheng
            else
                group.groupType = HongJianCardGroupType.WuShiK
            end
        else
            local isShunZi3, continuousNum = self:getMultiStraightInfo(3,2,4)
            if isShunZi3 then --三顺(飞机)
                group:SetGroupType(HongJianCardGroupType.ShunZi, 3)
                group.continuousNum = continuousNum
            else
                local isShunZi4, continuousNum = self:getMultiStraightInfo(4,2,6)
                if isShunZi4 then --四顺(大飞机)
                    group:SetGroupType(HongJianCardGroupType.ShunZi, 4)
                    group.continuousNum = continuousNum
                end
            end
        end
        group.weight = HongJianCardGroupWeight[group.groupType]
    end
    return self.group
end

--五十K
function HongJianCardExtract:isFiveTenK()
    local sortKeys = self:getHashSetSortByKey()
    if #sortKeys == 3 and sortKeys[1].fv == 5 and sortKeys[2].fv == 10 and sortKeys[3].fv == 13 then
        return true
    else
        return false
    end
end

--是否同黑色
---@param cards table<number, Game.Config.Card.CardBaseVo>
---@return boolean
function HongJianCardExtract:isBlackColor(cards)
    return (cards[1].suit == CardSuit.Spade and  cards[2].suit == CardSuit.Club) or
            (cards[1].suit == CardSuit.Club and  cards[2].suit == CardSuit.Spade)
end

--是否同红色
---@param cards table<number, Game.Config.Card.CardBaseVo>
---@return boolean
function HongJianCardExtract:isRedColor(cards)
    return (cards[1].suit == CardSuit.Heart and  cards[2].suit == CardSuit.Diamond) or
            (cards[1].suit == CardSuit.Diamond and  cards[2].suit == CardSuit.Heart)
end

return HongJianCardExtract
