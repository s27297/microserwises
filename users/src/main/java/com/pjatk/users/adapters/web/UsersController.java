package com.pjatk.users.adapters.web;

import com.pjatk.users.adapters.web.contract.users.*;
import com.pjatk.users.application.exceptions.NotFoundException;
import com.pjatk.users.core.port.in.commands.UserCommand;
import com.pjatk.users.core.port.in.queries.UsersQuery;
import com.pjatk.users.core.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UsersQuery usersQuery;
    private final UserCommand userCommand;

    @GetMapping("")
    public List<UserDto> getUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return usersQuery.getUsers(PageRequest.of(page, size))
                .stream().map(UserDto::from).toList();
    }

    @GetMapping("/logins")
    public List<UserLoginDto> getUsersLogins(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return usersQuery.getUsersLogins(PageRequest.of(page, size))
                .stream().map(user -> UserLoginDto.from(user)).toList();
    }

    @GetMapping("/prefix")
    public List<UserLoginDto> getUsersByPrefix(
            @RequestParam(name = "prefix", defaultValue = "") String prefix,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return usersQuery.findByLoginPrefix(prefix,PageRequest.of(page, size))
                .stream().map(UserLoginDto::from).toList();
    }
    @GetMapping("/prefix/full")
    public List<FullUserDto> getFullUsersByPrefix(
            @RequestParam(name = "prefix", defaultValue = "") String prefix,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return usersQuery.findByLoginPrefix(prefix,PageRequest.of(page, size))
                .stream().map(FullUserDto::from).toList();
    }
    @GetMapping("/{id}")
    public FullUserDto getUserById(
            @PathVariable(name = "id") String id
    ) {
        Optional<User> user = usersQuery.getUserById(id);
        return user.map(FullUserDto::from).orElseThrow(()->
                new NotFoundException("user not found"));
    }

    @PostMapping()
    public ResponseEntity<String> saveUser(@RequestBody UserDto user
    ) {
        String id=userCommand.save(UserDto.to(user));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FullUserDto> updateUser(
            @PathVariable(name="id") String id,
            @RequestBody UserDto user
    ) {
       User userResponse=userCommand.update(id,UserDto.to(user));
       if(userResponse==null) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
        return new ResponseEntity<>(FullUserDto.from(userResponse), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable(name="id") String id
    ) {
      boolean d= userCommand.delete(id);
      if(d)
        return new ResponseEntity<>("deleted", HttpStatus.OK);
      return new ResponseEntity<>("not found",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/sprawnosc")
    public ResponseEntity<String> deleteSprawnosc(
            @RequestParam(name="id") String id,
            @RequestParam(name="source_id") Integer source_id
    ) {
        String response= userCommand.deleteSprawnosc(id,source_id);
        if(Objects.equals(response, "sprawnosc deleted"))
            return new ResponseEntity<>(response, HttpStatus.OK);
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/payment")
    public ResponseEntity<String> deletePayment(
            @RequestParam(name="id") String id,
            @RequestParam(name="source_id") Integer source_id
    ) {
        String response= userCommand.deletePayment(id,source_id);
        if(Objects.equals(response, "payment deleted"))
            return new ResponseEntity<>(response, HttpStatus.OK);
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @PostMapping("/payment/{id}")
    public ResponseEntity<String> addPayment(
            @PathVariable(name="id") String id,
            @RequestBody PaymentDto paymentDto
    ) {
        String response= userCommand.addPayment(id,PaymentDto.to(paymentDto));
        if(Objects.equals(response, "payment added"))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else if(Objects.equals(response, "source_id must be unique"))
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @PostMapping("/sprawnosc/{id}")
    public ResponseEntity<String> addSprawnosc(
            @PathVariable(name="id") String id,
            @RequestBody SprawnoscDto sprawnoscDto
    ) {
        String response= userCommand.addSprawnosc(id,SprawnoscDto.to(sprawnoscDto));
        if(Objects.equals(response, "sprawnosc added"))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else if(Objects.equals(response, "source_id must be unique"))
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @PutMapping("/sprawnosc/{id}")
    public ResponseEntity<String> updateSprawnosc(
            @PathVariable(name="id") String id,
            @RequestBody SprawnoscDto sprawnoscDto
    ) {
        String answer=userCommand.updateSprawnosc(id,SprawnoscDto.to(sprawnoscDto));
        if(Objects.equals(answer, "sprawnosc updated")) {
            return new ResponseEntity<>(answer,HttpStatus.OK);
        }
        return new ResponseEntity<>(answer, HttpStatus.NOT_FOUND);
    }
    @PutMapping("/payment/{id}")
    public ResponseEntity<String> updatePayment(
            @PathVariable(name="id") String id,
            @RequestBody PaymentDto paymentDto
    ) {
        String answer=userCommand.updatePayment(id,PaymentDto.to(paymentDto));
        if(Objects.equals(answer, "payment updated")) {
            return new ResponseEntity<>(answer,HttpStatus.OK);
        }
        return new ResponseEntity<>(answer, HttpStatus.NOT_FOUND);
    }

}
