package ImageHoster.service;

import ImageHoster.model.Image;
import ImageHoster.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    /**
     * Method to get list of all images from repository
     * @return  List<Image>
     */
    public List<Image> getAllImages() {
        return imageRepository.getAllImages();
    }


    /**
     * Method to persist Image object in the database through repository
     * @param image Image object that needs to be persisted in the database
     */
    public void uploadImage(Image image) {
        imageRepository.uploadImage(image);
    }


    /**
     * Method to get image details based on image title from repository
     * @param title String that represents image title
     * @return      Image object
     */
    public Image getImageByTitle(String title) {
        return imageRepository.getImageByTitle(title);
    }

    /**
     * Method to get image details based on image Id from repository
     * @param imageId   Integer that represents image id
     * @return          Image object
     */
    public Image getImage(Integer imageId) {
        return imageRepository.getImage(imageId);
    }

    /**
     * Method to update image details in the repository
     * @param updatedImage Image object that has updated values
     */
    public void updateImage(Image updatedImage) {
        imageRepository.updateImage(updatedImage);
    }

    /**
     * Method to delete image details in the repository based on image id
     * @param imageId   Integer that represents image id
     */
    public void deleteImage(Integer imageId) {
        imageRepository.deleteImage(imageId);
    }

}
