package com.example.demoinsert.repository;

import com.example.demoinsert.AsyncRepo;
import com.example.demoinsert.BufferBatchPreparedStatementSetter;
import com.example.demoinsert.model.Buffer1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class JdbcRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcRepository.class);

    private static int numberOfThread = 10;
    private static final String sqlInsertQuery = "INSERT INTO buffer1 (name,audit1_id) VALUES (?,?)";

    private JdbcTemplate jdbcTemplate;
    private final AsyncRepo asyncRepo;

    @Autowired
    public JdbcRepository(JdbcTemplate jdbcTemplate,AsyncRepo asyncRepo) {
        this.jdbcTemplate = jdbcTemplate;
        this.asyncRepo=asyncRepo;
    }

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private static final ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void batchInsertAsync2(List<Buffer1> buffers) throws InterruptedException, ExecutionException {
        StopWatch timer = new StopWatch();
        final AtomicInteger sublists = new AtomicInteger();
        CompletableFuture[] futures = buffers.stream()
                .collect(Collectors.groupingBy(t -> sublists.getAndIncrement() / batchSize))
                .values()
                .stream()
                .map(ul -> runBatchInsert(ul, sqlInsertQuery))
                .toArray(CompletableFuture[]::new);
        CompletableFuture<Void> run = CompletableFuture.allOf(futures);//wait all thread complete the job
        timer.start();
        run.get();
        timer.stop();
        log.info("batchInsertAsync-2 -> Total time in seconds: " + timer.getTotalTimeSeconds());
    }

    public CompletableFuture<Void> runBatchInsert(List<Buffer1> buffers, String sql) {
        return CompletableFuture.runAsync(() -> {
            jdbcTemplate.batchUpdate(sql, new BufferBatchPreparedStatementSetter(buffers));
        }, executor);
    }

    public void batchInsertSync(List<Buffer1> buffers)  {
        StopWatch timer = new StopWatch();
        timer.start();
        jdbcTemplate.batchUpdate(sqlInsertQuery, buffers, batchSize, (PreparedStatement ps, Buffer1 buffer) -> {
            ps.setString(1, buffer.getName());
            ps.setLong(2, buffer.getAudit().getId());
        });
        timer.stop();
        log.info("batchSyncInsert -> Total time in seconds: " + timer.getTotalTimeSeconds());
    }

    @Async
    public void batchInsertAsync1(List<Buffer1> buffers) throws InterruptedException, ExecutionException {
        StopWatch timer = new StopWatch();
        timer.start();
        jdbcTemplate.batchUpdate(sqlInsertQuery, buffers, batchSize, (PreparedStatement ps, Buffer1 buffer) -> {
            ps.setString(1, buffer.getName());
            ps.setLong(2, buffer.getAudit().getId());
        });
//        if (buffers.size() == 1000000)
//            throw new RuntimeException("something goes wrong"); showing transaction
        timer.stop();
        log.info("batchAsyncInsert-1 -> Total time in seconds: " + timer.getTotalTimeSeconds());
    }


    public void batchInsertAsync3(List<Buffer1> buffers) throws InterruptedException, ExecutionException {
        StopWatch timer = new StopWatch();
        final AtomicInteger sublists = new AtomicInteger();
        CompletableFuture[] futures = buffers.stream()
                .collect(Collectors.groupingBy(t -> sublists.getAndIncrement() / batchSize))
                .values()
                .stream()
                .map(ul -> asyncRepo.batchUpdateAsync(ul,sqlInsertQuery))
                .toArray(CompletableFuture[]::new);

        timer.start();
        CompletableFuture.allOf(futures).join();//wait all thread complete the job
        timer.stop();
        log.info("batchInsertAsync-3 -> Total time in seconds: " + timer.getTotalTimeSeconds());
    }

}
