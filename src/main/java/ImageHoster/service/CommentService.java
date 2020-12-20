package ImageHoster.service;

import ImageHoster.model.Comment;
import ImageHoster.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    /**
     * Method to persist Comment object in the database through repository
     * @param comment   Comment object to be persisted in the database
     */
    public void createComment(Comment comment) {
        commentRepository.createComment(comment);
    }

}
