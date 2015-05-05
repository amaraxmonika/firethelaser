var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var fs = require('fs');
var router = require('./routes.js')
var websocket = require('./socket.js')
var mongoose = require('mongoose')

// Server listening on port 80
server.listen(80);

// Setting up connection to mongoDB
var connString = 'mongodb://db1_test:password@dbh46.mongolab.com:27467/fire_the_laser'
mongoose.connect(connString)

// registering router events 
router(app)


// Setting up mongoose Schema
var User = mongoose.model('User', {
    name: String,
    pass: String, 
    role: String, 
    cursor: String
});

var addUser = function (user) {
    var dbUser = new User (user);
    dbUser.save(function (err) {
        if (err) console.log('there was an error');
        console.log('success');
    });

};

var queryUser = function (user) {
    User.findOne({ 'name': user.name, 'pass': user.pass }, 
                 function (err, person) {
        console.log('found one');
        console.dir(person);
    });
}

// registering websocket events
websocket(io, addUser, queryUser)
