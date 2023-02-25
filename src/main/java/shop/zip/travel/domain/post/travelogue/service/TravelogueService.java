package shop.zip.travel.domain.post.travelogue.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.res.CustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;

@Service
@Transactional(readOnly = true)
public class TravelogueService {

	private final TravelogueRepository travelogueRepository;

	public TravelogueService(TravelogueRepository travelogueRepository) {
		this.travelogueRepository = travelogueRepository;
	}

	public CustomSlice<TravelogueSimpleRes> getTravelogues(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"));

		Slice<TravelogueSimple> travelogues = travelogueRepository.findAllBySlice(pageRequest);

		return CustomSlice.toDto(
			travelogues.map(TravelogueSimpleRes::toDto)
		);
	}
}
