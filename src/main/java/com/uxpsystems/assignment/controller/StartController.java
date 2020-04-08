package com.uxpsystems.assignment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StartController {
    private final Logger LOGGER = LoggerFactory.getLogger(StartController.class);

    @RequestMapping("/")
    public String index() {
        LOGGER.info("Index call");
        return "index";
    }
}
