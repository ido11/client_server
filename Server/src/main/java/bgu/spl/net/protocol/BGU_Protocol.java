package bgu.spl.net.protocol;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.protocol.messages.*;

public class BGU_Protocol implements BidiMessagingProtocol {

    private boolean shouldTerminate =false;
    private int connectionId;
    private Connections connections;
    private DataBase dataBase;


    public BGU_Protocol(DataBase dataBase){
        this.dataBase = dataBase;
    }

    public void start(int connectionId, Connections connections) {
       this.connectionId = connectionId;
       this.connections = connections;
    }

    @Override
    public void process(Object message)
    {
        ((absMessage)message).procces(connectionId,connections, dataBase);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
