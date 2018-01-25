package com.netcracker.controller.request;

import com.netcracker.model.request.FriendRequest;
import com.netcracker.model.status.Status;
import com.netcracker.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @GetMapping("/{id}")
    public List<FriendRequest> getProfileRequests(@PathVariable("id") BigInteger id){
        return requestService.getProfileRequests(id);
    }

    @PostMapping("/update")
    public void updateRequestStatus(@RequestBody FriendRequest request){
        requestService.updateRequest(request);
    }

    @PostMapping("/send")
    public void sendRequest(@RequestBody FriendRequest request){
        requestService.sendRequest(request);
    }

    @PostMapping("/confirm")
    public void confirmRequest(@RequestBody FriendRequest request){
        requestService.confirmRequest(request);
    }

    @PostMapping("/decline")
    public void declineRequest(@RequestBody FriendRequest request){
        requestService.declineRequest(request);
    }

    @GetMapping("{currentProfileId}/delete/{profileIdToDelete}")
    public void deleteFriendshipStatus(@PathVariable("currentProfileId") BigInteger currentProfileId,
                                       @PathVariable("profileIdToDelete") BigInteger profileIdToDelete) {
        requestService.deleteFriendshipStatus(currentProfileId, profileIdToDelete);
    }

    @PostMapping("/blacklist")
    public void toBlaclistRequest(@RequestBody FriendRequest request){
        //requestService.sendRequest(request);
    }

    @GetMapping("{currentProfileId}/check/{profileIdToCheck}")
    public Status checkProfilesStatus(@PathVariable("currentProfileId") BigInteger currentProfileId,
                                      @PathVariable("profileIdToCheck") BigInteger profileIdToCheck){
        return requestService.getProfilesStatus(currentProfileId, profileIdToCheck);
    }
}
