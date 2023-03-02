package shop.zip.travel.presentation;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController2 {

  @GetMapping("/api/test")
  public String test(@RequestHeader Map<String, Object> requestHeader) {
    System.out.println(requestHeader);
    return "hello";
  }
}
