package ImageHoster.controller;

import ImageHoster.model.Image;
import ImageHoster.model.Tag;
import ImageHoster.model.User;
import ImageHoster.service.ImageService;
import ImageHoster.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private TagService tagService;

    /**
     * Controller method called when the request pattern is of type 'images'
     * and also the incoming request is of 'GET' type
     * Displays all the images in the user home page after successful login
     * @param model Model to supply attributes ('images') used for rendering view ('images')
     * @return      'images.html' file to displays all the images in the user home page
     */
    @RequestMapping("images")
    public String getUserImages(Model model) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "images";
    }

    /**
     * Controller method called when the details of the specific image with corresponding id and title are to be displayed
     * Gets image's details from database and display the same on images/image page
     * @param id        Integer that represents Image Id of the image whose details need to be displayed
     * @param title     Image title of the image whose details need to be displayed
     * @param model     Model to supply attributes ('images', 'tags') used for rendering view ('images/images')
     * @return          'images/images.html' file to display image's details and it tags
     */
    @RequestMapping("/images/{id}/{title}")
    public String showImage(@PathVariable("id") Integer id, @PathVariable("title") String title, Model model) {
        Image image = imageService.getImage(id);
        model.addAttribute("image", image);
        model.addAttribute("tags", image.getTags());
        model.addAttribute("comments", image.getComments());
        return "images/image";
    }

    /**
     * Controller method called when the request pattern is of type '/images/upload'
     * and also the incoming request is of 'GET' type
     * @return  'images/upload.html' file where a user can upload an image
     */
    @RequestMapping("/images/upload")
    public String newImage() {
        return "images/upload";
    }

    /**
     * Controller method called when the request pattern is of type '/images/upload'
     * and also the incoming request is of 'POST' type
     * Receives all the details of the image that needs to be persisted in the database
     * @param file      MultipartFile that needs to be converted to Base64 before persisting the details in database
     * @param tags      String of all tags that needs to persisted in 'tags' table individually if not present in database.
     * @param newImage  Image object details sent by user from upload page
     * @param session   HttpSession to get logged-in user details
     * @return          Redirected to '/images' to display all the images uploaded
     * @throws IOException  in case of access errors
     */
    @RequestMapping(value = "/images/upload", method = RequestMethod.POST)
    public String createImage(@RequestParam("file") MultipartFile file, @RequestParam("tags") String tags,
                              Image newImage, HttpSession session) throws IOException {

        User user = (User) session.getAttribute("loggeduser");
        newImage.setUser(user);
        String uploadedImageData = convertUploadedFileToBase64(file);
        newImage.setImageFile(uploadedImageData);
        // Convert tags string to list of tags
        List<Tag> imageTags = findOrCreateTags(tags);
        newImage.setTags(imageTags);
        newImage.setDate(new Date());
        imageService.uploadImage(newImage);
        return "redirect:/images";
    }

    /**
     * Controller method called when the request pattern is of type '/editImage'
     * and also the incoming request is of 'GET' type
     * Fetches the details of the image that needs to be edited
     * @param imageId   Integer that represents image id
     * @param model     Model to supply attributes ('image', 'tags') used for rendering view ('images/edit')
     * @param session   HttpSession to get logged-in user details
     * @return          'images/edit.html' file where the image's details can be edited
     */
    @RequestMapping(value = "/editImage")
    public String editImage(@RequestParam("imageId") Integer imageId, Model model, HttpSession session) {
        Image image = imageService.getImage(imageId);
        User loggedUser = (User) session.getAttribute("loggeduser");
        //If the logged-in user has not posted the image selected to edit,
        //return back to images/image with the error message on the screen
        if (loggedUser.getId() != image.getUser().getId()) {
            String error = "Only the owner of the image can edit the image";
            model.addAttribute("editError", error);
            model.addAttribute("image", image);
            model.addAttribute("tags", image.getTags());
            model.addAttribute("comments", image.getComments());
            return "images/image";
        }

        // Convert the list of all the tags to a string containing all the tags separated by a comma
        String tags = convertTagsToString(image.getTags());
        model.addAttribute("image", image);
        model.addAttribute("tags", tags);
        return "images/edit";
    }

    /**
     * Controller method called when the request pattern is of type '/editImage'
     * and also the incoming request is of 'PUT' type
     * Updates the image details in database
     * @param file          MultipartFile that needs to be converted to Base64 before updating the details in database
     * @param imageId       Integer that represents image id
     * @param tags          String of all tags that needs to updated in 'tags' table individually if not present in database.
     * @param updatedImage  Image which has all the updated details for the image already persisted in database
     * @param session       HttpSession to get logged-in user details
     * @return              Redirects to '/images/{title} to display the updates values for the same image
     * @throws IOException  in case of access errors
     */
    @RequestMapping(value = "/editImage", method = RequestMethod.PUT)
    public String editImageSubmit(@RequestParam("file") MultipartFile file, @RequestParam("imageId") Integer imageId,
                                  @RequestParam("tags") String tags, Image updatedImage, HttpSession session) throws IOException {

        Image image = imageService.getImage(imageId);
        String updatedImageData = convertUploadedFileToBase64(file);
        List<Tag> imageTags = findOrCreateTags(tags);

        if (updatedImageData.isEmpty())
            updatedImage.setImageFile(image.getImageFile());
        else {
            updatedImage.setImageFile(updatedImageData);
        }

        updatedImage.setId(imageId);
        User user = (User) session.getAttribute("loggeduser");
        updatedImage.setUser(user);
        updatedImage.setTags(imageTags);
        updatedImage.setDate(new Date());

        imageService.updateImage(updatedImage);
        return "redirect:/images/" + imageId + "/" + updatedImage.getTitle();
    }


    /**
     * Controller method called when the request pattern is of type '/deleteImage'
     * and also the incoming request is of 'DELETE' type
     * Deletes the image from database corresponding to image id passed from the screen
     * @param imageId   Integer that represents image id to be deleted
     * @param model     Model to supply attributes ('image', 'tags') used for rendering view ('images/image')
     *                  in case the logged-in user is not the owner of the image
     * @param session   HttpSession to get logged-in user details
     * @return          Redirects to '/images' to display the available images in database
     */
    @RequestMapping(value = "/deleteImage", method = RequestMethod.DELETE)
    public String deleteImageSubmit(@RequestParam(name = "imageId") Integer imageId, Model model, HttpSession session) {
        Image image = imageService.getImage(imageId);
        User loggedUser = (User) session.getAttribute("loggeduser");
        //If the logged in user has not posted the image selected to delete,
        //return back to images/image with the error message on the screen
        if (loggedUser.getId() != image.getUser().getId()) {
            String error = "Only the owner of the image can delete the image";
            model.addAttribute("deleteError", error);
            model.addAttribute("image", image);
            model.addAttribute("tags", image.getTags());
            model.addAttribute("comments", image.getComments());
            return "images/image";
        }

        imageService.deleteImage(imageId);
        return "redirect:/images";
    }


    /**
     * Method to convert the image to Base64 format
     * @param file  MultipartFile that needs to be converted to Base64
     * @return      String that represents the encoded value of file
     * @throws IOException  in case of access errors
     */
    private String convertUploadedFileToBase64(MultipartFile file) throws IOException {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }

    /**
     * Converts the ‘tags’ string to a list of all the tags
     * and stores the tags in the database if they do not exist in the database.
     * @param tagNames  String that represents tags
     * @return          List<Tag>
     */
    private List<Tag> findOrCreateTags(String tagNames) {
        StringTokenizer st = new StringTokenizer(tagNames, ",");
        List<Tag> tags = new ArrayList<Tag>();

        while (st.hasMoreTokens()) {
            String tagName = st.nextToken().trim();
            Tag tag = tagService.getTagByName(tagName);

            if (tag == null) {
                Tag newTag = new Tag(tagName);
                tag = tagService.createTag(newTag);
            }
            tags.add(tag);
        }
        return tags;
    }


    /**
     * Converts the list of all tags to a single string containing all the tags separated by a comma
     * @param tags  List<Tag> that represents list of all tags for an image
     * @return      String that represents the tags of the image
     */
    private String convertTagsToString(List<Tag> tags) {
        StringBuilder tagString = new StringBuilder();

        for (int i = 0; i <= tags.size() - 2; i++) {
            tagString.append(tags.get(i).getName()).append(",");
        }

        if (!tags.isEmpty()) {
            Tag lastTag = tags.get(tags.size() - 1);
            tagString.append(lastTag.getName());
        }

        return tagString.toString();
    }
}
