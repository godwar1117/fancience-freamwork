package com.fancience.web.encrypt.processor;

import com.fancience.web.encrypt.annotation.WebEncrypt;
import com.fancience.web.logger.processor.LoggerRequestResponseBodyMethodProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

/**
 * 加密Processor
 * Created by Leonid on 18/3/27.
 */
public class EncryptRequestResponseBodyMethodProcessor extends AbstractMessageConverterMethodProcessor implements AsyncHandlerMethodReturnValueHandler {
    private static Logger logger = LoggerFactory.getLogger(LoggerRequestResponseBodyMethodProcessor.class);
    private static final Object NO_VALUE;
    public EncryptRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    static {
        NO_VALUE = new Object();
    }
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.hasMethodAnnotation(WebEncrypt.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        mavContainer.setRequestHandled(true);
        ServletServerHttpRequest inputMessage = this.createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = this.createOutputMessage(webRequest);
        this.writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);

    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(WebEncrypt.class);
    }

    @Override
    protected <T> Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
        //return super.readWithMessageConverters(inputMessage, parameter, targetType);
        boolean noContentType = false;
        MediaType contentType;
        try {
            contentType = inputMessage.getHeaders().getContentType();
        } catch (InvalidMediaTypeException e) {
            throw new HttpMediaTypeNotSupportedException(e.getMessage());
        }
        if(contentType == null) {
            noContentType = true;
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        }
        Class contextClass = parameter.getContainingClass();
        Class targetClass = targetType instanceof Class ? (Class)targetType:null;
        if(targetClass == null) {
            ResolvableType httpMethod = ResolvableType.forMethodParameter(parameter);
            // 获取方法中实际的参数类型
            targetClass = httpMethod.resolve();
        }
        HttpMethod httpMethod = inputMessage instanceof HttpRequest ?((HttpRequest)inputMessage).getMethod():null;
        Object body = NO_VALUE;
        EmptyBodyCheckingHttpInputMessage message;
        msgCvt : {
            message = new EmptyBodyCheckingHttpInputMessage(inputMessage);
            Iterator ex = this.messageConverters.iterator();
            HttpMessageConverter converter;
            Class converterType;
            GenericHttpMessageConverter genericConverter;
            while(true) {
                if(!ex.hasNext()) {
                    break msgCvt;
                }

                converter = (HttpMessageConverter)ex.next();
                converterType = converter.getClass();
                genericConverter = converter instanceof GenericHttpMessageConverter?(GenericHttpMessageConverter)converter:null;
                if(genericConverter != null) {
                    if(genericConverter.canRead(targetType, contextClass, contentType)) {
                        break;
                    }
                } else if(targetClass != null && converter.canRead(targetClass, contentType)) {
                    break;
                }
            }

//            if(message.hasBody()) {
//                HttpInputMessage msgToUse = this.getAdvice().beforeBodyRead(message, parameter, targetType, converterType);
//                body = genericConverter != null?genericConverter.read(targetType, contextClass, msgToUse):converter.read(targetClass, msgToUse);
//                body = this.getAdvice().afterBodyRead(body, msgToUse, parameter, targetType, converterType);
//            }
        }


        return null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        /*
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
        */
        parameter = parameter.nestedIfOptional();
        Object arg = this.readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
        String name = Conventions.getVariableNameForParameter(parameter);
        if(binderFactory != null) {
            WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
            if(arg != null) {
                this.validateIfApplicable(binder, parameter);
                if(binder.getBindingResult().hasErrors() && this.isBindExceptionRequired(binder, parameter)) {
                    throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
                }
            }
            if(mavContainer != null) {
                mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
            }
        }
        return this.adaptArgumentIfNecessary(arg, parameter);
    }

    @Override
    public boolean isAsyncReturnValue(Object o, MethodParameter methodParameter) {
        return methodParameter.hasMethodAnnotation(WebEncrypt.class);
    }

    private static class EmptyBodyCheckingHttpInputMessage implements HttpInputMessage {
        private final HttpHeaders headers;
        @Nullable
        private final InputStream body;

        public EmptyBodyCheckingHttpInputMessage(HttpInputMessage inputMessage) throws IOException {
            this.headers = inputMessage.getHeaders();
            InputStream inputStream = inputMessage.getBody();
            if(inputStream.markSupported()) {
                inputStream.mark(1);
                this.body = inputStream.read() != -1?inputStream:null;
                inputStream.reset();
            } else {
                PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
                int b = pushbackInputStream.read();
                if(b == -1) {
                    this.body = null;
                } else {
                    this.body = pushbackInputStream;
                    pushbackInputStream.unread(b);
                }
            }

        }

        public HttpHeaders getHeaders() {
            return this.headers;
        }

        public InputStream getBody() {
            return this.body != null?this.body: StreamUtils.emptyInput();
        }

        public boolean hasBody() {
            return this.body != null;
        }
    }
}
