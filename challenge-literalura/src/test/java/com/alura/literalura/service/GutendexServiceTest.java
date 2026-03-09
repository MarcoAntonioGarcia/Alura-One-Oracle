package com.alura.literalura.service;

import com.alura.literalura.model.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GutendexServiceTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GutendexService gutendexService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inject the mocked HttpClient into GutendexService because gutendexService
        // instantiates it in the constructor
        ReflectionTestUtils.setField(gutendexService, "client", httpClient);
        ReflectionTestUtils.setField(gutendexService, "objectMapper", objectMapper);
    }

    @Test
    void searchBooksByTitle_Success() throws Exception {
        // Arrange
        String title = "Romeo and Juliet";
        String jsonResponse = "{\"count\":1,\"next\":null,\"previous\":null,\"results\":[{\"id\":1112,\"title\":\"Romeo and Juliet\"}]}";

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        ApiResponse mockApiResponse = new ApiResponse(java.util.Collections.emptyList());

        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);
        when(objectMapper.readValue(jsonResponse, ApiResponse.class))
                .thenReturn(mockApiResponse);

        // Act
        ApiResponse response = gutendexService.searchBooksByTitle(title);

        // Assert
        assertNotNull(response);
        assertEquals(mockApiResponse, response);
        verify(httpClient, times(1)).send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()));
    }

    @Test
    void searchBooksByTitle_HttpError() throws Exception {
        // Arrange
        String title = "Unknown Book";

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(404); // Simulate Not Found

        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        // Act
        ApiResponse response = gutendexService.searchBooksByTitle(title);

        // Assert
        assertNull(response);
        verify(httpClient, times(1)).send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()));
        verifyNoInteractions(objectMapper);
    }

    @Test
    void searchBooksByTitle_Exception() throws Exception {
        // Arrange
        String title = "Error Book";

        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenThrow(new IOException("Network error"));

        // Act
        ApiResponse response = gutendexService.searchBooksByTitle(title);

        // Assert
        assertNull(response);
        verify(httpClient, times(1)).send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()));
    }
}
