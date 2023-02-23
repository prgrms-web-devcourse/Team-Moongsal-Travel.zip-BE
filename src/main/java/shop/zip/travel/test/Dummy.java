package shop.zip.travel.test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Dummy {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "score", nullable = false)
	private int score;

	protected Dummy() {
	}

	public Dummy(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public void update(String name){
		if(name.isBlank()){
			throw new IllegalArgumentException("name은 빈 값일 수 없습니다.");
		}
		this.name = name;
	}

	public void update(String name, int score){
		this.update(name);
		if(score < 0){
			throw new IllegalArgumentException("score는 음수일 수 없습니다.");
		}
		this.
			score = score;
	}
}
