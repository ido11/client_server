package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoderImp;
import bgu.spl.net.protocol.BGU_Protocol;
import bgu.spl.net.protocol.DataBase;
import bgu.spl.net.srv.Server;

public class ReactorMain {

    public static void main(String[] args) {
        DataBase dataBase = new DataBase(); //one shared object
        int port = Integer.parseInt(args[0]);
        int Threads = Integer.parseInt(args[1]);

        Server.reactor(
                Threads,
                port, //port
                () ->  new BGU_Protocol(dataBase), //protocol factory
                MessageEncoderDecoderImp::new //message encoder decoder factory
        ).serve();

    }
}
