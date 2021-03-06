package com.netcracker.service.request.impl;

import com.netcracker.dao.manager.query.Query;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.request.FriendRequest;
import com.netcracker.model.request.FriendRequestConstant;
import com.netcracker.model.status.Status;
import com.netcracker.model.status.StatusConstant;
import com.netcracker.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS;
import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS_IN_REF;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private EntityManagerService entityManagerService;

    @Override
    public List<FriendRequest> getProfileRequests(BigInteger profileId) {
        String requestsQuery = "SELECT OBJECT_ID\n" +
                "FROM OBJREFERENCE\n" +
                "WHERE ATTRTYPE_ID = " + FriendRequestConstant.REQ_STATUS +
                " AND REFERENCE = " + StatusConstant.ST_IS_PENDING +
                /*" AND SEQ_NO = 1\n" +*/
                " AND OBJECT_ID IN(\n" +
                "    SELECT OBJECT_ID \n" +
                "    FROM OBJREFERENCE\n" +
                "    WHERE ATTRTYPE_ID = " + FriendRequestConstant.REQ_TO +
                "    AND REFERENCE = " + profileId +
                ")" +
                " AND OBJECT_ID IN ( SELECT OBJECT_ID\n" +
                "    FROM OBJREFERENCE\n" +
                "    WHERE ATTRTYPE_ID = " + FriendRequestConstant.REQ_FROM +
                "    AND " + IGNORING_DELETED_ELEMENTS_IN_REF +
                ")";
        List<FriendRequest> list = entityManagerService.getObjectsBySQL(requestsQuery, FriendRequest.class, new QueryDescriptor());
        return list;
    }

    @Override
    public void updateRequest(FriendRequest request) {
        entityManagerService.update(request);
    }

    @Override
    public void sendRequest(FriendRequest request) {
        Status status = entityManagerService.getById(BigInteger.valueOf(StatusConstant.ST_IS_PENDING), Status.class);
        request.setRequestStatus(status);
        entityManagerService.create(request);
    }

    @Override
    public void confirmRequest(FriendRequest request) {
        Status status = entityManagerService.getById(BigInteger.valueOf(StatusConstant.ST_IS_FRIEND), Status.class);
        request.setRequestStatus(status);
        entityManagerService.delete(request.getObjectId(), 0);

        FriendRequest newRequest = new FriendRequest();
        newRequest.setReqFrom(request.getReqFrom());
        newRequest.setReqTo(request.getReqTo());
        newRequest.setRequestStatus(status);

        entityManagerService.create(newRequest);
    }

    @Override
    public void declineRequest(FriendRequest request) {
        entityManagerService.delete(request.getObjectId(), 0);
    }

    @Override
    public void deleteFriendshipStatus(BigInteger profileId, BigInteger profileIdToDelete){
        BigInteger requestId = entityManagerService.getProfilesRequestId(profileId, profileIdToDelete);
        entityManagerService.delete(requestId, 0);
    }

    @Override
    public Status getProfilesStatus(BigInteger currentProfileId, BigInteger profileIdToCheck) {
        BigInteger statusId = entityManagerService.getProfilesFriendshipStatus(currentProfileId, profileIdToCheck);
        return statusId.equals(BigInteger.valueOf(-1)) ? null : entityManagerService.getById(statusId, Status.class);
    }
}
