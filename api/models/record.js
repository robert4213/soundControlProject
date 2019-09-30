const mongoose = require('mongoose');
const autoIncrement = require('mongoose-auto-increment');

mongoose.connect(
	"mongodb+srv://"+process.env.MONGO_USER+":"+process.env.MONGO_PWD+"@cluster0-vprqz.mongodb.net/test?retryWrites=true&w=majority"
);

autoIncrement.initialize(mongoose.connection);

const recordScheme = mongoose.Schema({
    type: String
})

recordScheme.plugin(autoIncrement.plugin,"Record");

module.exports = mongoose.model('Record', recordScheme);