package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(title = "호텔 리스트 DTO - 날짜, 강아지 정보는 항상 같이 입력")
public class HotelListSelDto {
    // @JsonProperty(required = false) 기본
    @Min(0)
    @Schema(title = "메인필터 유무 (필수)")
    @JsonProperty(value = "main_filter")
    private int mainFilter; // 필터 했는지 유무
    @Schema(title = "주소(시도)")
    private String address;
    @Future
    @JsonProperty(value = "from_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(title = "시작일")
    private String fromDate;
    @Future
    @JsonProperty(value = "to_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(title = "종료일")
    private String toDate;
    @JsonProperty(value = "dog_info")
    @Schema(title = "강아지 정보")
    private List<DogSizeEa> dogInfo;
    @Schema(title = "검색")
    private String search; // 호텔 이름 검색
    // 호텔 이용 테마
    @JsonProperty(value = "hotel_option_pk")
    private List<Integer> hotelOptionPk;
    @Min(0)
    @JsonProperty(value = "filter_type")
    @Schema(title = "하단 필터링, default(0) : 추천순(최신순), 1 : 별점 높은 순, 2 : 리뷰 많은 순")
    private int filterType; // default(0) : 적용 x, 1 : 별점 높은 순, 2 : 리뷰 많은 순
    @JsonIgnore
    private int rowCount;
    @JsonIgnore
    private int userPk;
    @JsonIgnore
    private List<LocalDate> date; // 기간을 날짜 리스트로 저장
    @JsonIgnore
    private int startIdx;
    @JsonIgnore
    private int page;
    @JsonIgnore
    private List<Integer> dogSizePks = new ArrayList<>();
    @JsonIgnore
    private List<String> tokensToStrList;
    @JsonIgnore
    private int optionPkSize; // 옵션pk 총 갯수 - xml having절에서 사용
    @JsonIgnore
    private int dogPkSize; // 강아지pk 총 갯수 - xml having절에서 사용
    @JsonIgnore
    private List<Integer> filteredPk;

    public void setPage(int page){
        startIdx = (page - 1) * this.rowCount;
        this.page = page;
    }
}
