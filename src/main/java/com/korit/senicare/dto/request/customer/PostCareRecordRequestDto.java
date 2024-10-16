package com.korit.senicare.dto.request.customer;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostCareRecordRequestDto {
    
    private String contents;
    private Integer usedToolNumber;
    @Min(1)
    private Integer count;
}
