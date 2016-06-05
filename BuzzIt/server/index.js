var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var clients = 0;
var players = [];

var question;
var answerOptions;

server.listen(8080,function(){
	console.log("Server is now running...")
});

io.on('connection' , function(socket){
    clients = clients+1;

    if(clients == 1){
        server.close();
    }

	console.log("Player Connected!");

	socket.emit('socketID',{ id: socket.id })
	socket.emit('getPlayers', { id: socket.id })
	socket.emit('isPlayer1', { n: clients });

	socket.broadcast.emit('newPlayer', { id: socket.id });

    if(clients == 1){
        server.listen(8080);
    }

    socket.on('playerIsReady', function(data){
        data.id = socket.id;
        socket.broadcast.emit('playerIsReady', data);

        console.log("player " + data.id + " is " + data.isReady + " ready")

        for(var i=0; i<players.length; i++){
            if(players[i].id == data.id){
                players[i].isReady = data.isReady;
            }
        }
    });

	socket.on('disconnect', function(){
        clients = clients-1;
		console.log("Player Disconnected");
		socket.broadcast.emit('playerDisconnected', { id: socket.id });
		for(var i=0; i<players.length; i++){
		    if(players[i].id == socket.id){
		        players.splice(i, 1);
		    }
		}
	});

	players.push(new player("playerName"), socket.id);
});

function player(name, id){
    this.id = id;
    this.name = name;
    this.points = 0;
    this.isReady = false;
}

function question(questionString){
    this.questionString = questionString;
}

function answerOptions(optionA, optionB, optionC, optionD){
    this.optionA = optionA;
    this.optionB = optionB;
    this.optionC = optionC;
    this.optionD = optionD;
}