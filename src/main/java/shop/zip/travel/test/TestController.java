package shop.zip.travel.test;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/test")
@Transactional
public class TestController {

	private final DummyRepository dummyRepository;

	public TestController(DummyRepository dummyRepository) {
		this.dummyRepository = dummyRepository;
	}

	@GetMapping("/get")
	public ResponseEntity<DummyDTO> get(@RequestParam Long id){
		Dummy dummy = dummyRepository.findById(id)
			.orElseThrow(() -> {
				throw new IllegalArgumentException("해당하는 id의 리소스가 없습니다.");
			});
		return ResponseEntity.ok(new DummyDTO(dummy));
	}

	@PostMapping("/post")
	public ResponseEntity<Void> post(@RequestBody DummyDTO dummyDTO){
		Long id = dummyRepository.save(dummyDTO.toEntity()).getId();

		URI url = UriComponentsBuilder.fromUriString("/test/get?id={id}")
			.buildAndExpand(id)
			.toUri();

		return ResponseEntity.created(url).build();
	}

	@PutMapping("/put")
	public ResponseEntity<DummyDTO> put(@RequestBody DummyDTO dummyDTO, @RequestParam Long id){
		Dummy dummy = dummyRepository.findById(id)
			.orElseThrow(() -> {
				throw new IllegalArgumentException("해당하는 id의 리소스가 없습니다.");
			});

		dummy.update(dummyDTO.name(), dummyDTO.score());

		return ResponseEntity.ok(new DummyDTO(dummy));
	}

	@PatchMapping("/patch")
	public ResponseEntity<DummyDTO> patch(@RequestBody DummyDTO dummyDTO, @RequestParam Long id){
		Dummy dummy = dummyRepository.findById(id)
			.orElseThrow(() -> {
				throw new IllegalArgumentException("해당하는 id의 리소스가 없습니다.");
			});

		dummy.update(dummyDTO.name());

		return ResponseEntity.ok(new DummyDTO(dummy));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Void> delete(@RequestParam Long id){
		dummyRepository.deleteById(id);

		return ResponseEntity.ok().build();
	}

}
