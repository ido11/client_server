package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoderImp;
import bgu.spl.net.protocol.BGU_Protocol;
import bgu.spl.net.protocol.DataBase;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase(); //one shared object
        int port = Integer.parseInt(args[0]);

        Server.threadPerClient(
                port, //port
                () ->  new BGU_Protocol(dataBase), //protocol factory
                MessageEncoderDecoderImp::new //message encoder decoder factory
        ).serve();

    }
}
