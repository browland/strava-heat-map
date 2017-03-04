package net.benrowland.heatmap.controller;

import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.service.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class OAuthCallbackController {
    private static final Logger logger = LoggerFactory.getLogger(OAuthCallbackController.class);

    @Autowired
    private OAuthService oAuthService;

    @RequestMapping(path = "/token_exchange", method = RequestMethod.GET)
    public void tokenExchange(@RequestParam("state") String state,
                              @RequestParam("code") String authorisationCode, HttpServletResponse httpServletResponse)
            throws StravaApiException, IOException {
        logger.info("Received OAuth callback state (user name) {}, authorisation code {}", state, authorisationCode);

        oAuthService.authorise(state, authorisationCode);
        httpServletResponse.sendRedirect("/home.html");
    }
}
