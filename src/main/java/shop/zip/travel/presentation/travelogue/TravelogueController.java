package shop.zip.travel.presentation.travelogue;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;

@RestController
@RequestMapping("/api/travelogues")
public class TravelogueController {

	private final TravelogueService travelogueService;

	public TravelogueController(TravelogueService travelogueService) {
		this.travelogueService = travelogueService;
	}

	@PostMapping
	public ResponseEntity<Long> create(
			@RequestBody @Valid TravelogueCreateReq createReq,
			@RequestParam Long memberId) {

		Long travelogueId = travelogueService.save(createReq, memberId);
		return ResponseEntity.ok(travelogueId);
	}

	@GetMapping("/search")
	public ResponseEntity<List<TravelogueSimpleRes>> search(
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "lastTravelogue", required = false) Long lastTravelogue,
			@RequestParam(name = "orderType") String orderType, @RequestParam(name = "size") int size) {
		List<TravelogueSimpleRes> travelogueSimpleResList = travelogueService.search(lastTravelogue,
				keyword, orderType, size);

		return ResponseEntity.ok(travelogueSimpleResList);
	}
}

