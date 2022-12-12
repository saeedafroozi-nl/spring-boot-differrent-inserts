package com.example.demoinsert;

import com.example.demoinsert.model.Buffer1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class AsyncRepo {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AsyncRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async
    public CompletableFuture batchUpdateAsync(List<Buffer1> buffers, String sql) {
       return CompletableFuture.completedFuture(jdbcTemplate.batchUpdate(sql, new BufferBatchPreparedStatementSetter(buffers)));
    }
}
