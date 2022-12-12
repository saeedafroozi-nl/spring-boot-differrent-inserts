package com.example.demoinsert;

import com.example.demoinsert.model.Audit1;
import com.example.demoinsert.model.Audit2;
import com.example.demoinsert.model.Buffer1;
import com.example.demoinsert.model.Buffer2;

import java.util.ArrayList;
import java.util.List;

public class CreateObjectHelper {

    public static Audit1 createAudit1() {
        Audit1 audit = new Audit1();
        audit.setName("audit1");
        audit.setBuffers(createBuffer1(audit));
        return audit;
    }

    private static List<Buffer1> createBuffer1(Audit1 audit) {
        List<Buffer1> buffers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Buffer1 buffer = new Buffer1();
            buffer.setName("buffer1");
            buffer.setAudit(audit);
            buffers.add(buffer);
        }
        return buffers;
    }

    public static Audit2 createAudit2() {
        Audit2 audit = new Audit2();
        audit.setName("audit2");
        audit.setBuffers(createBuffer2(audit));
        return audit;
    }

    private static List<Buffer2> createBuffer2(Audit2 audit) {
        List<Buffer2> buffers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Buffer2 buffer = new Buffer2();
            buffer.setName("buffer2");
            buffer.setAudit(audit);
            buffers.add(buffer);
        }
        return buffers;
    }

}