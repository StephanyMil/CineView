package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("cine-view/movies")
public class MovieController {

    @Autowired
    MovieRepository movieRepository;

    
}
