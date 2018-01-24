package com.netcracker.controller.record;

import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordController {
    @Autowired
    RecordService recordService;

    @GetMapping("/{id}")
    public List<WallRecord> getProfileWallRecords(@PathVariable("id") BigInteger profileId) {
        return recordService.getProfileWallRecords(profileId);
    }

    @GetMapping("/author/{id}")
    public Profile getRecordAuthor(@PathVariable("id") BigInteger recordID) {
        return recordService.getRecordAuthor(recordID);
    }
    @GetMapping("/owner/{id}")
    public Profile getWallRecordOwner(@PathVariable("id") BigInteger id) {
        return recordService.getWallRecordOwner(id);
    }

    @PostMapping("/wall/add")
    public WallRecord createWallRecord(@RequestBody WallRecord record){
        return recordService.createWallRecord(record);
    }

    @PostMapping("/group/add")
    public GroupRecord createGroupRecord(@RequestBody GroupRecord record){
        return recordService.createGroupRecord(record);
    }

    @PostMapping("/wall/update")
    public void updateWallRecord(@RequestBody WallRecord record){
        recordService.updateWallRecord(record);
    }

    @PostMapping("/group/update")
    public void updateGroupRecord(@RequestBody GroupRecord record){
        recordService.updateGroupRecord(record);
    }

    @PostMapping("/delete")
    public void deleteWallRecord(@RequestBody AbstractRecord wallRecord){
        recordService.deleteWallRecord(wallRecord);
    }
}
