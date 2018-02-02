package com.netcracker.controller.record;

import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.record.PhotoRecord;
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
    private RecordService recordService;

    @GetMapping("/wall/{id}")
    public List<WallRecord> getWallRecords(@PathVariable("id") BigInteger profileId) {
        return recordService.getWallRecords(profileId);
    }

    @GetMapping("/group/{id}")
    public List<GroupRecord> getGroupRecords(@PathVariable("id") BigInteger groupId) {
        return recordService.getGroupRecords(groupId);
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

    @PostMapping("/photo/update")
    public void updatePhotoRecord(@RequestBody PhotoRecord record){
        recordService.updatePhotoRecord(record);
    }

    @PostMapping("/delete")
    public void deleteWallRecord(@RequestBody AbstractRecord wallRecord){
        recordService.deleteRecord(wallRecord);
    }
}
