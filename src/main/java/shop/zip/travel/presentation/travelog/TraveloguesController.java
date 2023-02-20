package shop.zip.travel.presentation.travelog;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.travelog.dto.response.TravelogueFindAllResponseDto;
import shop.zip.travel.domain.travelog.service.TravelogueService;

@RestController
@RequestMapping("/api/travelogues")
public class TraveloguesController {

  private final TravelogueService travelogueService;

  public TraveloguesController(TravelogueService travelogueService) {
    this.travelogueService = travelogueService;
  }

  @GetMapping
  public ResponseEntity<Slice<TravelogueFindAllResponseDto>> findAll(
      @RequestParam(name = "page") int page,
      @RequestParam(name = "size") int size) {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Direction.DESC, "createDate"));

    Slice<TravelogueFindAllResponseDto> travelogueFindAllResponseDtos = travelogueService.findAll(
        pageRequest);

    return ResponseEntity.ok(travelogueFindAllResponseDtos);
  }
}
