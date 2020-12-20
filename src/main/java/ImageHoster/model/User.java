package ImageHoster.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The User class is mapped to table 'users' in database
 * All the columns are mapped to its respective attributes of the class
 */

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    // One to One mapping
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // Below annotation indicates that the name of the column in 'users' table referring the primary key in
    // 'user_profile' table will be 'profile_id'
    @JoinColumn(name = "profile_id")
    private UserProfile profile;

    // One to Many mapping: the 'users' table is referenced by the 'images' table
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    //One to One mapping: the 'users' table is referenced by the 'comments' table
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> comments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

