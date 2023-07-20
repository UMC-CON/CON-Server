package com.umc.con.common.config;

import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.common.config.BaseResponseStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BaseResponseTest {

    @Test
    public void testBaseResponseSuccess() {
        // Arrange
        String expectedResult = "Test Result";
        BaseResponse<String> response = new BaseResponse<>(expectedResult);

        // Act & Assert
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(200, response.getCode());
        Assertions.assertEquals("요청에 성공했습니다.", response.getMessage());
        Assertions.assertEquals(expectedResult, response.getResult());
    }

    @Test
    public void testBaseResponseFailure() {
        // Arrange
        BaseResponseStatus failureStatus = BaseResponseStatus.RESPONSE_ERROR;
        BaseResponse<String> response = new BaseResponse<>(failureStatus);

        // Act & Assert
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(failureStatus.getCode(), response.getCode());
        Assertions.assertEquals(failureStatus.getMessage(), response.getMessage());
        Assertions.assertNull(response.getResult());
    }

}