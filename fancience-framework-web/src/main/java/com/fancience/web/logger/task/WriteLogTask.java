package com.fancience.web.logger.task;

import com.alibaba.fastjson.JSON;
import com.fancience.api.exception.ServiceException;
import com.fancience.api.web.service.ILogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;
import java.util.Map;

/**
 * 写日志的定时任务
 * Created by Leonid on 18/3/21.
 */
public class WriteLogTask implements Runnable, Serializable {

    private static Logger logger = LoggerFactory.getLogger(WriteLogTask.class);

    private ILogService logService;

    private Map<String, Object> entity;

    public WriteLogTask(ILogService logService,
                        Map<String, Object> entity) {
        this.logService = logService;
        this.entity = entity;
    }

    final static String ERROR_LOG_SERVICE_NO_FOUND = "日志处理器未加载，无法记录日志";
    final static String ERROR_LOG_ENTITY_NULL = "日志记录对象不存在，无法记录日志";

    @Override
    public void run() {
        if (logService == null) {
            logger.error(ERROR_LOG_SERVICE_NO_FOUND);
        }
        if (entity == null) {
            logger.error(ERROR_LOG_ENTITY_NULL);
        }
        if (logService != null && entity != null) {
            try {
                ((ILogService)logService).writeLog(entity);
            } catch (ServiceException e) {
                logger.error("日志纪录失败=============================>");
                logger.error("log : {}" , JSON.toJSONString(entity));
                logger.error("error : {}" , e.getMessage());
                logger.error("日志纪录失败=============================>");
            }
        }
    }

}
