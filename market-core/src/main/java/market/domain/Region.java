package market.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "region")
public class Region implements Serializable {
	public static final Region NULL;
	private static final long serialVersionUID = 5413261502059862627L;

	static {
		NULL = new Region.Builder()
			.setId(0L)
			.setName("null region")
			.build();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", insertable = false, updatable = false, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	@NotEmpty
	@Pattern(regexp = "^[^#$%^&*()']*$")
	private String name;

	@Column(name = "subtitle")
	@Pattern(regexp = "^[^#$%^*()']*$")
	private String subtitle;

	@Column(name = "description")
	private String description;

	@Column(name = "color")
	@Pattern(regexp = "^(a-z|A-Z|0-9-)*[^#$%^&*()']*$")
	private String color;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Region region = (Region) o;
		return Objects.equals(id, region.id) &&
			Objects.equals(name, region.name) &&
			Objects.equals(subtitle, region.subtitle) &&
			Objects.equals(description, region.description) &&
			Objects.equals(color, region.color);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, subtitle, description, color);
	}

	public static class Builder {
		private Long id;
		private String name;
		private String subtitle;
		private String description;
		private String color;

		public Builder() {
		}

		public Builder(Region region) {
			id = region.id;
			name = region.name;
			subtitle = region.subtitle;
			description = region.description;
			color = region.color;
		}

		public Region build() {
			Region region = new Region();
			region.id = id;
			region.name = name;
			region.subtitle = subtitle;
			region.description = description;
			region.color = color;
			return region;
		}

		public Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setSubtitle(String subtitle) {
			this.subtitle = subtitle;
			return this;
		}

		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder setColor(String color) {
			this.color = color;
			return this;
		}
	}
}
