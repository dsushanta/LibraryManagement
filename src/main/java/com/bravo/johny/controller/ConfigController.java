package com.bravo.johny.controller;

import com.bravo.johny.dto.LibraryConfig;
import com.bravo.johny.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping
    public LibraryConfig getConfigurations() {
        return configService.getLibraryConfigurations();
    }

    @PutMapping
    public void updateConfigurations(@RequestBody LibraryConfig config) {
        configService.updateLibraryConfigurations(config);
    }
}
