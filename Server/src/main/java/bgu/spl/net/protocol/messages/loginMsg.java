package bgu.spl.net.protocol.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.protocol.Client;
import bgu.spl.net.protocol.DataBase;

import java.util.HashMap;

public class loginMsg extends absMessage{

    public loginMsg(String msg) {
        super(msg);
        this.opCode =2;
    }

    public  void procces(int connectionId, Connections connection, DataBase dataBase){
        String[] splitedMsg = msg.split(" ");
        if (!(dataBase.contiansClient(splitedMsg[0]))) {
            errorMsg notExist = new errorMsg("Error 2");
            notExist.procces(connectionId, connection, dataBase);
        }
        else if (!(dataBase.isPassMatch(splitedMsg[0], splitedMsg[1]))) {
            errorMsg incpass = new errorMsg("Error 2");
            incpass.procces(connectionId, connection, dataBase);
        }
        else if (dataBase.getClient(splitedMsg[0]).isConnected()) {
            errorMsg connected = new errorMsg("Error 2");
            connected.procces(connectionId, connection, dataBase);
        }
        else if (dataBase.getClients().containsKey(connectionId) && dataBase.getClient(connectionId).isConnected()){
            errorMsg connected = new errorMsg("Error 2");
            connected.procces(connectionId, connection, dataBase);
        }
        else {
            synchronized (dataBase.getClient(splitedMsg[0])) {

                dataBase.getClient(splitedMsg[0]).setConnected(true);
                ackMsg ack = new ackMsg("ACK 2");
                ack.procces(connectionId, connection, dataBase);
                for (absMessage msg : dataBase.getClient(splitedMsg[0]).getWaitingMsgs()) {
                    msg.procces(connectionId, connection, dataBase);
                }
                dataBase.getClient(splitedMsg[0]).clearWaitingMsgs();

                for (Integer key : dataBase.getClients().keySet()) {
                    if (dataBase.getClients().get(key).getName().equals(splitedMsg[0])) {
                        if (!(key == connectionId)) {
                            dataBase.getClients().remove(key);
                            dataBase.getClients().put(connectionId, dataBase.getClient(splitedMsg[0]));
                            dataBase.getClients().get(connectionId).setId(connectionId);
                        }
                    }
                }
            }

        }
    }
}
