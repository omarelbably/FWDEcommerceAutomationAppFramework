package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Test data caching utility for efficient test data management
 * Provides in-memory and file-based caching with automatic cleanup
 */
public class TestDataCache {
    
    private static final Logger logger = LoggerFactory.getLogger(TestDataCache.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // In-memory cache
    private static final Map<String, CachedData> memoryCache = new ConcurrentHashMap<>();
    
    // Cache configuration
    private static final String CACHE_DIR = "target/test-data-cache";
    private static final long CACHE_TTL_HOURS = 24; // 24 hours
    private static final int MAX_CACHE_SIZE = 1000;
    
    // Scheduled cleanup
    private static final ScheduledExecutorService cleanupExecutor = Executors.newScheduledThreadPool(1);
    
    static {
        // Initialize cache directory
        initializeCacheDirectory();
        
        // Schedule cleanup task
        cleanupExecutor.scheduleAtFixedRate(
            TestDataCache::cleanupExpiredData,
            1, // Initial delay
            1, // Period
            TimeUnit.HOURS
        );
        
        // Load cached data from disk
        loadCachedDataFromDisk();
        
        // Shutdown hook for cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            cleanupExecutor.shutdown();
            saveCacheToDisk();
        }));
    }
    
    /**
     * Cache test data with automatic TTL
     */
    public static void cacheData(String key, Object data) {
        cacheData(key, data, CACHE_TTL_HOURS);
    }
    
    /**
     * Cache test data with custom TTL
     */
    public static void cacheData(String key, Object data, long ttlHours) {
        if (memoryCache.size() >= MAX_CACHE_SIZE) {
            cleanupOldestEntries();
        }
        
        CachedData cachedData = new CachedData(data, LocalDateTime.now().plusHours(ttlHours));
        memoryCache.put(key, cachedData);
        
        // Save to disk
        saveToDisk(key, cachedData);
        
        logger.debug("Data cached with key: {} (TTL: {} hours)", key, ttlHours);
    }
    
    /**
     * Retrieve cached data
     */
    @SuppressWarnings("unchecked")
    public static <T> T getCachedData(String key, Class<T> clazz) {
        CachedData cachedData = memoryCache.get(key);
        
        if (cachedData == null) {
            // Try to load from disk
            cachedData = loadFromDisk(key);
            if (cachedData != null) {
                memoryCache.put(key, cachedData);
            }
        }
        
        if (cachedData != null && !cachedData.isExpired()) {
            logger.debug("Retrieved cached data for key: {}", key);
            return (T) cachedData.getData();
        }
        
        if (cachedData != null && cachedData.isExpired()) {
            // Remove expired data
            memoryCache.remove(key);
            removeFromDisk(key);
            logger.debug("Expired data removed for key: {}", key);
        }
        
        return null;
    }
    
    /**
     * Check if data exists in cache and is not expired
     */
    public static boolean hasCachedData(String key) {
        CachedData cachedData = memoryCache.get(key);
        
        if (cachedData == null) {
            cachedData = loadFromDisk(key);
            if (cachedData != null) {
                memoryCache.put(key, cachedData);
            }
        }
        
        return cachedData != null && !cachedData.isExpired();
    }
    
    /**
     * Remove data from cache
     */
    public static void removeCachedData(String key) {
        memoryCache.remove(key);
        removeFromDisk(key);
        logger.debug("Data removed from cache for key: {}", key);
    }
    
    /**
     * Clear all cached data
     */
    public static void clearCache() {
        memoryCache.clear();
        clearDiskCache();
        logger.info("Cache cleared");
    }
    
    /**
     * Get cache statistics
     */
    public static CacheStats getCacheStats() {
        long expiredCount = memoryCache.values().stream()
            .filter(CachedData::isExpired)
            .count();
        
        long validCount = memoryCache.size() - expiredCount;
        
        return new CacheStats(
            memoryCache.size(),
            validCount,
            expiredCount,
            getDiskCacheSize()
        );
    }
    
    /**
     * Cache user data with automatic generation
     */
    public static Map<String, String> getOrCreateUserData(String key) {
        if (hasCachedData(key)) {
            return getCachedData(key, Map.class);
        }
        
        Map<String, String> userData = TestDataManager.generateUserData();
        cacheData(key, userData);
        return userData;
    }
    
    /**
     * Cache product data with automatic generation
     */
    public static Map<String, String> getOrCreateProductData(String key) {
        if (hasCachedData(key)) {
            return getCachedData(key, Map.class);
        }
        
        Map<String, String> productData = TestDataManager.generateProductData();
        cacheData(key, productData);
        return productData;
    }
    
    /**
     * Cache search data with automatic generation
     */
    public static String getOrCreateSearchTerm(String key) {
        if (hasCachedData(key)) {
            return getCachedData(key, String.class);
        }
        
        String searchTerm = TestDataManager.generateSearchTerm();
        cacheData(key, searchTerm);
        return searchTerm;
    }
    
    // Private helper methods
    
    private static void initializeCacheDirectory() {
        try {
            Path cachePath = Paths.get(CACHE_DIR);
            if (!Files.exists(cachePath)) {
                Files.createDirectories(cachePath);
                logger.info("Cache directory created: {}", CACHE_DIR);
            }
        } catch (IOException e) {
            logger.error("Failed to create cache directory", e);
        }
    }
    
    private static void saveToDisk(String key, CachedData data) {
        try {
            Path filePath = Paths.get(CACHE_DIR, key + ".json");
            String json = objectMapper.writeValueAsString(data);
            Files.write(filePath, json.getBytes());
        } catch (IOException e) {
            logger.error("Failed to save data to disk for key: {}", key, e);
        }
    }
    
    private static CachedData loadFromDisk(String key) {
        try {
            Path filePath = Paths.get(CACHE_DIR, key + ".json");
            if (Files.exists(filePath)) {
                String json = Files.readString(filePath);
                return objectMapper.readValue(json, CachedData.class);
            }
        } catch (IOException e) {
            logger.error("Failed to load data from disk for key: {}", key, e);
        }
        return null;
    }
    
    private static void removeFromDisk(String key) {
        try {
            Path filePath = Paths.get(CACHE_DIR, key + ".json");
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            logger.error("Failed to remove data from disk for key: {}", key, e);
        }
    }
    
    private static void clearDiskCache() {
        try {
            Path cachePath = Paths.get(CACHE_DIR);
            if (Files.exists(cachePath)) {
                Files.walk(cachePath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            logger.error("Failed to delete cache file: {}", file, e);
                        }
                    });
            }
        } catch (IOException e) {
            logger.error("Failed to clear disk cache", e);
        }
    }
    
    private static long getDiskCacheSize() {
        try {
            Path cachePath = Paths.get(CACHE_DIR);
            if (Files.exists(cachePath)) {
                return Files.walk(cachePath)
                    .filter(Files::isRegularFile)
                    .count();
            }
        } catch (IOException e) {
            logger.error("Failed to get disk cache size", e);
        }
        return 0;
    }
    
    private static void loadCachedDataFromDisk() {
        try {
            Path cachePath = Paths.get(CACHE_DIR);
            if (Files.exists(cachePath)) {
                Files.walk(cachePath)
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".json"))
                    .forEach(file -> {
                        try {
                            String key = file.getFileName().toString().replace(".json", "");
                            CachedData data = objectMapper.readValue(Files.readString(file), CachedData.class);
                            if (!data.isExpired()) {
                                memoryCache.put(key, data);
                            } else {
                                Files.delete(file);
                            }
                        } catch (IOException e) {
                            logger.error("Failed to load cache file: {}", file, e);
                        }
                    });
            }
        } catch (IOException e) {
            logger.error("Failed to load cached data from disk", e);
        }
    }
    
    private static void saveCacheToDisk() {
        memoryCache.forEach((key, data) -> {
            if (!data.isExpired()) {
                saveToDisk(key, data);
            }
        });
    }
    
    private static void cleanupExpiredData() {
        memoryCache.entrySet().removeIf(entry -> {
            if (entry.getValue().isExpired()) {
                removeFromDisk(entry.getKey());
                return true;
            }
            return false;
        });
        
        logger.debug("Cache cleanup completed. Remaining entries: {}", memoryCache.size());
    }
    
    private static void cleanupOldestEntries() {
        // Remove oldest 10% of entries
        int entriesToRemove = Math.max(1, memoryCache.size() / 10);
        
        memoryCache.entrySet().stream()
            .sorted((e1, e2) -> e1.getValue().getCreatedAt().compareTo(e2.getValue().getCreatedAt()))
            .limit(entriesToRemove)
            .forEach(entry -> {
                memoryCache.remove(entry.getKey());
                removeFromDisk(entry.getKey());
            });
        
        logger.debug("Removed {} oldest cache entries", entriesToRemove);
    }
    
    // Data classes
    
    public static class CachedData {
        private Object data;
        private LocalDateTime expiresAt;
        private LocalDateTime createdAt;
        
        public CachedData() {} // For JSON deserialization
        
        public CachedData(Object data, LocalDateTime expiresAt) {
            this.data = data;
            this.expiresAt = expiresAt;
            this.createdAt = LocalDateTime.now();
        }
        
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
        
        public LocalDateTime getExpiresAt() { return expiresAt; }
        public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
        
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        
        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiresAt);
        }
    }
    
    public static class CacheStats {
        private final long totalEntries;
        private final long validEntries;
        private final long expiredEntries;
        private final long diskEntries;
        
        public CacheStats(long totalEntries, long validEntries, long expiredEntries, long diskEntries) {
            this.totalEntries = totalEntries;
            this.validEntries = validEntries;
            this.expiredEntries = expiredEntries;
            this.diskEntries = diskEntries;
        }
        
        // Getters
        public long getTotalEntries() { return totalEntries; }
        public long getValidEntries() { return validEntries; }
        public long getExpiredEntries() { return expiredEntries; }
        public long getDiskEntries() { return diskEntries; }
        
        @Override
        public String toString() {
            return String.format("CacheStats{total=%d, valid=%d, expired=%d, disk=%d}", 
                               totalEntries, validEntries, expiredEntries, diskEntries);
        }
    }
} 