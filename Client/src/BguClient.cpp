



#include <stdlib.h>
#include <connectionHandler.h>
#include <mutex>
#include <thread>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/

  bool finish = false;
  bool out = false;

class ServerGet {
private:
    ConnectionHandler &handler;
    std::mutex &mut;
public:
    ServerGet(ConnectionHandler &handler, std::mutex &mut) : handler(handler), mut(mut) {}

    void run() {
        do {
            std::string answer;
            // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
            // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
            handler.getLine(answer);
            if (answer == "ACK 3") {
                finish = true;
                std::cout << answer << std::endl;
            } else {
                //  answer.resize(answer.length() - 1);
                std::cout << answer << std::endl;
                out = false;
            }
        } while (!finish);
    }
};


    class KeyBoardGet {
    private:
        ConnectionHandler &handler;
        std::mutex &mut;
    public:
        KeyBoardGet(ConnectionHandler &handler, std::mutex &mut) : handler(handler), mut(mut) {}

        void run() {
            while (!finish) {
                while (!out) {
                    const short bufsize = 1024;
                    char buf[bufsize];
                    std::cin.getline(buf, bufsize);
                    std::string line(buf);
                    handler.sendLine(line);
                    if (line == "LOGOUT")
                        out = true;
                }
            }
        }
    };


    int main(int argc, char *argv[]) {
   /*     if (argc < 3) {
            std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
            return -1;
         }
        std::string host = argv[1];
      short port = atoi(argv[2]); */
       std::string host = "127.0.0.1";
       short port = 7777;

        ConnectionHandler connectionHandler(host, port);
        if (!connectionHandler.connect()) {
            std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
            return 1;
        }
        std::mutex mutex;
        ServerGet server(connectionHandler, mutex);
        KeyBoardGet keyBoard(connectionHandler, mutex);
        std::thread th1(&ServerGet::run, &server);
        std::thread th2(&KeyBoardGet::run, &keyBoard);
        th1.join();
        th2.join();
        return 0;
    }

