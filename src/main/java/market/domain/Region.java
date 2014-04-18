package market.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Регион.
 */
@Entity
@Table(name = "region")
public class Region implements Serializable {

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
    @Pattern(regexp="^[^#$%^&*()']*$")
    private String name;

    @Column(name = "subtitle")
    @Pattern(regexp="^[^#$%^*()']*$")
    private String subtitle;

    @Column(name = "description")
    private String description;

    @Column(name = "color")
    @Pattern(regexp="^(a-z|A-Z|0-9-)*[^#$%^&*()']*$")
    private String color;

    public Region() {
    }

    public Region(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * @param subtitle the subtitle to set
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }
}
