package ImageHoster.service;

import ImageHoster.model.Tag;
import ImageHoster.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    /**
     * Method to get Tag object based on tag title
     * @param title String that represents tag title
     * @return      Tag object
     */
    public Tag getTagByName(String title) {
        return tagRepository.findTag(title);
    }

    /**
     * Method to persist Tag object details in the repository
     * @param tag   Tag object to be persisted in the database
     * @return  Tag object
     */
    public Tag createTag(Tag tag) {
        return tagRepository.createTag(tag);
    }
}
