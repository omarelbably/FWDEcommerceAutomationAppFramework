package org.example.utils;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestDataManager {
    
    private static final Logger logger = LoggerFactory.getLogger(TestDataManager.class);
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    
    // User registration data
    public static Map<String, String> generateUserData() {
        Map<String, String> userData = new HashMap<>();
        
        userData.put("firstName", faker.name().firstName());
        userData.put("lastName", faker.name().lastName());
        userData.put("email", faker.internet().emailAddress());
        userData.put("password", generateStrongPassword());
        userData.put("confirmPassword", userData.get("password"));
        userData.put("day", String.valueOf(random.nextInt(28) + 1));
        userData.put("month", String.valueOf(random.nextInt(12) + 1));
        userData.put("year", String.valueOf(random.nextInt(30) + 1970));
        
        logger.info("Generated user data: {}", userData);
        return userData;
    }
    
    public static String generateStrongPassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        
        StringBuilder password = new StringBuilder();
        
        // Ensure at least one character from each category
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));
        
        // Add random characters to make it 8+ characters
        String allChars = upperCase + lowerCase + numbers + specialChars;
        for (int i = 4; i < 12; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // Shuffle the password
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
    
    // Search data
    public static String generateSearchTerm() {
        String[] searchTerms = {
            "laptop", "phone", "camera", "book", "shirt", "shoes", "watch", "headphones"
        };
        return searchTerms[random.nextInt(searchTerms.length)];
    }
    
    // Product data
    public static Map<String, String> generateProductData() {
        Map<String, String> productData = new HashMap<>();
        
        productData.put("name", faker.commerce().productName());
        productData.put("price", faker.commerce().price());
        productData.put("description", faker.lorem().sentence());
        productData.put("category", faker.commerce().department());
        
        return productData;
    }
    
    // Address data
    public static Map<String, String> generateAddressData() {
        Map<String, String> addressData = new HashMap<>();
        
        addressData.put("street", faker.address().streetAddress());
        addressData.put("city", faker.address().city());
        addressData.put("state", faker.address().state());
        addressData.put("zipCode", faker.address().zipCode());
        addressData.put("country", faker.address().country());
        addressData.put("phone", faker.phoneNumber().phoneNumber());
        
        return addressData;
    }
    
    // Random data generators
    public static String getRandomEmail() {
        return faker.internet().emailAddress();
    }
    
    public static String getRandomName() {
        return faker.name().fullName();
    }
    
    public static String getRandomPhone() {
        return faker.phoneNumber().phoneNumber();
    }
    
    public static String getRandomCompany() {
        return faker.company().name();
    }
    
    public static String getRandomText(int wordCount) {
        return faker.lorem().words(wordCount).toString();
    }
    
    public static String getRandomSentence() {
        return faker.lorem().sentence();
    }
    
    public static String getRandomParagraph() {
        return faker.lorem().paragraph();
    }
    
    // Currency data
    public static String[] getSupportedCurrencies() {
        return new String[]{"USD", "EUR", "GBP", "JPY", "CAD", "AUD"};
    }
    
    public static String getRandomCurrency() {
        String[] currencies = getSupportedCurrencies();
        return currencies[random.nextInt(currencies.length)];
    }
    
    // Date utilities
    public static String getRandomDate() {
        return faker.date().birthday().toString();
    }
    
    public static String getFutureDate(int daysFromNow) {
        return faker.date().future(daysFromNow, java.util.concurrent.TimeUnit.DAYS).toString();
    }
    
    public static String getPastDate(int daysAgo) {
        return faker.date().past(daysAgo, java.util.concurrent.TimeUnit.DAYS).toString();
    }
    
    // Validation data
    public static String getInvalidEmail() {
        return "invalid-email";
    }
    
    public static String getWeakPassword() {
        return "123";
    }
    
    public static String getMismatchedPassword() {
        return "differentPassword123!";
    }
    
    // Test scenario data
    public static Map<String, Object> getTestScenarioData(String scenarioName) {
        Map<String, Object> scenarioData = new HashMap<>();
        
        switch (scenarioName.toLowerCase()) {
            case "registration" -> scenarioData.put("userData", generateUserData());
            case "search" -> scenarioData.put("searchTerm", generateSearchTerm());
            case "product" -> scenarioData.put("productData", generateProductData());
            case "address" -> scenarioData.put("addressData", generateAddressData());
            default -> logger.warn("Unknown scenario: {}", scenarioName);
        }
        
        return scenarioData;
    }
} 