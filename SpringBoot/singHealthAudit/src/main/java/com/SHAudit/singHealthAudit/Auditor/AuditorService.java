package com.SHAudit.singHealthAudit.Auditor;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuditorService {

    private static List<Auditor> auditors = new ArrayList<>();
    private static long idCounter = 0;

    static {
        auditors.add(new Auditor(++idCounter, "t2"));
        auditors.add(new Auditor(++idCounter, "t2"));
        auditors.add(new Auditor(++idCounter, "t2"));
        auditors.add(new Auditor(++idCounter, "t2"));
    }

    public List<Auditor> findAll() {
        return auditors;
    }
}
