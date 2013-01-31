package com.cloudcontrolled.sample.spring.memcachier;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.CallbackHandler;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

public class MemcachierConnection extends MemcachedClient {

    public MemcachierConnection(String username, String password, String servers) throws IOException {
        this(new SASLConnectionFactoryBuilder().build(username, password), getAddresses(servers));
    }

    public MemcachierConnection(ConnectionFactory cf, List<InetSocketAddress> addrs) throws IOException {
        super(cf, addrs);
    }

    private static List<InetSocketAddress> getAddresses(String servers) {
        List<InetSocketAddress> addrList = new ArrayList<InetSocketAddress>();
        for (String server : servers.split(",")) {
            String addr = server.split(":")[0];
            int port = Integer.parseInt(server.split(":")[1]);
            addrList.add(new InetSocketAddress(addr, port));
        }
        return addrList;
    }
}

class SASLConnectionFactoryBuilder extends ConnectionFactoryBuilder {
    public ConnectionFactory build(String username, String password){
        CallbackHandler ch = new PlainCallbackHandler(username, password);
        AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, ch);
        this.setProtocol(Protocol.BINARY);
        this.setAuthDescriptor(ad);
        return this.build();
    }
}