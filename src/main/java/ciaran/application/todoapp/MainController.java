package ciaran.application.todoapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import ciaran.application.service.jwtUserService;
import java.util.HashMap;
import java.util.Optional;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/user")
public class MainController {
    @Autowired

    private UserRepository userRepository;

    @PostMapping(value = "/add", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public User addNewUserBody(@RequestBody User u) {
        String salt = BCrypt.gensalt();
        u.setPassword(BCrypt.hashpw(u.getPassword(), salt));
        return userRepository.save(u);
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/getUser")
    public ResponseEntity<Optional<User>> getUser(@RequestParam Integer id) {
        Optional<User> u = userRepository.findById(id);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping("/getUserFromName")
    public @ResponseBody User findUserByName(@RequestParam String name) {
        return userRepository.findByName(name);
    }

    @PostMapping(value = "/loginUser", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody String login(@RequestBody String request) throws JoseException {
        Gson gson = new Gson();
        User deSerializedJsonString = gson.fromJson(request, User.class);
        User loginUser = userRepository.findByEmail(deSerializedJsonString.getEmail());
        Map<String, String> map = new HashMap<>();
        if (BCrypt.checkpw(deSerializedJsonString.getPassword(), loginUser.getPassword())) {
            map.put("jwt", jwtUserService.createToken(loginUser.getEmail(), loginUser.getId()));
            return gson.toJson(map);
        } else {

            map.put("msg", "Token Invalid");
            return gson.toJson(map);
        }
    }
}
