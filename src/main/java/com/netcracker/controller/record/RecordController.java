package com.netcracker.controller.record;

import com.netcracker.model.record.StubWallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/wallrecords")
public class RecordController {
    @Autowired
    RecordService recordService;

    @GetMapping("/{id}")
    public List<StubWallRecord> getMyWallRecords(@PathVariable("id") BigInteger profileId) {
        return recordService.getProfileWallRecords(profileId);
    }

    @GetMapping("/author/{id}")
    public Profile getCurrentWallRecordAuthor(@PathVariable("id") BigInteger wallRecordID) {
        return recordService.getWallRecordAuthor(wallRecordID);
    }
}
