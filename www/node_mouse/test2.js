var robot = require('robotjs');

var mouse = robot.getMousePos();
console.log('Mouse it at x: ' + mouse.x + ' y:' + mouse.y);

robot.moveMouse(mouse.x, mouse.y+100);
