package ImageHoster.controller;

import ImageHoster.model.Image;
import ImageHoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ImageService imageService;

    /**
     * Controller method called when the request pattern is of type '/'
     * and the incoming request is of 'GET' type
     * @param model Model to supply attributes ('images') used for rendering view ('index')
     * @return      'index.html' file which is the landing page of the application
     *              and displays all the images in the application
     */
    @RequestMapping("/")
    public String getAllImages(Model model) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "index";
    }
}