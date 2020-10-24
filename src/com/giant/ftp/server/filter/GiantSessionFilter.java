package com.giant.ftp.server.filter;

import org.apache.ftpserver.ipfilter.SessionFilter;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 会话过滤器
 * 自定义的会话过滤器用于拦截黑名单
 */
public class GiantSessionFilter implements SessionFilter {
    private static Logger logger = Logger.getLogger(GiantSessionFilter.class);
    private Set<String> blackHostSet = new HashSet<>();

    @Override
    public boolean accept(IoSession ioSession) {
        // 获取数据
        InetSocketAddress address = (InetSocketAddress) ioSession.getRemoteAddress();
        String hostName = address.getAddress().getHostName();
        String ipAddress = address.getAddress().getHostAddress();
        // 过滤黑名单
        if (blackHostSet.contains(ipAddress) || blackHostSet.contains(hostName)) {
            logger.debug("filter black " + ipAddress + " or " + hostName);
            return false;
        }
        return true;
    }

    public void addBlackHost(String hostName) {
        blackHostSet.add(hostName);
    }

    public void removeBlackHost(String hostName) {
        blackHostSet.remove(hostName);
    }

    public void addAll(Collection<String> blacks) {
        blackHostSet.addAll(blacks);
    }
}
