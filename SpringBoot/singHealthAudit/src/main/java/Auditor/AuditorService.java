package Auditor;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuditorService {

    private static List<Auditor> auditors = new ArrayList<>();
    private static long idCounter = 0;

    static {
        auditors.add(new Auditor(++idCounter, "in28minutes"));
        auditors.add(new Auditor(++idCounter, "in28minutes"));
        auditors.add(new Auditor(++idCounter, "in28minutes"));
        auditors.add(new Auditor(++idCounter, "in28minutes"));
    }

    public List<Auditor> findAll() {
        return auditors;

    }
}
