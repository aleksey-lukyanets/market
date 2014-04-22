package market.domain;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Товар.
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "distillery_id", nullable = false)
    private Distillery distillery;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private Storage storage;

    @Column(name = "name", nullable = false)
    @NotEmpty
    @Pattern(regexp = "^[^#$%^&*()']*$")
    private String name;

    @Column(name = "price", nullable = false)
    @NotNull
    private Integer price;

    @Column(name = "description")
    private String description;

    @Column(name = "volume")
    @NotNull
    private Integer volume;

    @Column(name = "alcohol")
    @NotNull
    @Min(value = 1)
    @Max(value = 96)
    private Float alcohol;

    @Column(name = "age")
    @Max(value = 2000)
    private Integer age;

    public Product() {
    }

    public Product(Distillery distillery, String name, int price) {
        this.distillery = distillery;
        this.name = name;
        this.price = price;
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
     * @return the price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Integer price) {
        this.price = price;
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
     * @return the volume
     */
    public Integer getVolume() {
        return volume;
    }

    /**
     * @param volume the volume to set
     */
    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    /**
     * @return the alcohol
     */
    public Float getAlcohol() {
        return alcohol;
    }

    /**
     * @param alcohol the alcohol to set
     */
    public void setAlcohol(Float alcohol) {
        this.alcohol = alcohol;
    }

    /**
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return the storage
     */
    public Storage getStorage() {
        return storage;
    }

    /**
     * @param storage the storage to set
     */
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    /**
     * @return the distillery
     */
    public Distillery getDistillery() {
        return distillery;
    }

    /**
     * @param distillery the distillery to set
     */
    public void setDistillery(Distillery distillery) {
        this.distillery = distillery;
    }
}
