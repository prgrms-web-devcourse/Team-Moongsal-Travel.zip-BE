package shop.zip.travel.presentation.subTravelogue;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueCreateReq;
import shop.zip.travel.domain.post.subTravelogue.dto.res.SubTravelogueCreateRes;
import shop.zip.travel.domain.post.subTravelogue.service.SubTravelogueService;

@RestController
@RequestMapping("/api/travelogues")
public class SubTravelogueController {

    private final SubTravelogueService subTravelogueService;

    public SubTravelogueController(SubTravelogueService subTravelogueService) {
        this.subTravelogueService = subTravelogueService;
    }

    @PostMapping("/{travelogueId}/subTravelogues")
    public ResponseEntity<SubTravelogueCreateRes> create(
        @RequestBody @Valid SubTravelogueCreateReq createReq,
        @PathVariable Long travelogueId) {

        SubTravelogueCreateRes subTravelogueCreateRes = subTravelogueService.save(createReq,
            travelogueId);
        return ResponseEntity.ok(subTravelogueCreateRes);
    }
}
