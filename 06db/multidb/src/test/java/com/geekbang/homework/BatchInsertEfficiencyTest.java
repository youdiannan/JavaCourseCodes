package com.geekbang.homework;

import com.geekbang.Application;
import com.geekbang.config.DBConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BatchInsertEfficiencyTest {

    @Resource
    private DataSource dataSource;

    @Resource
    private DBConfig dbConfig;

    private Long startTime;

    private Long endTime;

    private String insertSql = "INSERT INTO TB_MALL_ORDER (Id, OrderTime, PayTime, DiscountAmount, ActualPrice, Buyer, " +
            "CreateTime, UpdateTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    @Before
    public void beforeTest() {
        startTime = System.currentTimeMillis();
    }

    @After
    public void afterTest() {
        endTime = System.currentTimeMillis();
        System.out.println("insert finished, time cost: " + (endTime - startTime) + " ms");
        System.out.println("============== end ==============");
    }

    /**
     * 62636ms
     * @throws Exception
     */
    @Test
    public void rawConnectionSerialBatchInsert() throws Exception {
        System.out.println("============== rawConnectionBatchInsert begin ==============");
        Connection connection = getRawConnection();
        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                long id = i * 1000L + j + 1;
                fillOrderValue(preparedStatement, id);
                preparedStatement.addBatch();
            }
            int[] ints = preparedStatement.executeBatch();
            connection.commit();

            int count = Arrays.stream(ints).sum();
            System.out.println(String.format("rawConnectionSerialBatchInsert, times: %d, %d records successfully insert.", i+1, count));
        }

        connection.close();
    }

    /**
     * 15893ms
     * @throws Exception
     */
    @Test
    public void rawConnectionParallelBatchInsert() throws Exception {
        ExecutorService workStealingPool = Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors() * 2);
        List<Future> futures = new ArrayList<>();
        AtomicLong idRecorder = new AtomicLong();
        for (int i = 0; i < 1000; i++) {
            futures.add(workStealingPool.submit(createBatchInsertTask(1000, idRecorder)));
        }

        for (Future future : futures) {
            future.get();
        }
    }

    /**
     * 保留索引 - 15068ms
     * 不带索引插入 - 11703ms
     * @throws Exception
     */
    @Test
    public void cpParallelBatchInsert() throws Exception {
        ExecutorService workStealingPool = Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors() * 2);
        List<Future> futures = new ArrayList<>();
        AtomicLong idRecorder = new AtomicLong();
        for (int i = 0; i < 1000; i++) {
            futures.add(workStealingPool.submit(createCpBatchInsertTask(1000, idRecorder)));
        }

        for (Future future : futures) {
            future.get();
        }
    }

    private void fillOrderValue(PreparedStatement preparedStatement, long id) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setLong(2, System.currentTimeMillis());
        preparedStatement.setLong(3, System.currentTimeMillis());
        preparedStatement.setBigDecimal(4, BigDecimal.valueOf(id));
        preparedStatement.setBigDecimal(5, BigDecimal.valueOf(id));
        preparedStatement.setLong(6, id);
        long now = System.currentTimeMillis();
        preparedStatement.setLong(7, now);
        preparedStatement.setLong(8, now);
    }

    private Connection getRawConnection() throws Exception {
        return DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
    }

    private Runnable createBatchInsertTask(int batchSize, AtomicLong idRecorder) {
        long startId = idRecorder.addAndGet(batchSize);
        return () -> {
            try {
                Connection connection = getRawConnection();
                connection.setAutoCommit(false);

                PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
                for (int i = 0; i < batchSize; i++) {
                    fillOrderValue(preparedStatement, startId + i);
                    preparedStatement.addBatch();
                }
                int[] ints = preparedStatement.executeBatch();
                connection.commit();

                int count = Arrays.stream(ints).sum();
                System.out.println(String.format("rawConnectionParBatchInsert, %d records successfully insert.", count));
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private Runnable createCpBatchInsertTask(int batchSize, AtomicLong idRecorder) {
        long startId = idRecorder.addAndGet(batchSize);
        return () -> {
            try {
                Connection connection = dataSource.getConnection();
                connection.setAutoCommit(false);

                PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
                for (int i = 0; i < batchSize; i++) {
                    fillOrderValue(preparedStatement, startId + i);
                    preparedStatement.addBatch();
                }
                int[] ints = preparedStatement.executeBatch();
                connection.commit();

                int count = Arrays.stream(ints).sum();
                System.out.println(String.format("cpConnectionParBatchInsert, %d records successfully insert.", count));
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }




}
