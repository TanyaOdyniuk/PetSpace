package com.netcracker.service.request;

import com.netcracker.model.request.FriendRequest;
import com.netcracker.model.status.Status;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface RequestService {

    List<FriendRequest> getProfileRequests(BigInteger profileId);

    void updateRequest(FriendRequest request);

    void sendRequest(FriendRequest request);

    void confirmRequest(FriendRequest request);

    void declineRequest(FriendRequest request);

    void deleteFriendshipStatus(BigInteger profileId, BigInteger profileIdToDelete);

    Status getProfilesStatus(BigInteger profileId, BigInteger profileIdToCheck);
}
