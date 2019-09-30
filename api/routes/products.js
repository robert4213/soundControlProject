const express = require('express');
const router =  express.Router();

router.get('/',(req,res,next)=>{
    res.status(200).json({
        message:'good get'
    });
});

router.post('/',(req,res,next)=>{
    res.status(200).json({
        message:'good post'
    });
});

router.get('/:productId',(req,res,next)=>{
    const id = req.params.productId;
    if(id === 'special'){
        res.status(200).json({
            message:'special id'
        });
    }else{
        res.status(200).json({
            message:id
        });
    }
    
});

module.exports = router;