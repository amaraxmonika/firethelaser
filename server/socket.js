var io = require('socket.io')

module.exports = function (server) {
    
    var ioSocket = io(server)
    ioSocket.on('connection', function (socket) {
        socket.on('data', function (data){
            
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

            // calling socket event on client side
            io.emit('moveCursor', data);
            
        });
        socket.on('mouseRelativeCursorEvent', function (data){
            //console.dir(data);
            if (data.x == undefined){
                data = JSON.parse(data.toString());
                data = data.toString();
            }

            console.dir("data.x: " + data.x + " data.y: " + data.y);

            // calling socket event on client side
            io.emit('moveCursorRelative', data);
        });
        socket.on('leftClick', function (data){
            console.dir("data.x: " + data.x + " data.y: " + data.y);
            io.emit('leftClickCursor', data);
        });
    });
}
