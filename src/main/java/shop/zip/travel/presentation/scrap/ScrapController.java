package shop.zip.travel.presentation.scrap;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @GetMapping("/api/storage")
  public ResponseEntity<List<ScrapSimpleRes>> getAllStorage(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    List<ScrapSimpleRes> allStorageList = scrapService.findAllStorage(userPrincipal.getUserId());
    return ResponseEntity.ok(allStorageList);
  }

  @GetMapping("/api/storage/{storageObjectId}")
  public ResponseEntity<ScrapDetailRes> getOneStorage(@PathVariable ObjectId storageObjectId) {
    ScrapDetailRes scrapDetailRes = scrapService.findOneStorage(storageObjectId);
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
  public ResponseEntity<Void> addMyScrapToStorage(@RequestBody ScrapCreateReq scrapCreateReq) {
    scrapService.addContent(scrapCreateReq.storageObjectId(), scrapCreateReq.content());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/api/storage/{storageObjectId}")
  public ResponseEntity<Void> deleteStorage(@PathVariable ObjectId storageObjectId) {
    scrapService.deleteOneStorage(storageObjectId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/api/storage/{storageObjectId}/{scrapObjectId}")
  public ResponseEntity<Void> deleteScrapInStorage(@PathVariable ObjectId storageObjectId,
      @PathVariable ObjectId scrapObjectId) {
    scrapService.deleteScrapInStorage(storageObjectId, scrapObjectId);
    return ResponseEntity.ok().build();
  }
}
