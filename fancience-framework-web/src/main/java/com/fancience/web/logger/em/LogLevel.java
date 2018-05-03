package com.fancience.web.logger.em;

/**
 * 日志级别
 * Created by Leonid on 17/10/26.
 */
public enum LogLevel {

    Low(1, "低"),
    Medium(2, "中"),
    High(3, "高");

    private Integer id;
    private String name;

    private LogLevel(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
