package main.java.com.walletapi.controllers.handlers.analytics;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import main.java.com.walletapi.services.AnalyticsService;
import main.java.com.walletapi.utils.AdminAuthValidator;
import main.java.com.walletapi.utils.ResponseFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TopSpendersHandlerTest {

    @Mock
    private AnalyticsService mockAnalyticsService;

    @Mock
    private HttpExchange mockExchange;

    private TopSpendersHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new TopSpendersHandler(mockAnalyticsService);
    }

    @Test
    void testHandleWithValidAdminKeyAndValidLimit() throws Exception {
        Headers headers = new Headers();
        headers.add("Admin-Key", "1234");

        List<Map.Entry<String, BigDecimal>> mockSpenders = List.of(
            Map.entry("0", BigDecimal.valueOf(0.00)),
            Map.entry("1", BigDecimal.valueOf(0.00))
        );

        String expectedResponse = ResponseFormatter.formatTopSpenders(mockSpenders);
        OutputStream responseStream = new ByteArrayOutputStream();

        when(mockExchange.getRequestMethod()).thenReturn("GET");
        when(mockExchange.getRequestHeaders()).thenReturn(headers);
        when(mockExchange.getRequestURI()).thenReturn(new URI("/analytics/top-spenders?limit=2"));
        when(mockExchange.getResponseBody()).thenReturn(responseStream);
        when(mockAnalyticsService.getTopSpenders(2)).thenReturn(mockSpenders);

        mockStatic(AdminAuthValidator.class).when(() -> AdminAuthValidator.isValidAdminKey("1234")).thenReturn(true);

        handler.handle(mockExchange);

        verify(mockExchange).sendResponseHeaders(eq(200), eq((long) expectedResponse.getBytes(StandardCharsets.UTF_8).length));
        assertEquals(expectedResponse, responseStream.toString());
    }

    @Test
    void testHandleWithInvalidAdminKey() throws Exception {
        Headers headers = new Headers();
        headers.add("Admin-Key", "invalid-key");

        OutputStream responseStream = new ByteArrayOutputStream();
        when(mockExchange.getRequestMethod()).thenReturn("GET");
        when(mockExchange.getRequestHeaders()).thenReturn(headers);
        when(mockExchange.getResponseBody()).thenReturn(responseStream);

        mockStatic(AdminAuthValidator.class).when(() -> AdminAuthValidator.isValidAdminKey("invalid-key")).thenReturn(false);

        handler.handle(mockExchange);

        verify(mockExchange).sendResponseHeaders(eq(401), eq(0L));
    }

    @Test
    void testHandleWithInvalidLimitParameter() throws Exception {
        Headers headers = new Headers();
        headers.add("Admin-Key", "1234");

        String expectedResponse = "Invalid 'limit' parameter.";
        OutputStream responseStream = new ByteArrayOutputStream();

        when(mockExchange.getRequestMethod()).thenReturn("GET");
        when(mockExchange.getRequestHeaders()).thenReturn(headers);
        when(mockExchange.getRequestURI()).thenReturn(new URI("/analytics/top-spenders?limit=abc"));
        when(mockExchange.getResponseBody()).thenReturn(responseStream);

        mockStatic(AdminAuthValidator.class).when(() -> AdminAuthValidator.isValidAdminKey("1234")).thenReturn(true);

        handler.handle(mockExchange);

        verify(mockExchange).sendResponseHeaders(eq(400), eq((long) expectedResponse.getBytes(StandardCharsets.UTF_8).length));
        assertEquals(expectedResponse, responseStream.toString());
    }

    @Test
    void testHandleWithUnsupportedHttpMethod() throws Exception {
        Headers headers = new Headers();
        headers.add("Admin-Key", "1234");

        OutputStream responseStream = new ByteArrayOutputStream();

        when(mockExchange.getRequestMethod()).thenReturn("POST");
        when(mockExchange.getRequestHeaders()).thenReturn(headers);
        when(mockExchange.getResponseBody()).thenReturn(responseStream);

        handler.handle(mockExchange);

        verify(mockExchange).sendResponseHeaders(eq(405), eq(0L));
    }

    @Test
    void testHandleWithEmptyTopSpenders() throws Exception {
        Headers headers = new Headers();
        headers.add("Admin-Key", "1234");

        List<Map.Entry<String, BigDecimal>> emptySpenders = List.of();

        String expectedResponse = ResponseFormatter.formatTopSpenders(emptySpenders);
        OutputStream responseStream = new ByteArrayOutputStream();

        when(mockExchange.getRequestMethod()).thenReturn("GET");
        when(mockExchange.getRequestHeaders()).thenReturn(headers);
        when(mockExchange.getRequestURI()).thenReturn(new URI("/analytics/top-spenders?limit=2"));
        when(mockExchange.getResponseBody()).thenReturn(responseStream);
        when(mockAnalyticsService.getTopSpenders(2)).thenReturn(emptySpenders);

        mockStatic(AdminAuthValidator.class).when(() -> AdminAuthValidator.isValidAdminKey("1234")).thenReturn(true);

        handler.handle(mockExchange);

        verify(mockExchange).sendResponseHeaders(eq(200), eq((long) expectedResponse.getBytes(StandardCharsets.UTF_8).length));
        assertEquals(expectedResponse, responseStream.toString());
    }
}
