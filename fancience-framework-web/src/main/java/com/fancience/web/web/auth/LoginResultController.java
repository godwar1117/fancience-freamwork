package com.fancience.web.web.auth;

import com.fancience.api.protocol.WebFormResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制controller
 * Created by Leonid on 18/3/20.
 */
@RestController
@RequestMapping("/login")
public class LoginResultController {

    @RequestMapping("/success")
    public Object success(HttpServletRequest request) {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", request.getSession().getId());
        map.put("user", user);
        WebFormResult<?> result = WebFormResult.createSuccessResult(map, "登录成功");
        return result;
    }

    @RequestMapping("/failure")
    public Object failure(HttpServletRequest request) {
        return WebFormResult.createErrorResult("登录失败");
    }

}
