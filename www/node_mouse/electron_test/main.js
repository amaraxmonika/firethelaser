var app = require('app');  // Module to control application life.
var BrowserWindow = require('browser-window');  // Module to create native browser window.
var robot = require('robotjs');
var ipc = require('ipc');
//var robot = '';

// Report crashes to our server.
require('crash-reporter').start();

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the javascript object is GCed.
var mainWindow = null;

// Quit when all windows are closed.
app.on('window-all-closed', function() {
  if (process.platform != 'darwin')
    app.quit();
});

// This method will be called when Electron has done everything
// initialization and ready for creating browser windows.
app.on('ready', function() {
  // Create the browser window.
  mainWindow = new BrowserWindow({width: 800, height: 600});
  //mainWindow = new BrowserWindow({width: 800, height: 600, frame: false});

  // and load the index.html of the app.
  mainWindow.loadUrl('file://' + __dirname + '/index.html');
  //mainWindow.loadUrl('http://beardownforwhat.com');
  //mainWindow.loadUrl('http://104.236.77.85');

  // fires when page is ready
  mainWindow.webContents.on('did-finish-load', function(){
      mainWindow.webContents.send('ping', 'whooooooh!');
  });

  // fires when mouse move event has fired
  ipc.on('mouseMove', function(event, data){
      var mouse = robot.getMousePos();
      //console.log('mouse is at x: ' + mouse.x + ' y: ' + mouse.y);
      //console.log('data is x: ' + data.x + ' y: ' + data.y);
      // moving mouse here
      robot.moveMouse(mouse.x+data.x, mouse.y+data.y);
  });

  // fires when mouse click event has fired
  ipc.on('leftClick', function(event, data){
       robot.mouseClick();
       console.log('left mouse click server');
  });
  // Emitted when the window is closed.
  mainWindow.on('closed', function() {
    // Dereference the window object, usually you would store windows
    // in an array if your app supports multi windows, this is the time
    // when you should delete the corresponding element.
    mainWindow = null;
  });
});
