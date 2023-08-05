package com.rc.core.infrastructure

import com.rc.core.domain.exception.KeyNotFoundException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals

class InFileDatabaseTest {

    private var database = InFileDatabase("src/test/resources/database.txt")
    private lateinit var resultValue : String

    @AfterEach
    fun `drop file`() {
        Files.deleteIfExists(Paths.get("src/test/resources/database.txt"))
    }

    @Test
    fun `should set a value for a non-existent key`() {
        whenAKeyValueIsSet()

        thenTheKeyValueIsSaved()
    }

    @Test
    fun `should update value when set a value for an existent key`() {
        givenAKeyValueWasSet(key = "some key", value = "some value")

        whenAKeyValueIsSet(key = "some key", value = "another value")

        thenTheKeyValueIsSaved(key = "some key", value = "another value")
    }

    @Test
    fun `should get a value when an existing key is requested`() {
        givenAKeyValueWasSet()

        whenGetAKey()

        thenAValueIsGet()
    }

    @Test
    fun `should throw a business exception when a non-existent key is requested`() {
        assertThrows<KeyNotFoundException> {
            whenGetAKey()
        }
    }

    @Test
    fun `should get a value when an existing key is requested after the server is restarted`() {
        givenAKeyValueWasSet()
        givenTheServerWasRestarted()

        whenGetAKey()

        thenAValueIsGet()
    }

    private fun givenTheServerWasRestarted() {
        database = InFileDatabase("src/test/resources/database.txt")
    }

    private fun givenAKeyValueWasSet(key: String = "some key", value: String= "some value") {
        database.set(key, value)
    }

    private fun whenAKeyValueIsSet(key: String = "some key", value: String= "some value") {
        givenAKeyValueWasSet(key = key, value = value)
    }

    private fun whenGetAKey(key: String = "some key") {
        resultValue = database.get(key)
    }

    private fun thenTheKeyValueIsSaved(key: String = "some key", value: String = "some value") {
        val result = database.get(key)
        assertEquals(value, result)
    }

    private fun thenAValueIsGet(value: String = "some value") {
        assertEquals(value, resultValue)
    }
}