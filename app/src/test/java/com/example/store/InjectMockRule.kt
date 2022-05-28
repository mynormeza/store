package com.example.store

import io.mockk.MockKAnnotations
import org.junit.rules.TestRule

object InjectMockRule {
    fun create(testClass: Any) = TestRule { statement, _ ->
        MockKAnnotations.init(testClass, relaxUnitFun = true)
        statement
    }
}