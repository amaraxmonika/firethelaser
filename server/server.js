var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var fs = require('fs');
var router = require('./routes.js')

server.listen(80);

// registering router events 
router(server)
/*
app.get('/', function (req, res) { 
    res.sendFile(__dirname + '/index.html');
    console.log('works');
});

app.get('/ball.html', function (req, res) { 
    res.sendFile(__dirname + '/ball.html');
    console.log('works');
});

app.get('/js/:file', function (req, res) { 
    console.log(req.params.file);
    res.sendFile(__dirname + '/js/' + req.params.file);
    console.log('works');
});

app.get('/style/:file', function (req, res) { 
    console.log(req.params.file);
    res.sendFile(__dirname + '/style/' + req.params.file);
    console.log('works');
});
*/

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
       */
        
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
