package market.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.*;

@Entity
@Table(name = "region")
public class Region implements Serializable {
	private static final long serialVersionUID = 5413261502059862627L;

	public static final Region NULL;
	static {
		NULL = new Region();
		NULL.setId(Long.valueOf(0));
		NULL.name = "null region";
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

	public Region() {
	}

	public Region(String name) {
		this.name = name;
	}

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
}
