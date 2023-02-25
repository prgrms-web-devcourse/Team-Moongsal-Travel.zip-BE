package shop.zip.travel.domain.post.travelogue.dto.res;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public record CustomSlice<T> (
	List<T> content,
	Pageable pageable,
	int size,
	int number,
	int numberOfElements,
	boolean first,
	boolean last,
	boolean empty
){
	public static <T> CustomSlice<T> toDto(Slice<T> slice){
		return new CustomSlice<>(
			slice.getContent(),
			slice.getPageable(),
			slice.getSize(),
			slice.getNumber(),
			slice.getNumberOfElements(),
			slice.isFirst(),
			slice.isLast(),
			slice.isEmpty()
		);
	}
}
