package com.SHAudit.singHealthAudit.Auditor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class AuditorResource {
    @Autowired
    private AuditorService auditorService;

    @GetMapping("/auditors")
    public List<Auditor> getAllTheAuditors() {
        return auditorService.findAll();
    }

}

