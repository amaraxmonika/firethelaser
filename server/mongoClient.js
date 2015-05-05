// File interface for mongodb
//var mongoose = require('mongoose')
//var connString = 'mongodb://db1_test:password@dbh46.mongolab.com:27467/fire_the_laser'

module.exports = function (mongoose, connString) {
    // setting up user schema
    var User = mongoose.model('User', {
        name: String,
        pass: String, 
        role: String, 
        cursor: String
    });

    // Connecting to the database
    mongoose.connect(connString)
    var db = mongoose.connection
    db.once('open', function () {
        //exports.addUser = function (user) {
        addUser: function (user) {
            var dbUser = new User (user);
            dbUser.save(function (err) {
                if (err) console.log('there was an error');
                console.log('success');
            });
        };
        
        // Query a user
        //exports.findUser = function (user) {
        findUser: function (user) {
            User.findOne({ 'name': user.name, 'pass': user.password }, function (err, person) {
                console.log('found one');
                console.dir(person);
            });
        };
    });

};// end exports
