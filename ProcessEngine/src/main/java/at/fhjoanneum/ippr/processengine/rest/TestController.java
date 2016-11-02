package at.fhjoanneum.ippr.processengine.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/home")
  public String home(@AuthenticationPrincipal final UserDetails user) {
    return "hello cool guy with the name " + user;
  }

}
