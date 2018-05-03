package com.fancience.logger.slf4j;

import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.joran.spi.ConsoleTarget;
import ch.qos.logback.core.status.WarnStatus;
import ch.qos.logback.core.util.EnvUtil;
import ch.qos.logback.core.util.OptionHelper;
import com.fancience.api.utils.SpringBeanUtil;
import com.fancience.logger.support.ILoggerNettyHandler;

import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by Leonid on 18/3/26.
 */
public class FancienceConsoleAppender<E> extends OutputStreamAppender<E> {
    protected ConsoleTarget target;
    protected boolean withJansi;
    private static final String WindowsAnsiOutputStream_CLASS_NAME = "org.fusesource.jansi.WindowsAnsiOutputStream";



    public FancienceConsoleAppender() {
        this.target = ConsoleTarget.SystemOut;
        this.withJansi = false;
    }

    public void setTarget(String value) {
        ConsoleTarget t = ConsoleTarget.findByName(value.trim());
        if(t == null) {
            this.targetWarn(value);
        } else {
            this.target = t;
        }

    }

    public String getTarget() {
        return this.target.getName();
    }

    private void targetWarn(String val) {
        WarnStatus status = new WarnStatus("[" + val + "] should be one of " + Arrays.toString(ConsoleTarget.values()), this);
        status.add(new WarnStatus("Using previously set target, System.out by default.", this));
        this.addStatus(status);
    }

    public void start() {
        OutputStream targetStream = this.target.getStream();
        if(EnvUtil.isWindows() && this.withJansi) {
            targetStream = this.getTargetStreamForWindows(targetStream);
        }

        this.setOutputStream(targetStream);
        super.start();
    }

    private OutputStream getTargetStreamForWindows(OutputStream targetStream) {
        try {
            this.addInfo("Enabling JANSI WindowsAnsiOutputStream for the console.");
            Object e = OptionHelper.instantiateByClassNameAndParameter("org.fusesource.jansi.WindowsAnsiOutputStream", Object.class, this.context, OutputStream.class, targetStream);
            return (OutputStream)e;
        } catch (Exception var3) {
            this.addWarn("Failed to create WindowsAnsiOutputStream. Falling back on the default stream.", var3);
            return targetStream;
        }
    }

    @Override
    protected void subAppend(E event) {
        super.subAppend(event);
        // 使用netty进行日志传输
        try {
            ILoggerNettyHandler nettyHandler = SpringBeanUtil.getBeanOfType(ILoggerNettyHandler.class);
            if (nettyHandler != null) {
                byte[] ioe = this.encoder.encode(event);
                nettyHandler.send(ioe);
            }
        } catch (Exception e) {

        }
    }

    public boolean isWithJansi() {
        return this.withJansi;
    }

    public void setWithJansi(boolean withJansi) {
        this.withJansi = withJansi;
    }
}
