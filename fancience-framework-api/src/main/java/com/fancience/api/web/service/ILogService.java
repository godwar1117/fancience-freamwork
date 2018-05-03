package com.fancience.api.web.service;

import com.fancience.api.exception.ServiceException;

import java.util.Map;

/**
 * 记录日志service
 * Created by Leonid on 18/3/21.
 */
public interface ILogService {

    /**
     * 记录日志方法
     * @param entity
     * @throws ServiceException
     */
    void writeLog(Map<String, Object> entity) throws ServiceException;

}
