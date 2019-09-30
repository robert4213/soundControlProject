const express = require('express');
const morgan = require('morgan');
const mongoose = require("mongoose");
const app = express();
const bodyParser = require('body-parser');

const recordRoutes = require('./api/routes/records');

mongoose.connect(
	"mongodb+srv://"+process.env.MONGO_USER+":"+process.env.MONGO_PWD+"@cluster0-vprqz.mongodb.net/test?retryWrites=true&w=majority"
);

app.use(morgan('dev'));
app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());

app.use('/records',recordRoutes);

app.use((req,res,next)=>{
	const error = new Error('Not Found');
	error.status = 404;
	next(error);	
});

app.use((error,req,res,next)=>{
	res.status(error.status|| 500);
	res.json({
		error:{
			message: error.message
		}
	});
});

module.exports = app;

