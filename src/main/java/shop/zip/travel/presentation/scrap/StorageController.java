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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.storage.dto.req.ScrapCreateReq;
import shop.zip.travel.domain.storage.dto.req.StorageCreateReq;
import shop.zip.travel.domain.storage.dto.res.ScrapDetailRes;
import shop.zip.travel.domain.storage.dto.res.ScrapListRes;
import shop.zip.travel.domain.storage.dto.res.ScrapSimpleRes;
import shop.zip.travel.domain.storage.service.StorageService;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

  private final StorageService storageService;

  public StorageController(StorageService storageService) {
    this.storageService = storageService;
  }

  @GetMapping
  public ResponseEntity<ScrapListRes> getAllStorage(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    List<ScrapSimpleRes> allStorageList = storageService.findAllStorage(userPrincipal.getUserId());
    return ResponseEntity.ok(new ScrapListRes(allStorageList));
  }

  @GetMapping("/{storageObjectId}")
  public ResponseEntity<ScrapDetailRes> getOneStorage(@PathVariable ObjectId storageObjectId) {
    ScrapDetailRes scrapDetailRes = storageService.findOneStorage(storageObjectId);
    return ResponseEntity.ok(scrapDetailRes);
  }

  @PostMapping
  public ResponseEntity<Void> createStorage(@RequestBody StorageCreateReq storageCreateReq,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    Long memberId = userPrincipal.getUserId();
    storageService.createStorage(memberId, storageCreateReq.title());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/scrap")
  public ResponseEntity<Void> addScrapToStorage(@RequestBody ScrapCreateReq scrapCreateReq) {
    storageService.addContent(new ObjectId(scrapCreateReq.storageObjectId())
        , scrapCreateReq.content(), scrapCreateReq.postId());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{storageObjectId}")
  public ResponseEntity<Void> deleteStorage(@PathVariable ObjectId storageObjectId) {
    storageService.deleteOneStorage(storageObjectId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{storageObjectId}/scrap/{scrapObjectId}")
  public ResponseEntity<Void> deleteScrapInStorage(@PathVariable ObjectId storageObjectId,
      @PathVariable ObjectId scrapObjectId) {
    storageService.deleteScrapInStorage(storageObjectId, scrapObjectId);
    return ResponseEntity.ok().build();
  }
}
