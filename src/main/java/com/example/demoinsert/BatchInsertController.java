package com.example.demoinsert;

import com.example.demoinsert.model.Audit1;
import com.example.demoinsert.model.Audit2;
import com.example.demoinsert.repository.Audit1JpaRepository;
import com.example.demoinsert.repository.Audit2JpaRepository;
import com.example.demoinsert.repository.BufferJpaRepository;
import com.example.demoinsert.repository.JdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/insert")
public class BatchInsertController {

    private final Audit1JpaRepository audit1JpaRepository;
    private final Audit2JpaRepository audit2JpaRepository;
    private final BufferJpaRepository bufferJpaRepository;
    private final JdbcRepository jdbcRepository;

    @Autowired
    public BatchInsertController(Audit1JpaRepository audit1JpaRepository, Audit2JpaRepository audit2JpaRepository, BufferJpaRepository bufferJpaRepository, JdbcRepository jdbcRepository) {
        this.audit1JpaRepository = audit1JpaRepository;
        this.audit2JpaRepository = audit2JpaRepository;
        this.bufferJpaRepository = bufferJpaRepository;
        this.jdbcRepository = jdbcRepository;
    }

    @RequestMapping("/old")
    @PostMapping
    @Transactional
    public boolean testInsertOldWay() {
        Audit1 audit = CreateObjectHelper.createAudit1();
        audit1JpaRepository.save(audit);
        return true;
    }

    @RequestMapping("/sequence")
    @PostMapping
    @Transactional
    public boolean testInsertSequenceWay() {
        Audit2 audit = CreateObjectHelper.createAudit2();
        audit2JpaRepository.save(audit);
        bufferJpaRepository.saveAll(audit.getbuffers());
        return true;
    }

    @RequestMapping("/sync")
    @PostMapping
    @Transactional
    public boolean testInsertJdbcSyncWay() {
        Audit1 audit = CreateObjectHelper.createAudit1();
        audit1JpaRepository.save(audit);
        jdbcRepository.batchInsertSync(audit.getbuffers());
//                if (audit.getbuffers().size() == 100)
//            throw new RuntimeException("something goes wrong");
        return true;
    }

    @RequestMapping("/asyncV1")
    @PostMapping
    @Transactional
    public boolean testInsertJdbcASyncWay1() throws InterruptedException, ExecutionException {
        Audit1 audit = CreateObjectHelper.createAudit1();
        audit1JpaRepository.save(audit);
        jdbcRepository.batchInsertAsync1(audit.getbuffers());

        return true;
    }

    @RequestMapping("/asyncV2")
    @PostMapping
    //@Transactional
    public boolean testInsertJdbcAsyncWay2() throws InterruptedException, ExecutionException {
        Audit1 audit = CreateObjectHelper.createAudit1();
        audit1JpaRepository.save(audit);
        jdbcRepository.batchInsertAsync2(audit.getbuffers());
        return true;
    }

    @RequestMapping("/asyncV3")
    @PostMapping
    @Transactional
    public boolean testInsertJdbcAsyncWay3() throws InterruptedException, ExecutionException {
        Audit1 audit = CreateObjectHelper.createAudit1();
        audit1JpaRepository.save(audit);
        jdbcRepository.batchInsertAsync3(audit.getbuffers());
        return true;
    }

}
