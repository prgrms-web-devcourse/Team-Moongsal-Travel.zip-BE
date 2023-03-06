package shop.zip.travel.presentation.scrap;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.scrap.dto.req.ScrapCreateReq;
import shop.zip.travel.domain.scrap.dto.res.ScrapDetailRes;
import shop.zip.travel.domain.scrap.dto.res.ScrapSimpleRes;
import shop.zip.travel.domain.scrap.service.ScrapService;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
public class ScrapController {

  private final ScrapService scrapService;

  public ScrapController(ScrapService scrapService) {
    this.scrapService = scrapService;
  }

  // scrap 할 때 이름만 띄워주는게 아니라 objectId 도 한번에 건네주기
  // 문서 목록 조회용 API
  @GetMapping("/api/storage")
  public ResponseEntity<List<ScrapSimpleRes>> getAllStorage(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    List<ScrapSimpleRes> allStorageList = scrapService.findAllStorage(userPrincipal.getUserId());
    return ResponseEntity.ok(allStorageList);
  }

  // 문서 상세 조회 API
  @GetMapping("/api/storage/{objectId}")
  public ResponseEntity<ScrapDetailRes> getOneStorage(@PathVariable ObjectId objectId) {
    ScrapDetailRes scrapDetailRes = scrapService.findOneStorage(objectId);
    return ResponseEntity.ok(scrapDetailRes);
  }

  @PostMapping("/api/storage/{title}")
  public ResponseEntity<Void> createStorage(@PathVariable String title, @AuthenticationPrincipal
  UserPrincipal userPrincipal) {
    Long memberId = userPrincipal.getUserId();
    scrapService.createStorage(memberId, title);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/api/storage/scrap")
  public ResponseEntity<Void> addMyScrap(@RequestBody ScrapCreateReq scrapCreateReq) {
    scrapService.addContent(scrapCreateReq.objectId(), scrapCreateReq.content());
    return ResponseEntity.ok().build();
  }

  // 문서 삭제 ->
}
