package shop.zip.travel.domain.scrap.dto.req;

import org.bson.types.ObjectId;

public record ScrapCreateReq(ObjectId objectId, String content) {

}
