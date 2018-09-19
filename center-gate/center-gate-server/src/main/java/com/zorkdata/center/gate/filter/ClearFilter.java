package com.zorkdata.center.gate.filter;

import com.netflix.zuul.ZuulFilter;
import com.zorkdata.center.common.context.BaseContextHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/23 8:57
 */
@Component
@Slf4j
public class ClearFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        BaseContextHandler.remove();
        return null;
    }

}
