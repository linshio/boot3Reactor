package cn.linshio.controller;

import cn.linshio.entity.TAuthor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author linshio
 * @create 2024/6/5 11:23
 */
@RestController
public class AuthorController {

    @GetMapping("/author")
    public Flux<TAuthor> author(){
        return null;
    }
}
