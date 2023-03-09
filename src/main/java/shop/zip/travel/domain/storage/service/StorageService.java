package shop.zip.travel.domain.storage.service;

import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.storage.document.Place;
import shop.zip.travel.domain.storage.document.Storage;
import shop.zip.travel.domain.storage.dto.res.ScrapDetailRes;
import shop.zip.travel.domain.storage.dto.res.ScrapSimpleRes;
import shop.zip.travel.domain.storage.exception.StorageNotFoundException;
import shop.zip.travel.domain.storage.repository.StorageRepository;
import shop.zip.travel.global.error.ErrorCode;

@Service
public class StorageService {

  private final StorageRepository storageRepository;

  public StorageService(StorageRepository storageRepository) {
    this.storageRepository = storageRepository;
  }

  @Transactional(readOnly = true)
  public List<ScrapSimpleRes> findAllStorage(Long memberId) {
    List<Storage> allStorage = storageRepository.findByMemberId(memberId);
    List<ScrapSimpleRes> scrapSimpleResList = allStorage
        .stream()
        .map(ScrapSimpleRes::toDto)
        .collect(Collectors.toList());
    return scrapSimpleResList;
  }

  @Transactional(readOnly = true)
  public ScrapDetailRes findOneStorage(ObjectId objectId) {
    Storage storage = storageRepository
        .findById(objectId)
        .orElseThrow(() -> new StorageNotFoundException(ErrorCode.STORAGE_NOT_FOUND));
    ScrapDetailRes scrapDetailRes = ScrapDetailRes.toDto(storage);

    return scrapDetailRes;
  }
  @Transactional
  public void createStorage(Long memberId, String title) {
    Storage storage = new Storage(memberId, title);
    storageRepository.save(storage);
  }

  @Transactional
  public void addContent(ObjectId storageObjectId, String content, Long postId) {
    Storage storage = storageRepository
        .findById(storageObjectId)
        .orElseThrow(() -> new StorageNotFoundException(ErrorCode.STORAGE_NOT_FOUND));
    storage.getContents().add(new Place(content, postId));
    storageRepository.save(storage);
  }

  @Transactional
  public void deleteOneStorage(ObjectId objectId) {
    storageRepository.deleteById(objectId);
  }

  @Transactional
  public void deleteScrapInStorage(ObjectId storageObjectId, ObjectId scrapObjectId) {
    Storage storage = storageRepository
        .findById(storageObjectId)
        .orElseThrow(() -> new StorageNotFoundException(ErrorCode.STORAGE_NOT_FOUND));

    storage.getContents().removeIf(place -> place.getScrapObjectId().equals(scrapObjectId.toString()));
    storageRepository.save(storage);
  }
}
