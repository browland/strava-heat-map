package net.benrowland.heatmap.controller;

import net.benrowland.heatmap.ForbiddenException;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.service.LoginService;
import net.benrowland.heatmap.service.StravaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final String STRAVA_CONNECT_URL = "https://www.strava.com/oauth/authorize?client_id=15346&response_type=code&redirect_uri=http://localhost:8080/token_exchange/&approval_prompt=force&state=%s";

    @Autowired
    private LoginService loginService;

    @Autowired
    private StravaUserService stravaUserService;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public void login(@RequestParam String username, @RequestParam String password, HttpSession httpSession, HttpServletResponse httpServletResponse) throws IOException {
        logger.info("HS: " + httpSession);
        logger.info("Received login request for username {}", username);

        if(!loginService.authorise(username, password)) {
            throw new ForbiddenException();
        }

        StravaUserEntity stravaUserEntity = stravaUserService.findStravaUser(username);
        if(stravaUserEntity == null || stravaUserEntity.getAccessToken() == null) {
            String stravaConnectUrl = String.format(STRAVA_CONNECT_URL, username);
            httpServletResponse.sendRedirect(stravaConnectUrl);
        }
        else {
            httpSession.setAttribute("StravaUser", stravaUserEntity);
            httpServletResponse.sendRedirect("http://localhost:8080/home.html");
        }
    }
}
