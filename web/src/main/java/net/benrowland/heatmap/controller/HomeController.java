package net.benrowland.heatmap.controller;

import net.benrowland.heatmap.ForbiddenException;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.service.LoginService;
import net.benrowland.heatmap.service.StravaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class HomeController {
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public void login(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("/home.html");
    }
}
