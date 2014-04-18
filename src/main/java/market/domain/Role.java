package market.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Роль пользователя.
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {
    
    enum Roles {
        ROLE_ADMIN,
        ROLE_STAFF,
        ROLE_USER
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany(mappedBy = "roles")
    private Set<UserAccount> users = new HashSet<>();

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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the users
     */
    public Set<UserAccount> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(Set<UserAccount> users) {
        this.users = users;
    }

}
