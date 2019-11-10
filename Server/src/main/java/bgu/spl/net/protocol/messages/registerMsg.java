package bgu.spl.net.protocol.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.protocol.Client;
import bgu.spl.net.protocol.DataBase;

public class registerMsg extends absMessage{

    public registerMsg(String msg) {
        super(msg);
        this.opCode = 1;
    }

    public void procces(int connectionId, Connections connection, DataBase dataBase)
    {
       String[] splitedMsg = msg.split(" ");
        Client client = new Client(splitedMsg[0], splitedMsg[1], connectionId);
        if (dataBase.contiansClient(client.getName())) {
            errorMsg error = new errorMsg("Error 1");
            error.procces(connectionId, connection, dataBase);
        }
        else if (dataBase.getClients().containsKey(connectionId)){
            errorMsg error = new errorMsg("Error 1");
            error.procces(connectionId, connection, dataBase);
        }
        else {
            dataBase.addClient(client, connectionId);
            ackMsg ack = new ackMsg("ACK 1");
            ack.procces(connectionId, connection, dataBase);
    }
    }
}
