package market.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Table(name = "distillery")
public class Distillery implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", insertable = false, updatable = false, nullable = false)
	private Long id;

	@Column(name = "title", nullable = false)
	@NotEmpty
	@Pattern(regexp = "^[^#$%^*()']*$")
	private String title;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "region_id", nullable = false)
	private Region region;

	@Column(name = "description")
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

}
