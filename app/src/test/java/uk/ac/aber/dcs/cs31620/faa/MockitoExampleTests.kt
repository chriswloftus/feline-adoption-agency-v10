package uk.ac.aber.dcs.cs31620.faa

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class MockitoExampleTests {

    @Mock
    private var mockedList:MutableList<String> = mock(MutableList::class.java) as MutableList<String>

    @Before
    fun setup() {
        // We can use the @Mock annotation instead of doing this!
        // We need the MockitoJUnit rule for this to work.
    }

    @Test
    fun verificationExamplesWithInterface(){
        val mockedListInterface:MutableList<String> = mock(MutableList::class.java) as MutableList<String>
        mockedListInterface.add("one")
        mockedListInterface.clear()

        // Mockito remembers mock interactions, so we can verify that
        // the following operations were performed on the mocked list
        verify(mockedListInterface).add("one")
        verify(mockedListInterface).clear()
    }

    @Test(expected = RuntimeException::class)
    fun stubbingExamplesWithClasses() {
        `when`(mockedList[0]).thenReturn("first")
        `when`(mockedList[1]).thenThrow(RuntimeException())

        assertEquals(mockedList[0], "first")

        // Should return null because no stub for 99
        assertNull(mockedList[99])

        //following throws runtime exception
        mockedList[1]

        // All methods that return something will return a default: null, primitive, empty collection...
    }

    @Test
    fun verifyingNumberOfInvocations(){
        mockedList.add("once")

        mockedList.add("twice")
        mockedList.add("twice")

        mockedList.add("three times")
        mockedList.add("three times")
        mockedList.add("three times")

        //following two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once")
        verify(mockedList, times(1)).add("once")

        //exact number of invocations verification
        verify(mockedList, times(2)).add("twice")
        verify(mockedList, times(3)).add("three times")

        //verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened")

        //verification using atLeast()/atMost()
        verify(mockedList, atLeastOnce()).add("three times")
        verify(mockedList, atLeast(2)).add("three times")
        verify(mockedList, atMost(5)).add("three times")
    }

    @Test(expected = java.lang.RuntimeException::class)
    fun forceThrowingException(){
        doThrow(RuntimeException()).`when`(mockedList).clear()

        //following throws RuntimeException:
        mockedList.clear()
    }

    @Test
    fun verifyingOrderOfMockMehodInvocations() {
        val singleMock: MutableList<String> = mock(MutableList::class.java) as MutableList<String>

        //using a single mock
        singleMock.add("was added first")
        singleMock.add("was added second")

        //create an inOrder verifier for a single mock
        var inOrder = inOrder(singleMock)

        //following will make sure that add is first called with "was added first, then with "was added second"
        inOrder.verify(singleMock).add("was added first")
        inOrder.verify(singleMock).add("was added second")

        // B. Multiple mocks that must be used in a particular order
        val firstMock: MutableList<String> = mock(MutableList::class.java) as MutableList<String>
        val secondMock: MutableList<String> = mock(MutableList::class.java) as MutableList<String>

        //using mocks
        firstMock.add("was called first")
        secondMock.add("was called second")

        //create inOrder object passing any mocks that need to be verified in order
        inOrder = inOrder(firstMock, secondMock)

        //following will make sure that firstMock was called before secondMock
        inOrder.verify(firstMock).add("was called first")
        inOrder.verify(secondMock).add("was called second")

    }

    @Test
    fun verifyingInvocationDidNotHappen(){
        val mockOne: MutableList<String> = mock(MutableList::class.java) as MutableList<String>
        val mockThree: MutableList<String> = mock(MutableList::class.java) as MutableList<String>

        //using mocks - only mockOne is interacted
        mockOne.add("one")

        //ordinary verification
        verify(mockOne).add("one")

        //verify that method was never called on a mock
        verify(mockOne, never()).add("two")

        //verify that other mocks were not interacted
        // Fails if this is uncommented
        //verifyNoInteractions(mockOne, mockThree)
    }

    @Test(expected = java.lang.RuntimeException::class)
    fun stubbingConsecutiveCalls(){
        `when`(mockedList[0])
            .thenReturn("foo")
            .thenThrow(RuntimeException())

        assertEquals("foo", mockedList[0])

        mockedList[0] // Generates the exception
    }

    @Test
    fun stubbingConsecutiveCallsTakeTwo(){
        `when`(mockedList[0])
            .thenReturn("one", "two", "three")

        assertEquals("one", mockedList[0])
        assertEquals("two", mockedList[0])
        assertEquals("three", mockedList[0])
    }

    @Test
    fun makingRequestsWithTimeouts(){
        `when`<Any>(mockedList[0]).thenReturn("first")

        mockedList[0]

        //passes when get(0) is called no later than within 100 ms
        //exits immediately when verification is satisfied (e.g. may not wait full 100 ms)

        //passes when get(0) is called no later than within 100 ms
        //exits immediately when verification is satisfied (e.g. may not wait full 100 ms)
        verify(mockedList, timeout(100))[0]
        //above is an alias to:
        //above is an alias to:
        verify(mockedList, timeout(100).times(1))[0]

        mockedList[0]

        //passes as soon as get(0) has been called 2 times under 100 ms

        //passes as soon as get(0) has been called 2 times under 100 ms
        verify(mockedList, timeout(100).times(2))[0]

        //equivalent: this also passes as soon as get(0) has been called 2 times under 100 ms

        //equivalent: this also passes as soon as get(0) has been called 2 times under 100 ms
        verify(mockedList, timeout(100).atLeast(2))[0]

    }

}