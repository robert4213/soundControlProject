const express = require('http');
const router =  express();

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

modules.export = router;