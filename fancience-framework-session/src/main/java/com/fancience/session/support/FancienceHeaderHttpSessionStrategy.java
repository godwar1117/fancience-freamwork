package com.fancience.session.support;

import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * spring session 2.0
 * Created by Leonid on 18/3/14.
 */
public class FancienceHeaderHttpSessionStrategy extends HeaderHttpSessionIdResolver {

    public FancienceHeaderHttpSessionStrategy(String headerName) {
        super(headerName);
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        return super.resolveSessionIds(request);
    }
}
