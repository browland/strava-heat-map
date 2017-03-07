package net.benrowland.heatmap.controller;

import net.benrowland.heatmap.security.ForbiddenException;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.service.StravaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private static final String STRAVA_CONNECT_URL = "https://www.strava.com/oauth/authorize?" +
        "client_id=15346&response_type=code&redirect_uri=%stoken_exchange/&approval_prompt=force&state=%s";

    @Autowired
    private StravaUserService stravaUserService;

    @Value("${external.endpointUrl}")
    private String externalEndpointUrl;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public void home(HttpServletResponse httpServletResponse) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user == null) {
            log.error("No user in principal!");
            throw new ForbiddenException();
        }

        if(newlyRegisteredUser(user)) {
            String stravaConnectUrl = String.format(STRAVA_CONNECT_URL, externalEndpointUrl, user.getUsername());
            httpServletResponse.sendRedirect(stravaConnectUrl);
            return;
        }

        // We're all good now ...
        httpServletResponse.sendRedirect("/home.html");
    }

    protected boolean newlyRegisteredUser(User user) {
        StravaUserEntity stravaUserEntity = stravaUserService.findStravaUser(user.getUsername());
        return stravaUserEntity == null || stravaUserEntity.getAccessToken() == null;
    }
}
