package com.owori.domain.record.service;

import com.owori.domain.record.dto.request.AddRecordRequest;
import com.owori.domain.record.dto.request.UpdateRecordRequest;
import com.owori.domain.record.dto.response.AddRecordResponse;
import com.owori.domain.record.dto.response.FindRecordByMonthResponse;
import com.owori.domain.record.dto.response.UpdateRecordResponse;
import com.owori.domain.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;

    public AddRecordResponse addRecord(AddRecordRequest addRecordRequest) {
        return null; // todo 로직 작성
    }

    public UpdateRecordResponse updateRecord(UpdateRecordRequest updateRecordRequest) {
        return null; // todo 로직작성
    }

    public List<FindRecordByMonthResponse> findRecordByMonth(Pageable pageable, String month) {
        return null; // todo 로직작성
    }
}
