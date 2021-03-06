package com.zorkdata.center.gate.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zorkdata.center.auth.client.config.ServiceAuthConfig;
import com.zorkdata.center.auth.client.config.UserAuthConfig;
import com.zorkdata.center.auth.client.jwt.UserAuthUtil;
import com.zorkdata.center.auth.common.util.jwt.IJWTInfo;
import com.zorkdata.center.auth.common.util.jwt.JWTInfo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.context.BaseContextHandler;
import com.zorkdata.center.common.core.RespTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/23 8:53
 */
@Component
@Slf4j
public class AdminAccessFilter extends ZuulFilter {
    public static final String REGEX = ",";
    //    @Autowired
//    @Lazy
//    private IUserService userService;
//    @Autowired
//    @Lazy
//    private ILogService logService;

    @Value("${gate.ignore.startWith}")
    private String startWith;

    @Value("${zuul.prefix}")
    private String zuulPrefix;
    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private UserAuthConfig userAuthConfig;

//    @Autowired
//    private ServiceAuthUtil serviceAuthUtil;

    @Override
    public String filterType() {
        return "pre";
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
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        final String requestUri = request.getRequestURI().substring(zuulPrefix.length());
        final String method = request.getMethod();
        BaseContextHandler.setToken(null);
        // 不进行拦截的地址
        if (isStartWith(requestUri)) {
            return null;
        }
        IJWTInfo user = null;
        try {
            user = getJWTUser(request, ctx);
        } catch (Exception e) {
            setFailedRequest(RespTools.getRespMsg(CodeTable.TOKEN_ERROR_CODE), 200);
            return null;
        }
//        List<PermissionInfo> permissionIfs = userService.getAllPermissionInfo();
//        // 判断资源是否启用权限约束
//        Stream<PermissionInfo> stream = getPermissionIfs(requestUri, method, permissionIfs);
//        List<PermissionInfo> result = stream.collect(Collectors.toList());
//        PermissionInfo[] permissions = result.toArray(new PermissionInfo[]{});
//        if (permissions.length > 0) {
//            checkUserPermission(permissions, ctx, user);
//        }
//        // 申请客户端密钥头
//        ctx.addZuulRequestHeader(serviceAuthConfig.getTokenHeader(), serviceAuthUtil.getClientToken());
        return null;
    }

    /**
     * 获取目标权限资源
     *
     * @param requestUri
     * @param method
     * @param serviceInfo
     * @return
     */
//    private Stream<PermissionInfo> getPermissionIfs(final String requestUri, final String method, List<PermissionInfo> serviceInfo) {
//        return serviceInfo.parallelStream().filter(new Predicate<PermissionInfo>() {
//            @Override
//            public boolean test(PermissionInfo permissionInfo) {
//                String url = permissionInfo.getUri();
//                String uri = url.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
//                String regEx = "^" + uri + "$";
//                return (Pattern.compile(regEx).matcher(requestUri).find() || requestUri.startsWith(url + "/"))
//                        && method.equals(permissionInfo.getMethod());
//            }
//        });
//    }
//
//    private void setCurrentUserInfoAndLog(RequestContext ctx, IJWTInfo user, PermissionInfo pm) {
//        String host = ClientUtil.getClientIp(ctx.getRequest());
//        ctx.addZuulRequestHeader("userId", user.getId());
//        ctx.addZuulRequestHeader("userName", URLEncoder.encode(user.getName()));
//        ctx.addZuulRequestHeader("userHost", ClientUtil.getClientIp(ctx.getRequest()));
//        LogInfo logInfo = new LogInfo(pm.getMenu(), pm.getName(), pm.getUri(), new Date(), user.getId(), user.getName(), host);
//        DBLog.getInstance().setLogService(logService).offerQueue(logInfo);
//    }

    /**
     * 返回session中的用户信息
     *
     * @param request
     * @param ctx
     * @return
     */
    private IJWTInfo getJWTUser(HttpServletRequest request, RequestContext ctx) throws Exception {
        String authToken = request.getHeader(userAuthConfig.getTokenHeader());
        if (StringUtils.isBlank(authToken)) {
            authToken = request.getParameter("token");
        }
        ctx.addZuulRequestHeader(userAuthConfig.getTokenHeader(), authToken);
        BaseContextHandler.setToken(authToken);
        JWTInfo jwtInfo = userAuthUtil.getInfoFromToken(authToken);
        if (jwtInfo == null) {
            throw new Exception("token 过期或者token不存在");
        } else {
            return jwtInfo;
        }
    }


//    private void checkUserPermission(PermissionInfo[] permissions, RequestContext ctx, IJWTInfo user) {
//        List<PermissionInfo> permissionInfos = userService.getPermissionByUsername(user.getUniqueName());
//        PermissionInfo current = null;
//        for (PermissionInfo info : permissions) {
//            boolean anyMatch = permissionInfos.parallelStream().anyMatch(new Predicate<PermissionInfo>() {
//                @Override
//                public boolean test(PermissionInfo permissionInfo) {
//                    return permissionInfo.getCode().equals(info.getCode());
//                }
//            });
//            if (anyMatch) {
//                current = info;
//                break;
//            }
//        }
//        if (current == null) {
//            setFailedRequest(JSON.toJSONString(new TokenForbiddenResponse("Token Forbidden!")), 200);
//        } else {
//            if (!RequestMethod.GET.toString().equals(current.getMethod())) {
//                setCurrentUserInfoAndLog(ctx, user, current);
//            }
//        }
//    }


    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri) {
        boolean flag = false;
        for (String s : startWith.split(REGEX)) {
            if (requestUri.startsWith(s)) {
                return true;
            }
        }
        return flag;
    }

    /**
     * 网关抛异常
     *
     * @param body
     * @param code
     */
    private void setFailedRequest(String body, int code) {
//        log.debug("Reporting error ({}): {}", code, body);
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(code);
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody(body);
            ctx.getResponse().setContentType("text/html;charset=UTF-8");
            ctx.setSendZuulResponse(false);
        }
    }

}
