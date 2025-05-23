package CUK.CUKBOB.review.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class ReviewUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Boolean> parseReviewListArray(String jsonArrayString) {
        try {
            return objectMapper.readValue(jsonArrayString, new TypeReference<List<Boolean>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("리뷰 리스트 파싱 실패", e);
        }
    }
}