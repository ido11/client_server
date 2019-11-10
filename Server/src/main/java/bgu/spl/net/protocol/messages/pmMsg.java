package bgu.spl.net.protocol.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.protocol.DataBase;

public class pmMsg extends absMessage {
    public pmMsg(String msg) {
        super(msg);
        this.opCode =6;
    }

    public void procces(int connectionId, Connections connection, DataBase dataBase)
    {
        if (!dataBase.getClient(connectionId).isConnected()){
            errorMsg notConn = new errorMsg("Error 6");
            notConn.procces(connectionId, connection, dataBase);
            return;
        }
        String[] spiltedMsg = msg.split(" ");
        if (!dataBase.contiansClient(spiltedMsg[0])){
            errorMsg notConn = new errorMsg("Error 6");
            notConn.procces(connectionId, connection, dataBase);
            return;
        }
        String contant ="";
        for (int i=1 ; i<spiltedMsg.length; i++){
            contant = contant + spiltedMsg[i] + " ";
        }
        notificationMsg noti = new notificationMsg("NOTIFICATION " + "0 " + dataBase.getClient(connectionId).getName() + " "  + contant);
        if (dataBase.getClient(spiltedMsg[0]).isConnected()){
            noti.procces(dataBase.getClient(spiltedMsg[0]).getId(), connection, dataBase);
        }
        else{
            dataBase.getClient(spiltedMsg[0]).addWaitingMsg(noti);
        }
        ackMsg ack = new ackMsg("ACK 6");
        ack.procces(connectionId, connection, dataBase);
        dataBase.addToDataMsg(this);
    }
}
