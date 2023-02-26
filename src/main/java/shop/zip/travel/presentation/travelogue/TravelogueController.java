package shop.zip.travel.presentation.travelogue;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueDetailRes;
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

    @GetMapping("/{travelogueId}")
    public ResponseEntity<TravelogueDetailRes> get(@PathVariable Long travelogueId) {
        TravelogueDetailRes travelogueDetail = travelogueService.getTravelogueDetail(travelogueId);
        return ResponseEntity.ok(travelogueDetail);
    }

}

