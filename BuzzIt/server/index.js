var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var clients = 0;
var players = [];

var currentQuestion;

server.listen(8080, function(){
	console.log("Server is now running...")
});

io.on('connection' , function(socket){
    clients = clients+1;

	console.log("Player Connected!");

	socket.emit('socketID',{ id: socket.id })

	socket.emit('welcomingStep', { n: clients });

	if(clients == 1){
        socket.emit('playerOne', { id: socket.id })
    } else {
	    socket.broadcast.emit('newPlayer', { id: socket.id });
    }

    socket.on('playerIsReady', function(data){
        data.id = socket.id;
        socket.broadcast.emit('playerIsReady', data);

        for(var i = 0; i < players.length; i++){
            if(players[i].id == data.id){
                players[i].name = data.playerName;
                console.log("Player name: " + players[i].name);
            }
        }
    });

	socket.on('disconnect', function(){
        clients = clients-1;
		console.log("Player Disconnected");
		io.sockets.emit('playerDisconnected', { id: socket.id });
		for(var i=0; i<players.length; i++){
		    if(players[i].id == socket.id){
		        players.splice(i, 1);
		    }
		}
	});

	socket.on('gameStart', function(){
		console.log("Starting Game");
        //currentQuestion = new question("","","","","");
		socket.broadcast.emit('gameStart', "gameStart")
	});

    socket.on('sendQuestion', function(data){
        data.id = socket.id;
        socket.broadcast.emit('sendQuestion', data);
    });

	players.push(new player("playerName"), socket.id);
});

function player(name, id){
    this.id = id;
    this.name = name;
    this.points = 0;
    this.isReady = false;
}