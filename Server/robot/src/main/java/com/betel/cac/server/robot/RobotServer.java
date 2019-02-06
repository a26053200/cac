package com.betel.cac.server.robot;

import com.betel.cac.core.consts.ServerName;
import com.betel.cac.core.utils.ServerTools;
import com.betel.common.Monitor;
import com.betel.config.ServerConfigVo;
import com.betel.servers.forward.ServerClient;
import com.betel.servers.node.NodeServer;

public class RobotServer extends NodeServer
{
    public RobotServer(ServerConfigVo serverConfig, Monitor monitor)
    {
        super(serverConfig, monitor);
    }

    public static void main(String[] args) throws Exception
    {
        ServerConfigVo serverCfg = ServerTools.createServerConfig(args,ServerName.ROBOT_SERVER, ServerName.BALANCE_SERVER);
        RobotMonitor mnt = new RobotMonitor(serverCfg);
        RobotServer server = new RobotServer(serverCfg,mnt);
        server.setServerClient(new ServerClient(serverCfg, mnt));
        server.run();
    }
}
