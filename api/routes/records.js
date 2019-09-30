const express = require('express');
const router = express.Router();
const Record = require('../models/record');
const mongoose = require("mongoose");

router.get('/',(req,res,next)=>{
    Record.find().exec().then(data =>{
        console.log(data);
        res.status(200).json(data);
    })
    .catch(err =>{
        console.log(err);
        res.status(500).json({error:err});
    });
});

router.post('/',(req,res,next)=>{
    const record =  new Record({
        type: req.body.type,
        data: req.body.data,
        target: req.body.target
    });

    record.save().then(result =>{
        console.log(result);

        res.status(200).json({
            message:'good post',
            newRecord: result
        });
    })
    .catch(err =>{
        console.log(err);
        res.status(500).json({error:err});
    });
});

router.get('/:recordId',(req,res,next)=>{
    const id = req.params.recordId;
    Record.findById(id)
    .exec()
    .then( data =>{
        console.log(data);
        res.status(200).json(data);
    })
    .catch(err=>{
        console.log(err);
        res.status(500).json({error:err});
    });
    
});

router.delete('/:recordId',(req,res,next)=>{
    const recordId = req.params.recordId;
    Record.remove({_id:recordId}).exec().then(result=>{
        console.log(result);
        res.status(200).json({result:result});
    })
    .catch(err =>{
        console.log(err);
        res.status(500).json({error:err});
    });
});

router.patch('/:recordId',(req,res,next)=>{
    const recordId = req.params.recordId;
    const updateOps = {};
    for(op of req.body ){
        updateOps[op.propName] = op.value;
    }
    Record.update({_id:recordId},{$set:updateOps})
    .exec()
    .then(result=>{
        console.log(result);
        res.status(200).json({result:result});
    })
    .catch(err =>{
        console.log(err);
        res.status(500).json({error:err});
    });
});

module.exports = router;