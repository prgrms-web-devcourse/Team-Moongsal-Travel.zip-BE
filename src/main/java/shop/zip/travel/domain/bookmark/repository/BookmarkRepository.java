package shop.zip.travel.domain.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.zip.travel.domain.bookmark.entity.Bookmark;
import shop.zip.travel.domain.bookmark.repository.querydsl.BookmarkRepositoryQuerydsl;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>,
  BookmarkRepositoryQuerydsl {

}
