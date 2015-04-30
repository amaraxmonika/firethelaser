var ipc = require('ipc');

toggleMouse = function() {
    ipc.send('toggleMouse', 'hi');
}
