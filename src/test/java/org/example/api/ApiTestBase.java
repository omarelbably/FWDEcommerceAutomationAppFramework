package org.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Base class for API testing with REST Assured
 * Provides common functionality for REST API testing
 */
public class ApiTestBase {
    
    private static final Logger logger = LoggerFactory.getLogger(ApiTestBase.class);
    private static final Properties config = loadConfig();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    protected static final String BASE_URL = config.getProperty("api.base.url", "https://api.example.com");
    protected static final String API_KEY = config.getProperty("api.key", "");
    protected static final int TIMEOUT = Integer.parseInt(config.getProperty("api.timeout", "30000"));
    
    private static Properties loadConfig() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (IOException e) {
            logger.warn("Could not load config.properties, using default values", e);
        }
        return props;
    }
    
    /**
     * Create a base request specification
     */
    protected RequestSpecification createRequestSpec() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .timeout(TIMEOUT)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Accept", "application/json");
    }
    
    /**
     * Perform GET request
     */
    protected Response get(String endpoint) {
        logger.info("Performing GET request to: {}", endpoint);
        Response response = createRequestSpec()
                .when()
                .get(endpoint);
        logResponse(response);
        return response;
    }
    
    /**
     * Perform GET request with path parameters
     */
    protected Response get(String endpoint, Map<String, Object> pathParams) {
        logger.info("Performing GET request to: {} with path params: {}", endpoint, pathParams);
        Response response = createRequestSpec()
                .pathParams(pathParams)
                .when()
                .get(endpoint);
        logResponse(response);
        return response;
    }
    
    /**
     * Perform GET request with query parameters
     */
    protected Response get(String endpoint, Map<String, Object> pathParams, Map<String, Object> queryParams) {
        logger.info("Performing GET request to: {} with path params: {} and query params: {}", 
                   endpoint, pathParams, queryParams);
        Response response = createRequestSpec()
                .pathParams(pathParams)
                .queryParams(queryParams)
                .when()
                .get(endpoint);
        logResponse(response);
        return response;
    }
    
    /**
     * Perform POST request
     */
    protected Response post(String endpoint, Object body) {
        logger.info("Performing POST request to: {} with body: {}", endpoint, body);
        Response response = createRequestSpec()
                .body(body)
                .when()
                .post(endpoint);
        logResponse(response);
        return response;
    }
    
    /**
     * Perform PUT request
     */
    protected Response put(String endpoint, Object body) {
        logger.info("Performing PUT request to: {} with body: {}", endpoint, body);
        Response response = createRequestSpec()
                .body(body)
                .when()
                .put(endpoint);
        logResponse(response);
        return response;
    }
    
    /**
     * Perform DELETE request
     */
    protected Response delete(String endpoint) {
        logger.info("Performing DELETE request to: {}", endpoint);
        Response response = createRequestSpec()
                .when()
                .delete(endpoint);
        logResponse(response);
        return response;
    }
    
    /**
     * Perform PATCH request
     */
    protected Response patch(String endpoint, Object body) {
        logger.info("Performing PATCH request to: {} with body: {}", endpoint, body);
        Response response = createRequestSpec()
                .body(body)
                .when()
                .patch(endpoint);
        logResponse(response);
        return response;
    }
    
    /**
     * Log response details
     */
    private void logResponse(Response response) {
        logger.info("Response Status: {}", response.getStatusCode());
        logger.info("Response Headers: {}", response.getHeaders());
        if (response.getBody() != null) {
            logger.info("Response Body: {}", response.getBody().asPrettyString());
        }
    }
    
    /**
     * Convert object to JSON string
     */
    protected String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("Error converting object to JSON", e);
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }
    
    /**
     * Convert JSON string to object
     */
    protected <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("Error converting JSON to object", e);
            throw new RuntimeException("Error converting JSON to object", e);
        }
    }
    
    /**
     * Validate response status code
     */
    protected void validateStatusCode(Response response, int expectedStatusCode) {
        if (response.getStatusCode() != expectedStatusCode) {
            logger.error("Expected status code: {}, but got: {}", expectedStatusCode, response.getStatusCode());
            throw new AssertionError("Expected status code: " + expectedStatusCode + 
                                   ", but got: " + response.getStatusCode());
        }
    }
    
    /**
     * Validate response contains field
     */
    protected void validateResponseContainsField(Response response, String fieldName) {
        if (!response.jsonPath().get(fieldName).toString().isEmpty()) {
            logger.error("Response does not contain field: {}", fieldName);
            throw new AssertionError("Response does not contain field: " + fieldName);
        }
    }
    
    /**
     * Validate response field value
     */
    protected void validateResponseFieldValue(Response response, String fieldName, Object expectedValue) {
        Object actualValue = response.jsonPath().get(fieldName);
        if (!expectedValue.equals(actualValue)) {
            logger.error("Field {} expected: {}, but got: {}", fieldName, expectedValue, actualValue);
            throw new AssertionError("Field " + fieldName + " expected: " + expectedValue + 
                                   ", but got: " + actualValue);
        }
    }
    
    /**
     * Wait for response with polling
     */
    protected Response waitForResponse(String endpoint, int maxAttempts, long intervalMs) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            logger.info("Attempt {}/{} to get response from: {}", attempt, maxAttempts, endpoint);
            Response response = get(endpoint);
            
            if (response.getStatusCode() == 200) {
                return response;
            }
            
            if (attempt < maxAttempts) {
                try {
                    Thread.sleep(intervalMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Wait interrupted", e);
                }
            }
        }
        
        throw new RuntimeException("Failed to get successful response after " + maxAttempts + " attempts");
    }
} 