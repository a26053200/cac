package com.betel.cac.server.robot;

import com.betel.config.ServerConfigVo;
import com.betel.servers.node.NodeServerMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RobotMonitor extends NodeServerMonitor
{
    final static Logger logger = LogManager.getLogger(RobotMonitor.class);

    public RobotMonitor(ServerConfigVo serverCfgInfo)
    {
        super(serverCfgInfo);
    }
}
