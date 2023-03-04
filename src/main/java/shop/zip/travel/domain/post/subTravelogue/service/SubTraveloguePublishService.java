package shop.zip.travel.domain.post.subTravelogue.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.exception.InvalidPublishTravelogueException;
import shop.zip.travel.global.error.ErrorCode;

@Service
@Transactional(readOnly = true)
public class SubTraveloguePublishService {

  public void verifySubTravelogues(List<SubTravelogue> subTravelogues) {
    subTravelogues.forEach(subTravelogue -> {
      verifyPublish(subTravelogue);
      verifyPublish(subTravelogue.getAddresses());
    });
  }

  private void verifyPublish(SubTravelogue subTravelogue) {
    if (subTravelogue.cannotPublish()) {
      throw new InvalidPublishTravelogueException(ErrorCode.CANNOT_PUBLISH_TRAVELOGUE);
    }
  }

  private void verifyPublish(List<Address> requestAddresses) {
    requestAddresses.forEach(requestAddress -> {
      if (requestAddress.cannotPublish()
      ) {
        throw new InvalidPublishTravelogueException(ErrorCode.CANNOT_PUBLISH_TRAVELOGUE);
      }
    });
  }
}
