package com.fancience.web.logger.processor;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.fancience.api.protocol.WebFormResult;
import com.fancience.api.web.service.ILogService;
import com.fancience.web.logger.annotation.WebLog;
import com.fancience.web.logger.em.LogLevel;
import com.fancience.web.logger.em.LogType;
import com.fancience.web.logger.task.WriteLogTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作日志记录信息
 * Created by Leonid on 17/10/23.
 */
public class LoggerRequestResponseBodyMethodProcessor extends AbstractMessageConverterMethodProcessor implements AsyncHandlerMethodReturnValueHandler {

    private static Logger logger = LoggerFactory.getLogger(LoggerRequestResponseBodyMethodProcessor.class);

    public LoggerRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired(required = false)
    private ILogService logService;

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.hasMethodAnnotation(WebLog.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        String inParameters = JSON.toJSONString(webRequest.getParameterMap());
        String outParameters = JSON.toJSONString(returnValue);
        String ip = HttpUtil.getClientIP((HttpServletRequest) webRequest.getNativeRequest(), null);
        String jsonParameters = null;
        if (!mavContainer.getDefaultModel().isEmpty()) {
            Object obj = mavContainer.getDefaultModel().get("org.springframework.validation.BindingResult.map");
            if (null != obj && obj instanceof BeanPropertyBindingResult) {
                BeanPropertyBindingResult beanPropertyBindingResult = (BeanPropertyBindingResult) obj;
                jsonParameters = JSON.toJSONString(beanPropertyBindingResult.getTarget());
            }
        }
        logger.info("IN parameter : {}", inParameters);
        logger.info("OUT parameter : {}", outParameters);
        logger.info("JSON parameter : {}", jsonParameters);
        logger.info("ip {}", ip);
        AnnotatedElement annotatedElement = AnnotatedElementUtils.forAnnotations(returnType.getMethodAnnotations());
        Annotation logAnnotation = annotatedElement.getAnnotation(WebLog.class);
        if(null != logAnnotation) {
            try {
                // 记录日志
                Object name = AnnotationUtils.getValue(logAnnotation);
                Map<String, Object> logEntity = new HashMap<String, Object>();
                // 获取日志相关信息
                logEntity.put("inParameters", inParameters);
                logEntity.put("outParameters", outParameters);
                logEntity.put("name", name.toString());

                LogType type = (LogType) AnnotationUtils.getValue(logAnnotation, "type");
                logEntity.put("logType", type.getName());

                LogLevel logLevel = (LogLevel) AnnotationUtils.getValue(logAnnotation, "level");
                logEntity.put("logLevel", logLevel.getName());

                Boolean iserror = (Boolean) AnnotationUtils.getValue(logAnnotation, "isError");
                logEntity.put("iserror", iserror);

                logEntity.put("url", webRequest.getDescription(true));

                if(returnValue instanceof WebFormResult) {
                    logEntity.put("desc",((WebFormResult) returnValue).getLogMessage());
                }
                WriteLogTask writeLogTask = new WriteLogTask(logService, logEntity);
                threadPoolTaskExecutor.execute(writeLogTask);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        // 去掉前台不需要的信息
        if(returnValue instanceof WebFormResult) {
            returnValue = WebFormResult.toFrontResult((WebFormResult)returnValue);
        }

        mavContainer.setRequestHandled(true);
        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);

        // Try even with null return value. ResponseBodyAdvice could get involved.
        writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);

    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        parameter = parameter.nestedIfOptional();
        Object arg = readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
        String name = Conventions.getVariableNameForParameter(parameter);

        WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
        if (arg != null) {
            validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
            }
        }
        mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());

        return adaptArgumentIfNecessary(arg, parameter);
    }

    @Override
    public boolean isAsyncReturnValue(Object o, MethodParameter methodParameter) {
        return methodParameter.hasMethodAnnotation(WebLog.class);
    }
}
