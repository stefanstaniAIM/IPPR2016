package at.fhjoanneum.ippr.processengine.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  // @AuthenticationPrincipal final UserDetails user
  @GetMapping("/home")
  public String home() {
    return "hello cool guy with the name "; // + user;
  }

}
