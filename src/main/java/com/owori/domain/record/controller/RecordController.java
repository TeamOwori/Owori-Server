package com.owori.domain.record.controller;

import com.owori.domain.record.dto.request.AddRecordRequest;
import com.owori.domain.record.dto.request.UpdateRecordRequest;
import com.owori.domain.record.dto.response.AddRecordResponse;
import com.owori.domain.record.dto.response.FindRecordByMonthResponse;
import com.owori.domain.record.dto.response.UpdateRecordResponse;
import com.owori.domain.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordController {
    private final RecordService recordService;

    @PostMapping
    public ResponseEntity<AddRecordResponse> addRecord(@RequestBody AddRecordRequest addRecordRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.addRecord(addRecordRequest));
    }

    @PatchMapping("/update")
    public ResponseEntity<UpdateRecordResponse> updateRecord(@RequestParam UUID id, @RequestBody UpdateRecordRequest updateRecordRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.updateRecord(updateRecordRequest));
    }

    @GetMapping("/month")
    public ResponseEntity<List<FindRecordByMonthResponse>> findRecordByMonth(@PageableDefault(sort = "startDate", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam String month) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.findRecordByMonth(pageable, month));
    }
}
