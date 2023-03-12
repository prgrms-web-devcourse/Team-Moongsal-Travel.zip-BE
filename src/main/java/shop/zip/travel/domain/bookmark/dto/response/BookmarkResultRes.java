package shop.zip.travel.domain.bookmark.dto.response;

public record BookmarkResultRes(
    boolean isAdded,
    boolean isCanceled
) {

  public static BookmarkResultRes toDto(boolean isAdded, boolean isCanceled) {
    return new BookmarkResultRes(
        isAdded,
        isCanceled
    );
  }
}
