package market.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class DistilleryDTO extends RepresentationModel<DistilleryDTO> {

	private Long id;
	private String title;
	private String region;
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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DistilleryDTO that = (DistilleryDTO) o;
		return Objects.equals(id, that.id) &&
			Objects.equals(title, that.title) &&
			Objects.equals(region, that.region) &&
			Objects.equals(description, that.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, region, description);
	}

	@Override
	public String toString() {
		return "DistilleryDTO{" +
			"id=" + id +
			", title='" + title + '\'' +
			", region='" + region + '\'' +
			", description='" + description + '\'' +
			'}';
	}
}
