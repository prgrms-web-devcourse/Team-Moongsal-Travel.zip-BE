package shop.zip.travel.presentation.subTravelogue;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.post.subTravelogue.dto.SubTravelogueCreateReq;
import shop.zip.travel.domain.post.subTravelogue.service.SubTravelogueService;

@RestController
@RequestMapping("/api/subTravelogues")
public class SubTravelogueController {

    private final SubTravelogueService subTravelogueService;

    public SubTravelogueController(SubTravelogueService subTravelogueService) {
        this.subTravelogueService = subTravelogueService;
    }

    @PostMapping("/{travelogueId}")
    public ResponseEntity<Long> create(
        @RequestBody @Valid SubTravelogueCreateReq createReq,
        @PathVariable Long travelogueId) {

        Long subTravelogueId = subTravelogueService.save(createReq, travelogueId);
        return ResponseEntity.ok(subTravelogueId);
    }

}
