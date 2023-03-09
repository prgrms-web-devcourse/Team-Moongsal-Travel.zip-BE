package shop.zip.travel.domain.post.travelogue.dto.res;

public record LikeResultRes(
    boolean isAdded,
    boolean isCanceled
) {

  public static LikeResultRes toDto(boolean isAdded, boolean isCanceled) {
    return new LikeResultRes(
        isAdded,
        isCanceled
    );
  }
}
