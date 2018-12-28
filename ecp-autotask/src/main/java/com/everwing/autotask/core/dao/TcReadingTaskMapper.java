package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by DELL on 2018/6/5.
 */
@Repository
public interface TcReadingTaskMapper {

    List<TcReadingTask> findCanCopyTasks();

    TcReadingTask getNextTaskByCurrentId(String id);

    void insert(TcReadingTask newTask);

    Integer startInitTasks();

    List<TcReadingTask> findCanCompleteTasks();

    int update(TcReadingTask task);
}
