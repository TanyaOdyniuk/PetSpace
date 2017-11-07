package com.netcracker.controller;

import com.netcracker.model.StubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Odyniuk on 06/11/2017.
 */
@RestController
@RequestMapping("/restcontroller")
public class StubRestController {
    @Autowired
    private List<StubUser> users;
    private StubUser findUserById(Integer id){
        for (StubUser u:users) {
            if(u.getId().equals(id)){
                return u;
            }
        }
        return new StubUser();
    }
    private int getIndexById(Integer id){
        int res = -1;
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getId().equals(id)){
                res = i;
            }
        }
        return res;
    }
    @GetMapping
    public List<StubUser> getUsers(){
        return users;
    }

    @GetMapping("/{id}")
    public StubUser getUserById(@PathVariable("id") Integer id){
        return findUserById(id);
    }

    @PostMapping
    public void addUser(@RequestBody StubUser user){
        users.add(user);
    }

    @PutMapping("/{id}")
    public void editUserById(@PathVariable("id") Integer id, @RequestBody StubUser stubUser){
        int index = getIndexById(id);
        if(index >= 0){
            users.set(index, stubUser);
        }
        else{
            addUser(stubUser);
        }

    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") Integer id){
        users.remove(findUserById(id));
    }
}
