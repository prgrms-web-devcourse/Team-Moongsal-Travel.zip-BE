package shop.zip.travel.presentation.travelogue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import shop.zip.travel.domain.post.travelogue.dto.res.CustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;

@RestController
@RequestMapping("/api/travelogues")
public class TravelogueController {

	private final TravelogueService travelogueService;

	public TravelogueController(TravelogueService travelogueService) {
		this.travelogueService = travelogueService;
	}

	@GetMapping
	public ResponseEntity<CustomSlice<TravelogueSimpleRes>> getAll(@RequestParam int size, @RequestParam int page){
		CustomSlice<TravelogueSimpleRes> travelogueSimpleRes = travelogueService.getTravelogues(page,size);

		return ResponseEntity.ok(travelogueSimpleRes);
	}
}
