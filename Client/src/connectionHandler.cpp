#include <connectionHandler.h>
 
using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
 
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_){}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(std::string& line) {

    char mychar;
    char *chararray = &mychar;
    getBytes(&mychar,2);
    short opcode=bytesToShort(chararray);
    std::string toprint="";




    if(opcode==10) {//case ack

        char mychar2;
        char *chararray2 = &mychar2;
        getBytes(&mychar2,2);
        short ackType= bytesToShort(chararray2);

        toprint+="ACK ";

        switch (ackType){

            case 1: {
                toprint+="1";
                break;
            }

            case 2: {
                toprint+="2";
                break;
            }

            case 3: {
                toprint+="3";
                break;
            }

            case 4: {
                toprint+="4";
                char mychar13;
                char *chararray13 = &mychar13;
                getBytes(&mychar13,1);
                short tmp = std::stoi(chararray13);
                //short numOfUsers= bytesToShort(chararray13);
                toprint+= " " + std::to_string(tmp);
                for(int i=0;i<tmp;i++)
                {
                     std::string user = " ";
                    getFrameAscii(user,'\0');
                    toprint+=user.substr(0,user.length()-1);
                }
                break;

            }
            case  5 : {
                toprint+="5";
                break;
            }

            case  6 : {
                toprint+="6";
                break;
            }

            case  7 : {
                toprint+="7 ";
                char mychar3;
                char *chararray3 = &mychar3;
                getBytes(&mychar3,1);
                short numOfUsers= std::stoi(chararray3);
                toprint+=std::to_string(numOfUsers) + " ";
                for(int i=0;i<numOfUsers;i++)
                {
                    std::string user;
                    getFrameAscii(user,'\0');
                    toprint+=user.substr(0, user.length()-1) + " ";
                }
                break;
            }

            case 8 : {
                toprint+="8 ";
              //  char mychar3;
              //  char *chararray3 = &mychar3;
              //  getBytes(&mychar3,1);
              //  short ackType= std::stoi(chararray3);
              //  toprint+=std::to_string(ackType)+" ";
                char mychar4;
                char *chararray4 = &mychar4;
                getBytes(&mychar4,1);
                short numPosts= std::stoi(chararray4);
                toprint+=std::to_string(numPosts)+" ";
                char mychar5;
                char *chararray5 = &mychar5;
                getBytes(&mychar5,1);
                short NumFollowers= std::stoi(chararray5);
                toprint+=std::to_string(NumFollowers)+" ";
                char mychar6;
                char *chararray6 = &mychar6;
                getBytes(&mychar6,1);
                short NumFollowing= std::stoi(chararray6);
                toprint+=std::to_string(NumFollowing);
                break;

            }
        }
        
    }

    if(opcode==9){
       toprint+="NOTIFICATION ";
        char mychar1;
        char *chararray1 = &mychar1;
        getBytes(&mychar1,1);
        short PublicPM= std::stoi(chararray1);  //maybe bug bug because char and not short ..
        if(PublicPM==0){toprint+="PM ";}
        else{toprint+="PUBLIC ";}
        std::string user;
        getFrameAscii(user,'\0');
        toprint+=user.substr(0,user.length()-1)+" ";
        std::string contant;
        getFrameAscii(contant,'\0');
        toprint+=contant.substr(0,contant.length()-1) + " ";
        contant = "";
        bool done = false;
        while (!done) {
            getFrameAscii(contant, '\0');
           // std::cout << contant << "       zzzz"<< std::endl << std::endl;
            if (contant.substr(0,contant.length()-1).empty()){
                    done = true;
                }
            else {
                toprint+=contant.substr(0,contant.length()-1) +" ";
                contant = "";
            }
        }
    }


    if(opcode==11){
       toprint+="ERROR ";
        char mychar1;
        char *chararray1 = &mychar1;
        getBytes(&mychar1,1);
        short errorCode= std::stoi(chararray1);
        toprint+=std::to_string(errorCode);
    }

    line = toprint;
}

bool ConnectionHandler::sendLine(std::string& line) {

    std::stringstream ss(line);
    std::string item;
    std::vector<std::string> splittedStrings;
    while (std::getline(ss, item, ' ')) {
        splittedStrings.push_back(item);
    }
    char commandByte[2];
    short commandShort = command2short(splittedStrings[0]);
    shortToBytes(commandShort, commandByte);
    sendBytes(commandByte, 2);
    if (commandShort == 1) { return sendRegister(splittedStrings[1], splittedStrings[2]); }
    if (commandShort == 2) { return sendLogin(splittedStrings[1], splittedStrings[2]); }
    if (commandShort == 4) { return sendFollow(splittedStrings);}
    if (commandShort == 5) { return sendPost(splittedStrings);}
    if (commandShort == 6) { return sendPM(splittedStrings);}
    if (commandShort == 8) { return sendStat(splittedStrings[1]);}

}
 
bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    // Stop when we encounter the null character. 
    // Notice that the null character is not appended to the frame string.
    try {
		do{
			getBytes(&ch, 1);
            frame.append(1, ch);
        }while (delimiter != ch);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
    bool result=sendBytes(frame.c_str(),frame.length());
    if(!result) return false;
    return sendBytes(&delimiter,1);
}

// Close down the connection properly.
    void ConnectionHandler::close() {
        try {
            socket_.close();
        } catch (...) {
            std::cout << "closing failed: connection already closed" << std::endl;
        }
    }

    short ConnectionHandler::command2short(std::string command) {
        if (command == "REGISTER") {
            return 1;
        }
        if (command == "LOGIN") {
            return 2;
        }
        if (command == "LOGOUT") {
            return 3;
        }
        if (command == "FOLLOW") {
            return 4;
        }
        if (command == "POST") {
            return 5;
        }
        if (command == "PM") {
            return 6;
        }
        if (command == "USERLIST") {
            return 7;
        }
        if (command == "STAT") {
            return 8;
        }
        return 9;
    }

    void ConnectionHandler::shortToBytes(short num, char *bytesArr) {
        bytesArr[0] = ((num >> 8) & 0xFF);
        bytesArr[1] = (num & 0xFF);
    }

    bool  ConnectionHandler::sendRegister(std::string name, std::string pass) {
     bool ret=true;
    ret=ret&sendBytes(name.c_str(), name.length());
        char zero[2];
        shortToBytes(0, zero);
        ret=ret&sendBytes(zero, 1);
        ret=ret&sendBytes(pass.c_str(), pass.length());
        ret=ret& sendBytes(zero, 1);
        return ret;
    }

    bool ConnectionHandler::sendLogin(std::string name, std::string pass) {
    bool ret=true;
    ret=ret&sendBytes(name.c_str(), name.length());
        char zero[2];
        shortToBytes(0, zero);
        ret=ret& sendBytes(zero, 1);
        ret=ret& sendBytes(pass.c_str(), pass.length());
        ret=ret& sendBytes(zero, 1);

        return ret;
    }

    bool ConnectionHandler::sendFollow(std::vector<std::string> splittedStrings){
    bool ret=true;
        if (splittedStrings[1] == "0"){
            char zero[2];
            shortToBytes(0, zero);
            ret=ret& sendBytes(zero, 1);
        }
        else {
            sendBytes("\1",1);
        }
        short num=std::stoi(splittedStrings[2]);
        char numBytes[2];
        shortToBytes(num,numBytes);
        char temp[1];
        temp[0] = numBytes[1];
        sendBytes(temp,1);
        for(int i=0;i<stoi(splittedStrings[2]);i++ )
        {
            ret=ret&sendBytes(splittedStrings[i+3].c_str(),splittedStrings[i+3].length());
            char zero[2];
            shortToBytes(0, zero);
            sendBytes(zero, 1);
        }
        return ret;
    }

    bool ConnectionHandler::sendPost(std::vector<std::string> contant)
    {
    bool ret=true;
       // ret=ret& sendBytes(contant.c_str(),contant.length());
        for (int i = 1 ; i< static_cast<int>(contant.size()); i++){
            std::string tmp = contant[i] + " ";
            ret=ret& sendBytes(tmp.c_str(),tmp.length());
        }
        char zero[2];
        shortToBytes(0, zero);
        ret=ret& sendBytes(zero, 1);
        return ret;
    }


    bool ConnectionHandler::sendPM(std::vector<std::string> contant) {
    bool ret=true;
        ret=ret& sendBytes(contant[1].c_str(), contant[1].length());
    char zero[2];
    shortToBytes(0, zero);
        ret=ret&sendBytes(zero, 1);
        for (int i = 2 ; i< static_cast<int>(contant.size()); i++){
            std::string tmp = contant[i] + " ";
            ret=ret& sendBytes(tmp.c_str(),tmp.length());
        }
        ret=ret&sendBytes(zero, 1);
        return ret;
}

    short ConnectionHandler::bytesToShort(char *bytesArr)
    {
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
    }

    bool ConnectionHandler::sendStat(std::string name)
    {
    bool ret=true;
        ret=ret&sendBytes(name.c_str(),name.length());
    char zero[2];
    shortToBytes(0, zero);
        ret=ret& sendBytes(zero, 1);
        return ret;
    }

