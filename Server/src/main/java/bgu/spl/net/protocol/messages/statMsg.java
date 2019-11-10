package bgu.spl.net.protocol.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.protocol.DataBase;

public class statMsg extends absMessage {
    public statMsg(String msg) {
        super(msg);
        this.opCode =8;
    }

    public void procces(int connectionId, Connections connection, DataBase dataBase)
    {
        if (!dataBase.getClient(connectionId).isConnected()){
            errorMsg notConn = new errorMsg("Error 8");
            notConn.procces(connectionId, connection, dataBase);
            return;
        }
        if (!dataBase.contiansClient(msg)){
            errorMsg notConn = new errorMsg("Error 8");
            notConn.procces(connectionId, connection, dataBase);
            return;
        }
        int numPosts = dataBase.getClient(msg).getPostedMsgs().size();
        int numFollowers = dataBase.getClient(msg).getFollowMe().size();
        int numFollowing = dataBase.getClient(msg).getIfollow().size();
        ackMsg ack = new ackMsg("ACK 8 " + numPosts + " " + numFollowers + " " + numFollowing);
        ack.procces(connectionId, connection, dataBase);
    }
}
