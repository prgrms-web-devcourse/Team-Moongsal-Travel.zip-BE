package shop.zip.travel.domain.post.travelog.entity.type;

public enum Transportation {

	CAR("자동차"),
	TAXI("택시"),
	BUS("버스"),
	SUBWAY("지하철"),
	TRAIN("기차"),
	SHIP("배"),
	PLANE("비행기"),
	WALK("도보"),
	BICYCLE("자전거");

	private String str;

	Transportation(String str) {
		this.str = str;
	}
}
