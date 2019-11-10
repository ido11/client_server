package bgu.spl.net.api;

import bgu.spl.net.api.bidi.Connections;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.srv.bidi.ConnectionHandler;

public class ConnectionsImpl<T> implements Connections {

    private ConcurrentHashMap<Integer, ConnectionHandler> handlers;

    public ConnectionsImpl(){
        handlers = new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, Object msg)
    {
        try {
            handlers.get(connectionId).send(msg);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public void broadcast(Object msg) {

    }

    @Override
    public void disconnect(int connectionId) {
       handlers.remove(connectionId);
    }

    public void  connect(ConnectionHandler handler,int connectionId){
        handlers.putIfAbsent(connectionId, handler);
    }
}

