package com.lys.community.controller.back_stage;

import org.springframework.http.codec.multipart.PartHttpMessageWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin
@Controller
public class BackStageController {

    @RequestMapping(path = "/toback",method = RequestMethod.GET)
    public String Back(){
        return "back_stage/index";
    }
}
