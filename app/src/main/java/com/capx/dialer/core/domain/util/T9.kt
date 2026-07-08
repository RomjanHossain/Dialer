package com.capx.dialer.core.domain.util

import com.capx.dialer.core.domain.model.Contact

/**
 * T9 predictive-text matching for the dialpad.
 *
 * Lets the user find a contact by typing the digits that correspond to the
 * letters of their name on a phone keypad. For example typing `2743` matches
 * "Arif" (A=2, R=7, I=4, F=3), and `273` matches any contact whose name (or a
 * word within it) begins with those key groups such as "Are", "Bre", "Cap"…
 *
 * Pure Kotlin — no Android dependencies.
 */
object T9 {

    /** Maps a lowercase letter to its dial-pad digit. */
    private val letterToDigit: Map<Char, Char> = buildMap {
        fun map(digit: Char, letters: String) = letters.forEach { put(it, digit) }
        map('2', "abc")
        map('3', "def")
        map('4', "ghi")
        map('5', "jkl")
        map('6', "mno")
        map('7', "pqrs")
        map('8', "tuv")
        map('9', "wxyz")
    }

    /**
     * Converts a piece of text to its T9 digit representation. Any character
     * that is not a mappable letter (spaces, punctuation) is dropped so that
     * "Mary-Jane" and "MaryJane" produce the same digit string.
     */
    fun toDigits(text: String): String {
        val sb = StringBuilder(text.length)
        for (c in text.lowercase()) {
            letterToDigit[c]?.let { sb.append(it) }
                ?: if (c.isDigit()) sb.append(c) else Unit
        }
        return sb.toString()
    }

    /**
     * Returns true if [contact] matches the typed [query].
     *
     * A match occurs when the query (digits only) is a prefix of the T9
     * encoding of the whole name, a prefix of any single word in the name,
     * or a substring of the raw phone number.
     */
    fun matches(contact: Contact, query: String): Boolean {
        val digits = query.filter { it.isDigit() }
        if (digits.isEmpty()) return false

        // Number match — most common case for a dialpad.
        val normalizedNumber = contact.phoneNumber.filter { it.isDigit() }
        if (normalizedNumber.contains(digits)) return true

        // Whole-name T9 prefix match.
        val name = contact.name
        if (name.isBlank()) return false
        if (toDigits(name).startsWith(digits)) return true

        // Per-word prefix match ("john sm" -> matching "sm" as a word start).
        return name.split(' ', '-', '.', ',')
            .any { word -> word.isNotBlank() && toDigits(word).startsWith(digits) }
    }
}
