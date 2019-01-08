---
--- Generated by Tools
--- Created by zheng.
--- DateTime: 2018-06-19-00:17:42
---

---@class Game.Modules.Login.View.LoginMdr : Game.Core.Ioc.BaseMediator
local BaseMediator = require("Game.Core.Ioc.BaseMediator")
local LoginMdr = class("LoginMdr",BaseMediator)

local USERNAME = "username"
local PASSWORD = "password"
local AID = "aid"
local TOKEN = "token"

function LoginMdr:OnInit()
    --vmgr:LoadView(ViewConfig.Notice)
    self.PlayerPrefs_Username = StringUtils.EncryptWithMD5(Application.dataPath .. USERNAME)
    self.PlayerPrefs_Password = StringUtils.EncryptWithMD5(Application.dataPath .. PASSWORD)

    self.username = PlayerPrefs.GetString(self.PlayerPrefs_Username, "")
    self.password = PlayerPrefs.GetString(self.PlayerPrefs_Password, "")

    self.gameObject:SetInputField("V/H1/InputField", self.username)
    self.gameObject:SetInputField("V/H2/InputField", self.password)
end

function LoginMdr:On_Click_BtnLogin()
    self.username = self.gameObject:GetText("V/H1/InputField/Text")
    self.password = self.gameObject:GetText("V/H2/InputField/Text")

    if string.isNullOrEmpty(self.username) or string.isNullOrEmpty(self.password) then
        print("Please input id and pw")
    else
        self.loginService:HttpLogin(self.username, self.password, handler(self,self.OnHttpLogin))
    end
end

function LoginMdr:OnHttpLogin(data)
    log("aid:{0} token:{1}", data.aid, data.token)
    PlayerPrefs.SetString(self.PlayerPrefs_Username, self.username)
    PlayerPrefs.SetString(self.PlayerPrefs_Password, self.password)

    vmgr:UnloadView(ViewConfig.Login)
    vmgr:LoadView(ViewConfig.Notice)
end

return LoginMdr