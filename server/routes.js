// File for registering route listeners for express
var fs = require('fs')

module.exports = function (app) {

    // apply routes here
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

}
