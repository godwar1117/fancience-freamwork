package com.fancience.web.logger.em;

/**
 * 日志类型
 * Created by Leonid on 17/10/26.
 */
public enum LogType {

    System(1, "系统操作日志"), // 用户不可见
    User(2, "用户操作日志"); // 用户可见

    private Integer id;
    private String name;

    private LogType(Integer id, String name) {
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
