package Auditor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
public class AuditorResource {
    @Autowired
    private AuditorService auditorService;

    @GetMapping("/instructors/{username}/courses")
    public List<Auditor> getAllCourses(@PathVariable String username) {
        return auditorService.findAll();
    }
}

