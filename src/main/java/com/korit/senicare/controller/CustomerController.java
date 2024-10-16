package com.korit.senicare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.korit.senicare.dto.request.customer.PatchCustomerRequestDto;
import com.korit.senicare.dto.request.customer.PostCareRecordRequestDto;
import com.korit.senicare.dto.request.customer.PostCustomerRequestDto;
import com.korit.senicare.dto.response.ResponseDto;
import com.korit.senicare.dto.response.customer.GetCareRecordListResponseDto;
import com.korit.senicare.dto.response.customer.GetCustomerListResponseDto;
import com.korit.senicare.dto.response.customer.GetCustomerResponseDto;
import com.korit.senicare.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;

    @PostMapping(value={"", "/"})
    public ResponseEntity<ResponseDto> postCustomer(
        @RequestBody @Valid PostCustomerRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response = customerService.postCustomer(requestBody);
        return response;
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<? super GetCustomerListResponseDto> getCustomerList(){
        ResponseEntity<? super GetCustomerListResponseDto> response = customerService.getCustomerList();
        return response;
    }

    @GetMapping("/{customerNumber}")
    public ResponseEntity<? super GetCustomerResponseDto> getCustomer(
        @PathVariable("customerNumber") Integer customerNumber
    ){
        ResponseEntity<? super GetCustomerResponseDto> response = customerService.getCutomer(customerNumber);
        return response;
    }

    @PatchMapping("/{customerNumber}")
    public ResponseEntity<ResponseDto> patchCustomer(
        @RequestBody @ Valid PatchCustomerRequestDto requestDto,
        @PathVariable("customerNumber") Integer customerNumber,
        @AuthenticationPrincipal String userid
    ){
        ResponseEntity<ResponseDto> response = customerService.pathCustomer(requestDto, customerNumber, userid);
        return response;
    }

    @DeleteMapping("/{customerNumber}")
    public ResponseEntity<ResponseDto> deleteCustomer(
        @PathVariable("customerNumber") Integer customerNumber,
        @AuthenticationPrincipal String userId
    ){
        ResponseEntity<ResponseDto> response = customerService.deleteCustomer(customerNumber, userId);
        return response;
    }

    @PostMapping("{customerNumber}/care-record")
    public ResponseEntity<ResponseDto> postCareRecord(
    @RequestBody @Valid PostCareRecordRequestDto requestBody,
    @PathVariable("customerNumber") Integer customerNumber,
    @AuthenticationPrincipal String userIdString 
    ){
        Integer usedToolNumber = requestBody.getUsedToolNumber();
        Integer count = requestBody.getCount();
        if(
            (usedToolNumber != null && count == null) ||
            (usedToolNumber == null && count != null)
        ) return ResponseDto.validationFail();

        ResponseEntity<ResponseDto> response = customerService.postCareRecord(requestBody, customerNumber, userIdString);
        return response;
    }

    @GetMapping("/{customerNumber}/care-records")
    public ResponseEntity<? super GetCareRecordListResponseDto> getCareRecoardList(
        @PathVariable("customerNumber") Integer customerNumber
    ){
        ResponseEntity<? super GetCareRecordListResponseDto> response = customerService.getCareRecordList(customerNumber);
        return response;
    }
}

// @PathVariable 경로변수, @AuthenticationPrincipal 접근주체자 가지고 와야할때)