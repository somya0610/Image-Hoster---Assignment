package ImageHoster.model;

import javax.persistence.*;
import java.util.List;

/**
 * The Tag class is mapped to table 'tags' in database
 * All the columns are mapped to its respective attributes of the class
 */

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String name;

    // Many to Many mapping, a new table will be generated containing the two columns
    // both referencing to the primary key of both the tables ('images', 'tags')
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    private List<Image> images;

    public Tag() {
    }

    public Tag(String tagName) {
        this.name = tagName;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
