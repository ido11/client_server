CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lpthread

all: Client
	g++ -o bin/BGSclient bin/connectionHandler.o bin/BguClient.o $(LDFLAGS)

Client: bin/connectionHandler.o bin/BguClient.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/BguClient.o: src/BguClient.cpp
	g++ $(CFLAGS) -o bin/BguClient.o src/BguClient.cpp

.PHONY: clean
clean:
	rm -f bin/*