// File for registering socket listeners

module.exports = function (io, addUser, queryUser) {
    
    //var ioSocket = io(server)
    io.on('connection', function (socket) {
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
            data = JSON.parse(data);
            console.dir("data.x: " + data.x + " data.y: " + data.y);
            io.emit('leftClickCursor', data);
        });

        // handler if user changes cursor color
        socket.on('changeCursor', function (data) {
            io.emit('changeCursorEvent');
        });

        // handler if sent user data object
        socket.on('addUser', function (data) {
            console.log('addUser: ');
            console.dir(data);
            addUser(data)
        });

        // handler if query user
        socket.on('queryUser', function (data) {
            console.log('queryUser: ');
            console.dir(data);
            queryUser(data)
        });
    });
}
