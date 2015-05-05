var mongoose = require('mongoose')
var mongoClient = require('./mongoClient.js')
var connString = 'mongodb://db1_test:password@dbh46.mongolab.com:27467/fire_the_laser'
mongoClient(mongoose, connString)
var usr = {'name':'chase', 'pass':'lePass'}
client.findUser(usr);
