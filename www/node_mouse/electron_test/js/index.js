//var socket = io('http://localhost:3000');
var socket = io('http://104.236.77.85');
var ipc = require('ipc');

// Ipc is used for async inter process communication
// between page thread and main program thread.
// Functions similar to socket.io

// Function definitions for websocket
socket.on('connect', function () {
    console.log('socket connected');

    // Move event sent from server
    socket.on('moveCursor', function (data) {
        moveMousePosition(data);
    });

    // Relative move cursor event
    socket.on('moveCursorRelative', function (data) {
        moveMouseRelativePosition(data);
    });

    // Click event sent from server
    socket.on('leftClickCursor', function (data) {
        mouseClick(data);
    });
});

// Trying to decouple mouse events from socket
// functions. This method logs event and calls
// ipc thread on backend to move mouse
moveMousePosition = function (data) {
    //console.log("X: " + data.x + " Y: " + data.y);
    ipc.send('mouseMove', data);
}

moveMouseRelativePosition = function (data) {
    //console.log("X: " + data.x + " Y: " + data.y);
    ipc.send('mouseMoveRelative', data);
}

mouseClick = function (data) {
    //console.log('left click');
    ipc.send('leftClick', data);
}
