package org.ecsail; // Adjust to match your project's package

import org.ecsail.static_tools.StringTools;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StringToolsTest {

    @Test
    void testIsValidEmail_ValidEmail() {
        assertTrue(StringTools.isValidEmail("user@example.com"), "Valid email should return true");
        assertTrue(StringTools.isValidEmail("user.name@domain.co"), "Valid email with dot should return true");
        assertTrue(StringTools.isValidEmail("user+label@sub.domain.org"), "Valid email with subdomain and label should return true");
        assertTrue(StringTools.isValidEmail("a@b.com"), "Minimal valid email should return true");
    }

    @Test
    void testIsValidEmail_InvalidEmail() {
        assertFalse(StringTools.isValidEmail("invalid@"), "Incomplete email should return false");
        assertFalse(StringTools.isValidEmail("no.at.symbol"), "Email without @ should return false");
        assertFalse(StringTools.isValidEmail("@domain.com"), "Email without local part should return false");
        assertFalse(StringTools.isValidEmail("user@.com"), "Email with empty domain should return false");
        assertFalse(StringTools.isValidEmail("user@domain"), "Email without TLD should return false");
        assertFalse(StringTools.isValidEmail("user@domain.c"), "Email with short TLD should return false");
    }

    @Test
    void testIsValidEmail_NullInput() {
        assertFalse(StringTools.isValidEmail(null), "Null email should return false");
    }

    @Test
    void testIsValidEmail_EmptyOrWhitespace() {
        assertFalse(StringTools.isValidEmail(""), "Empty email should return false");
        assertFalse(StringTools.isValidEmail(" "), "Whitespace email should return false");
    }

    @Test
    void testIsValidEmail_SpecialCharacters() {
        assertTrue(StringTools.isValidEmail("user+test@domain.com"), "Email with + should return true");
        assertTrue(StringTools.isValidEmail("user-test@domain.com"), "Email with - should return true");
        assertTrue(StringTools.isValidEmail("user_name@domain.com"), "Email with _ should return true");
        assertFalse(StringTools.isValidEmail("user#test@domain.com"), "Email with invalid # should return false");
    }
}