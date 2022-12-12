package com.example.demoinsert;

import com.example.demoinsert.model.Buffer1;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class BufferBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
    private List<Buffer1> buffers;

    public BufferBatchPreparedStatementSetter(List<Buffer1> buffers) {
        super();
        this.buffers = buffers;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) {
        try {
            Buffer1 buffer = buffers.get(i);
            ps.setString(1, buffer.getName());
            ps.setLong(2, buffer.getAudit().getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getBatchSize() {
        return buffers.size();
    }
}