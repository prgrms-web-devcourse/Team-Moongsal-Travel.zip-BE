package shop.zip.travel.domain.scrap.repository;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.zip.travel.domain.scrap.document.Scrap;

@SpringBootTest
class ScrapRepositoryTest {

  @Autowired
  ScrapRepository scrapRepository;

//  @Test
//  @DisplayName("제대로 저장되는지 확인")
//  void save() {
//    List<Content> list = new ArrayList<>();
//    list.add(new Place("두오모 성당"));
//    list.add(new Image("http://duomo"));
//    list.add(new Place("배키오 다리"));
//    list.add(new Image("http://backio"));
//    MyScrap myScrap = new MyScrap(1L, "피렌체 여행~", list);
//
//    myScrapRepository.save(myScrap);
//
//  }

  @Test
  void findById() {
    List<Scrap> scraps = scrapRepository.findByMemberId(1L);
    Optional<Scrap> byId = scrapRepository.findById(scraps.get(1).getId());
    System.out.println(byId.get().getTitle());
  }

  @Test
  @DisplayName("member id 로 저장된 문서들 조회")
  void find() {
    List<Scrap> scraps = scrapRepository.findByMemberId(1L);
    for (Scrap m : scraps) {
      System.out.println(m.getId());
      System.out.println(m.getMemberId());
      System.out.println(m.getTitle());
      System.out.println(m.getContents());
      System.out.println(m.getContents().size());
    }
  }

  @Test
  @DisplayName("제목만으로 생성")
  void createOnlyTitle() {
    Scrap scrap = new Scrap(2L, "대한민국 여행!!");
    scrapRepository.save(scrap);
  }


}