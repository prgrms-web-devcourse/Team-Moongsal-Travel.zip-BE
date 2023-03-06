package shop.zip.travel.domain.scrap.service;

import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.scrap.document.Place;
import shop.zip.travel.domain.scrap.document.Scrap;
import shop.zip.travel.domain.scrap.dto.res.ScrapDetailRes;
import shop.zip.travel.domain.scrap.dto.res.ScrapSimpleRes;
import shop.zip.travel.domain.scrap.exception.ScrapDocumentNotFoundException;
import shop.zip.travel.domain.scrap.repository.ScrapRepository;
import shop.zip.travel.global.error.ErrorCode;

@Service
public class ScrapService {

  private final ScrapRepository scrapRepository;

  public ScrapService(ScrapRepository scrapRepository) {
    this.scrapRepository = scrapRepository;
  }

  @Transactional
  public List<ScrapSimpleRes> findAllStorage(Long memberId) {
    List<Scrap> allStorage = scrapRepository.findByMemberId(memberId);
    List<ScrapSimpleRes> scrapSimpleResList = allStorage
        .stream()
        .map(ScrapSimpleRes::toDto)
        .collect(Collectors.toList());
    return scrapSimpleResList;
  }

  @Transactional
  public ScrapDetailRes findOneStorage(ObjectId objectId) {
    Scrap scrap = scrapRepository
        .findById(objectId)
        .orElseThrow(() -> new ScrapDocumentNotFoundException(ErrorCode.SCRAP_DOCUMENT_NOT_FOUND));
    ScrapDetailRes scrapDetailRes = ScrapDetailRes.toDto(scrap);

    return scrapDetailRes;
  }
  @Transactional
  public void createStorage(Long memberId, String title) {
    Scrap scrap = new Scrap(memberId, title);
    scrapRepository.save(scrap);
  }

  @Transactional
  public void addContent(ObjectId storageObjectId, String content) {
    Scrap scrap = scrapRepository
        .findById(storageObjectId)
        .orElseThrow(() -> new ScrapDocumentNotFoundException(ErrorCode.SCRAP_DOCUMENT_NOT_FOUND));
    scrap.getContents().add(new Place(content));
    scrapRepository.save(scrap);
  }

  @Transactional
  public void deleteOneStorage(ObjectId objectId) {
    scrapRepository.deleteById(objectId);
  }

  @Transactional
  public void deleteScrapInStorage(ObjectId storageObjectId, ObjectId scrapObjectId) {
    Scrap scrap = scrapRepository
        .findById(storageObjectId)
        .orElseThrow(() -> new ScrapDocumentNotFoundException(ErrorCode.SCRAP_DOCUMENT_NOT_FOUND));

    scrap.getContents().removeIf(place -> place.getObjectId().equals(scrapObjectId.toString()));
    scrapRepository.save(scrap);
  }
}
