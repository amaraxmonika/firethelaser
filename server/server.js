var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var fs = require('fs');
var router = require('./routes.js')
var websocket = require('./socket.js')

server.listen(8080);

// registering router events 
router(app)

// registering websocket events
websocket(io)

/*
// websocket stuff
io.on('connection', function (socket) {
    socket.on('data', function (data){
        //console.dir("data: " + data.data);
        /*
        data = JSON.parse(data.toString());
        data = data.toString();
        console.dir(data);
        console.dir("data: " + data.x);
        console.dir("data: " + data.y);
       //
        
    });
    socket.on('moveCursorEvent', function (data){
        //console.dir(data);
        if (data.x == undefined || data == undefined){
            //data = JSON.parse(data.toString());
            data = JSON.parse(data);
            console.dir(data);
            //data = data.toString();
            //console.dir(data);
        }
        console.dir("data: " + data.x);
        console.dir("data: " + data.y);
        io.emit('moveCursor', data);
        
    });
    socket.on('mouseRelativeCursorEvent', function (data){
        if (data.x == undefined){
            data = JSON.parse(data.toString());
            data = data.toString();
        }
        console.dir("data: " + data.x);
        console.dir("data: " + data.y);
        io.emit('moveCursorRelative', data);
    });
    socket.on('leftClick', function (data){
        console.dir("data: " + data.x);
        console.dir("data: " + data.y);
        io.emit('leftClickCursor', data);
        
    });
});
*/
