package com.netcracker.service;

import com.netcracker.model.StubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StubService {
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

    public List<StubUser> getUsers(){
        return users;
    }

    public StubUser getUserById(Integer id){
        return findUserById(id);
    }

    public void addUser(StubUser user){
        users.add(user);
    }

    public void editUserById(Integer id, StubUser stubUser){
        int index = getIndexById(id);
        if(index >= 0){
            users.set(index, stubUser);
        }
        else{
            addUser(stubUser);
        }

    }

    public void deleteUserById(Integer id){
        users.remove(findUserById(id));
    }
}
