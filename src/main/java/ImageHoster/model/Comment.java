package ImageHoster.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * The Comment class is mapped to table 'comments' in database
 * All the columns are mapped to its respective attributes of the class
 */

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "created_date")
    private LocalDate createdDate;

    // The 'comments' table is mapped to 'users' table with Many:One mapping
    // One user can post multiple comments but one comment should belong to one user
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // The 'comments' table is mapped to 'iamges' table with Many:One mapping
    // One image can have multiple comments but one comment can only belong to one image
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
