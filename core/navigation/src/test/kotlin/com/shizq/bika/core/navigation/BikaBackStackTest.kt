package com.shizq.bika.core.navigation

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class BikaBackStackTest {

    private lateinit var bikaBackStack: BikaBackStack

    @Before
    fun setup() {
        bikaBackStack = BikaBackStack(TestStartKey)
    }

    @Test
    fun testStartKey() {
        assertThat(bikaBackStack.currentKey).isEqualTo(TestStartKey)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testNavigate() {
        bikaBackStack.navigate(TestKeyFirst)

        assertThat(bikaBackStack.currentKey).isEqualTo(TestKeyFirst)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testNavigateTopLevel() {
        bikaBackStack.navigate(TestTopLevelKey)

        assertThat(bikaBackStack.currentKey).isEqualTo(TestTopLevelKey)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestTopLevelKey)
    }

    @Test
    fun testNavigateSingleTop() {
        bikaBackStack.navigate(TestKeyFirst)

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
        ).inOrder()

        bikaBackStack.navigate(TestKeyFirst)

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
        ).inOrder()
    }

    @Test
    fun testNavigateTopLevelSingleTop() {
        bikaBackStack.navigate(TestTopLevelKey)
        bikaBackStack.navigate(TestKeyFirst)

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
            TestTopLevelKey,
            TestKeyFirst,
        ).inOrder()

        bikaBackStack.navigate(TestTopLevelKey)

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
            TestTopLevelKey,
        ).inOrder()
    }

    @Test
    fun testSubStack() {
        bikaBackStack.navigate(TestKeyFirst)

        assertThat(bikaBackStack.currentKey).isEqualTo(TestKeyFirst)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)

        bikaBackStack.navigate(TestKeySecond)

        assertThat(bikaBackStack.currentKey).isEqualTo(TestKeySecond)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testMultiStack() {
        // add to start stack
        bikaBackStack.navigate(TestKeyFirst)

        assertThat(bikaBackStack.currentKey).isEqualTo(TestKeyFirst)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)

        // navigate to new top level
        bikaBackStack.navigate(TestTopLevelKey)

        assertThat(bikaBackStack.currentKey).isEqualTo(TestTopLevelKey)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestTopLevelKey)

        // add to new stack
        bikaBackStack.navigate(TestKeySecond)

        assertThat(bikaBackStack.currentKey).isEqualTo(TestKeySecond)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestTopLevelKey)

        // go back to start stack
        bikaBackStack.navigate(TestStartKey)

        assertThat(bikaBackStack.currentKey).isEqualTo(TestKeyFirst)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testRestore() {
        assertThat(bikaBackStack.backStack).containsExactly(TestStartKey)

        bikaBackStack.restore(
            linkedMapOf(
                TestStartKey to mutableListOf(TestStartKey, TestKeyFirst),
                TestTopLevelKey to mutableListOf(TestTopLevelKey, TestKeySecond),
            ),
        )

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
            TestTopLevelKey,
            TestKeySecond,
        ).inOrder()

        assertThat(bikaBackStack.currentKey).isEqualTo(TestKeySecond)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestTopLevelKey)
    }

    @Test
    fun testPopOneNonTopLevel() {
        bikaBackStack.navigate(TestKeyFirst)
        bikaBackStack.navigate(TestKeySecond)

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
            TestKeySecond,
        ).inOrder()

        bikaBackStack.popLast()

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
        ).inOrder()

        assertThat(bikaBackStack.currentKey).isEqualTo(TestKeyFirst)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testPopOneTopLevel() {
        bikaBackStack.navigate(TestKeyFirst)
        bikaBackStack.navigate(TestTopLevelKey)

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
            TestTopLevelKey,
        ).inOrder()

        assertThat(bikaBackStack.currentKey).isEqualTo(TestTopLevelKey)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestTopLevelKey)

        // remove TopLevel
        bikaBackStack.popLast()

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
        ).inOrder()

        assertThat(bikaBackStack.currentKey).isEqualTo(TestKeyFirst)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun popMultipleNonTopLevel() {
        bikaBackStack.navigate(TestKeyFirst)
        bikaBackStack.navigate(TestKeySecond)

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
            TestKeySecond,
        ).inOrder()

        bikaBackStack.popLast(2)

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
        ).inOrder()

        assertThat(bikaBackStack.currentKey).isEqualTo(TestStartKey)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun popMultipleTopLevel() {
        val testTopLevelKeyTwo = object : BikaNavKey {
            override val isTopLevel: Boolean
                get() = true
        }

        // second sub-stack
        bikaBackStack.navigate(TestTopLevelKey)
        bikaBackStack.navigate(TestKeyFirst)
        // third sub-stack
        bikaBackStack.navigate(testTopLevelKeyTwo)
        bikaBackStack.navigate(TestKeySecond)

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
            TestTopLevelKey,
            TestKeyFirst,
            testTopLevelKeyTwo,
            TestKeySecond,
        ).inOrder()

        bikaBackStack.popLast(4)

        assertThat(bikaBackStack.backStack).containsExactly(
            TestStartKey,
        ).inOrder()

        assertThat(bikaBackStack.currentKey).isEqualTo(TestStartKey)
        assertThat(bikaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun throwOnEmptyBackStack() {
        assertFailsWith<IllegalStateException> {
            bikaBackStack.popLast(1)
        }
    }
}

private object TestStartKey : BikaNavKey {
    override val isTopLevel: Boolean
        get() = true
}

private object TestTopLevelKey : BikaNavKey {
    override val isTopLevel: Boolean
        get() = true
}

private object TestKeyFirst : BikaNavKey {
    override val isTopLevel: Boolean
        get() = false
}

private object TestKeySecond : BikaNavKey {
    override val isTopLevel: Boolean
        get() = false
}