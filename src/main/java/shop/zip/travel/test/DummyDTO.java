package shop.zip.travel.test;

public record DummyDTO(String name, int score) {

	public Dummy toEntity(){
		return new Dummy(this.name, this.score);
	}

	public DummyDTO(Dummy dummy){
		this(dummy.getName(), dummy.getScore());
	}
}
