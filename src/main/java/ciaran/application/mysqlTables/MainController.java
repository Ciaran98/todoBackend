package ciaran.application.mysqlTables;

import java.lang.StackWalker.Option;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class MainController {
    @Autowired

    private UserRepository userRepository;

    @PostMapping(value = "/add", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<User> addNewUserBody(@RequestBody User u) {
        User persistedUser = userRepository.save(u);
        return ResponseEntity.created(URI.create(String.format("/User/%s", u.getName()))).body(persistedUser);
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/getUser")
    public @ResponseBody Optional<User> getUser(@RequestParam Integer id) {
        return userRepository.findById(id);
    }
}
