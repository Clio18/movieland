package com.tteam.movieland.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SoftReferenceCacheTest {

    private final SoftReferenceCache<Integer, String> cache = new SoftReferenceCache<>();

    @Mock
    private Supplier<String> supplier;

    @Test
    @DisplayName("Test Put and Get Method")
    void testPutAndGet() {
        int key = 1;
        String value = "test value";

        cache.put(key, value);
        Optional<String> result = cache.get(key);

        assertEquals(value, result.orElse(null));
    }

    @Test
    @DisplayName("Test Put and Get Method with Null Value")
    void testPutAndGetWithNull() {
        int key = 1;

        cache.put(key, null);
        Optional<String> result = cache.get(key);

        assertNull(result.orElse(null));
    }

    @Test
    @DisplayName("Test Get or Put Method Returns Cached Value")
    void testGetOrPutReturnsCachedValue() {
        int key = 1;
        String value = "test value";
        cache.put(key, value);

        String result = cache.getOrPut(key, supplier);

        assertEquals(value, result);
        verify(supplier, never()).get();
    }

    @Test
    @DisplayName("Test Get or Put Method Returns New Value")
    void testGetOrPutReturnsNewValue() {
        int key = 1;
        String value = "test value";
        when(supplier.get()).thenReturn(value);

        String result = cache.getOrPut(key, supplier);

        assertEquals(value, result);
        verify(supplier, times(1)).get();
    }

    @Test
    @DisplayName("Test Get or Put Method Returns Null Value")
    void testGetOrPutReturnsNull() {
        int key = 1;
        when(supplier.get()).thenReturn(null);

        String result = cache.getOrPut(key, supplier);

        assertNull(result);
        verify(supplier, times(1)).get();
    }
}