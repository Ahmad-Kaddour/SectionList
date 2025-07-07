import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import app.cash.turbine.test
import com.ahmadkaddour.sectionlist.SectionListState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class SectionListStateTest {
    @Composable
    fun TestContent(
        sectionListState: SectionListState,
        modifier: Modifier = Modifier,
        listContent: LazyListScope.() -> Unit,
    ) {
        LazyColumn(
            state = sectionListState.lazyListState,
            content = listContent,
            modifier = modifier
        )
    }

    /**
     * currentSectionIndex Test
     */
    @Test
    fun `currentSectionIndex in empty list`() = runComposeUiTest {
        val state = SectionListState(emptyList(), LazyListState())
        setContent {
            TestContent(
                sectionListState = state
            ) {}
        }
        runBlocking {
            assertEquals(0, state.currentSectionIndex.first())
        }
    }

    @Test
    fun `currentSectionIndex in single section list`() = runComposeUiTest {
        val data = List(10) { "Item $it" }
        val state = SectionListState(listOf(data.size + 1), LazyListState())
        setContent {
            TestContent(
                sectionListState = state
            ) {
                item { Text("Header") }
                items(data) { Text(it) }
            }
        }
        runBlocking {
            state.currentSectionIndex.test {
                assertEquals(0, awaitItem())
                state.lazyListState.scrollToItem(1)
                expectNoEvents()
                state.lazyListState.scrollToItem(5)
                expectNoEvents()
                state.lazyListState.scrollToItem(10)
                expectNoEvents()
            }
        }
    }

    @Test
    fun `currentSectionIndex in list with multiple sections`() = runComposeUiTest {
        val data = List(5) { section -> List(10) { item -> "Section $section, Item $item" } }
        val state = SectionListState(data.map { it.size }, LazyListState())
        setContent {
            TestContent(
                sectionListState = state,
                modifier = Modifier.height(100.dp)
            ) {
                for (section in data) {
                    items(section) {
                        Text(
                            text = it,
                            modifier = Modifier.height(100.dp)
                        )
                    }
                }
            }
        }
        runBlocking {
            state.currentSectionIndex.test {
                assertEquals(0, awaitItem())
                state.lazyListState.scrollToItem(4)
                expectNoEvents()
                state.lazyListState.scrollToItem(10)
                assertEquals(1, awaitItem())
                state.lazyListState.scrollToItem(11)
                expectNoEvents()
                state.lazyListState.scrollToItem(20)
                assertEquals(2, awaitItem())
                state.lazyListState.scrollToItem(29)
                expectNoEvents()
                state.lazyListState.scrollToItem(30)
                assertEquals(3, awaitItem())
                state.lazyListState.scrollToItem(31)
                expectNoEvents()
                state.lazyListState.scrollToItem(40)
                assertEquals(4, awaitItem())
                state.lazyListState.scrollToItem(49)
                expectNoEvents()
                state.lazyListState.scrollToItem(30)
                assertEquals(3, awaitItem())
                state.lazyListState.scrollToItem(0)
                assertEquals(0, awaitItem())
            }
        }
    }

    @Test
    fun `currentSectionIndex when scroll reach end`() = runComposeUiTest {
        val data = List(3) { section -> List(10) { item -> "Section $section, Item $item" } }
        val state = SectionListState(data.map { it.size }, LazyListState())
        setContent {
            TestContent(
                sectionListState = state,
                modifier = Modifier.height(200.dp)
            ) {
                for (section in data) {
                    items(section) {
                        Text(
                            text = it,
                            modifier = Modifier.height(10.dp)
                        )
                    }
                }
            }
        }
        runBlocking {
            state.currentSectionIndex.test {
                assertEquals(0, awaitItem())
                state.lazyListState.scrollToItem(10)
                assertEquals(2, awaitItem())
            }
        }
    }

    @Test
    fun `currentSectionIndex when the first item is partially visible`() = runComposeUiTest {
        val state = SectionListState(listOf(1, 1, 1), LazyListState())
        setContent {
            TestContent(
                sectionListState = state,
                modifier = Modifier.height(20.dp)
            ) {
                item { Text("Section 1", modifier = Modifier.height(20.dp)) }
                item { Text("Section 2", modifier = Modifier.height(10.dp)) }
                item { Text("Section 3", modifier = Modifier.height(15.dp)) }
            }
        }
        runBlocking {
            state.currentSectionIndex.test {
                assertEquals(0, awaitItem())
                state.lazyListState.scrollBy(10f)
                assertEquals(1, awaitItem())
                state.lazyListState.scrollBy(10f)
                expectNoEvents()
                state.lazyListState.scrollBy(5f)
                assertEquals(2, awaitItem())
            }
        }
    }

    @Test
    fun `currentSectionIndex when there is no fully visible item`() = runComposeUiTest {
        val state = SectionListState(listOf(1, 1, 1), LazyListState())
        setContent {
            TestContent(
                sectionListState = state,
                modifier = Modifier.height(20.dp)
            ) {
                item { Text("Section 1", modifier = Modifier.height(20.dp)) }
                item { Text("Section 2", modifier = Modifier.height(10.dp)) }
                item { Text("Section 3", modifier = Modifier.height(15.dp)) }
            }
        }
        runBlocking {
            state.currentSectionIndex.test {
                assertEquals(0, awaitItem())
                state.lazyListState.scrollBy(5f)
                expectNoEvents()
                state.lazyListState.scrollBy(16f)
                assertEquals(1, awaitItem())
            }
        }
    }

    /**
     * firstVisibleSectionIndex Test
     */
    @Test
    fun `firstVisibleSectionIndex in empty list`() = runComposeUiTest {
        val state = SectionListState(emptyList(), LazyListState())
        setContent {
            TestContent(
                sectionListState = state
            ) {}
        }
        runBlocking {
            assertEquals(0, state.firstVisibleSectionIndex)
        }
    }

    @Test
    fun `firstVisibleSectionIndex in single section list`() = runComposeUiTest {
        val data = List(10) { "Item $it" }
        val state = SectionListState(listOf(data.size), LazyListState())
        setContent {
            TestContent(
                sectionListState = state
            ) {
                items(data) { Text(it) }
            }
        }
        runBlocking {
            assertEquals(0, state.firstVisibleSectionIndex)
            state.lazyListState.scrollToItem(1)
            assertEquals(0, state.firstVisibleSectionIndex)
            state.lazyListState.scrollToItem(9)
            assertEquals(0, state.firstVisibleSectionIndex)
        }
    }

    @Test
    fun `firstVisibleSectionIndex in list with multiple sections`() = runComposeUiTest {
        val data = List(3) { section -> List(10) { item -> "Section $section, Item $item" } }
        val state = SectionListState(data.map { it.size }, LazyListState())
        setContent {
            TestContent(
                sectionListState = state,
                modifier = Modifier.height(100.dp)
            ) {
                for (section in data) {
                    items(section) {
                        Text(
                            text = it,
                            modifier = Modifier.height(100.dp)
                        )
                    }
                }
            }
        }
        runBlocking {
            assertEquals(0, state.firstVisibleSectionIndex)
            state.lazyListState.scrollToItem(4)
            assertEquals(0, state.firstVisibleSectionIndex)
            state.lazyListState.scrollToItem(10)
            assertEquals(1, state.firstVisibleSectionIndex)
            state.lazyListState.scrollToItem(19)
            assertEquals(1, state.firstVisibleSectionIndex)
            state.lazyListState.scrollToItem(20)
            assertEquals(2, state.firstVisibleSectionIndex)
        }
    }

    @Test
    fun `firstVisibleSectionIndex when scroll reach end`() = runComposeUiTest {
        val data = List(3) { section -> List(10) { item -> "Section $section, Item $item" } }
        val state = SectionListState(data.map { it.size }, LazyListState())
        setContent {
            TestContent(
                sectionListState = state,
                modifier = Modifier.height(200.dp)
            ) {
                for (section in data) {
                    items(section) {
                        Text(
                            text = it,
                            modifier = Modifier.height(10.dp)
                        )
                    }
                }
            }
        }
        runBlocking {
            assertEquals(0, state.firstVisibleSectionIndex)
            state.lazyListState.scrollToItem(10)
            assertEquals(1, state.firstVisibleSectionIndex)
            state.lazyListState.scrollToItem(29)
            assertEquals(1, state.firstVisibleSectionIndex)
        }
    }


    /**
     * lastVisibleSectionIndex Test
     */
    @Test
    fun `lastVisibleSectionIndex in empty list`() = runComposeUiTest {
        val state = SectionListState(emptyList(), LazyListState())
        setContent {
            TestContent(
                sectionListState = state
            ) {}
        }
        runBlocking {
            assertEquals(0, state.lastVisibleSectionIndex)
        }
    }

    @Test
    fun `lastVisibleSectionIndex in single section list`() = runComposeUiTest {
        val data = List(10) { "Item $it" }
        val state = SectionListState(listOf(data.size), LazyListState())
        setContent {
            TestContent(
                sectionListState = state
            ) {
                items(data) { Text(it) }
            }
        }
        runBlocking {
            assertEquals(0, state.lastVisibleSectionIndex)
            state.lazyListState.scrollToItem(1)
            assertEquals(0, state.lastVisibleSectionIndex)
            state.lazyListState.scrollToItem(9)
            assertEquals(0, state.lastVisibleSectionIndex)
        }
    }

    @Test
    fun `lastVisibleSectionIndex in list with multiple sections`() = runComposeUiTest {
        val data = List(3) { section -> List(10) { item -> "Section $section, Item $item" } }
        val state = SectionListState(data.map { it.size }, LazyListState())
        setContent {
            TestContent(
                sectionListState = state,
                modifier = Modifier.height(100.dp)
            ) {
                for (section in data) {
                    items(section) {
                        Text(
                            text = it,
                            modifier = Modifier.height(100.dp)
                        )
                    }
                }
            }
        }
        runBlocking {
            assertEquals(0, state.lastVisibleSectionIndex)
            state.lazyListState.scrollToItem(4)
            assertEquals(0, state.lastVisibleSectionIndex)
            state.lazyListState.scrollToItem(10)
            assertEquals(1, state.lastVisibleSectionIndex)
            state.lazyListState.scrollToItem(19)
            assertEquals(1, state.lastVisibleSectionIndex)
            state.lazyListState.scrollToItem(20)
            assertEquals(2, state.lastVisibleSectionIndex)
        }
    }

    @Test
    fun `lastVisibleSectionIndex when scroll reach end`() = runComposeUiTest {
        val data = List(3) { section -> List(10) { item -> "Section $section, Item $item" } }
        val state = SectionListState(data.map { it.size }, LazyListState())
        setContent {
            TestContent(
                sectionListState = state,
                modifier = Modifier.height(200.dp)
            ) {
                for (section in data) {
                    items(section) {
                        Text(
                            text = it,
                            modifier = Modifier.height(10.dp)
                        )
                    }
                }
            }
        }
        runBlocking {
            assertEquals(1, state.lastVisibleSectionIndex)
            state.lazyListState.scrollToItem(1)
            assertEquals(2, state.lastVisibleSectionIndex)
            state.lazyListState.scrollToItem(10)
            assertEquals(2, state.lastVisibleSectionIndex)
            state.lazyListState.scrollToItem(29)
            assertEquals(2, state.lastVisibleSectionIndex)
        }
    }

    /**
     * scrollToSection Test
     */
    @Test
    fun `scrollToSection in empty list`() = runComposeUiTest {
        val state = SectionListState(emptyList(), LazyListState())
        setContent {
            TestContent(
                sectionListState = state
            ) {}
        }
        runBlocking {
            assertEquals(0, state.currentSectionIndex.first())
            state.scrollToSection(1)
            assertEquals(0, state.currentSectionIndex.first())
        }
    }

    @Test
    fun `scrollToSection in list with multiple sections`() = runComposeUiTest {
        val data = List(3) { section -> List(10) { item -> "Section $section, Item $item" } }
        val state = SectionListState(data.map { it.size }, LazyListState())
        setContent {
            TestContent(
                sectionListState = state,
                modifier = Modifier.height(100.dp)
            ) {
                for (section in data) {
                    items(section) {
                        Text(
                            text = it,
                            modifier = Modifier.height(100.dp)
                        )
                    }
                }
            }
        }
        runBlocking {
            state.currentSectionIndex.test {
                assertEquals(0, awaitItem())
                state.scrollToSection(0)
                expectNoEvents()
                state.scrollToSection(1)
                assertEquals(1, awaitItem())
                assertEquals(1, state.firstVisibleSectionIndex)
                assertEquals(1, state.lastVisibleSectionIndex)
                state.scrollToSection(2)
                assertEquals(2, awaitItem())
                assertEquals(2, state.firstVisibleSectionIndex)
                assertEquals(2, state.lastVisibleSectionIndex)
                state.scrollToSection(0)
                assertEquals(0, awaitItem())
                assertEquals(0, state.firstVisibleSectionIndex)
                assertEquals(0, state.lastVisibleSectionIndex)
            }
        }
    }

    @Test
    fun `scrollToSection section out of bound`() = runComposeUiTest {
        val data = List(2) { section -> List(10) { item -> "Section $section, Item $item" } }
        val state = SectionListState(data.map { it.size }, LazyListState())
        setContent {
            TestContent(
                sectionListState = state,
                modifier = Modifier.height(100.dp)
            ) {
                for (section in data) {
                    items(section) {
                        Text(
                            text = it,
                            modifier = Modifier.height(100.dp)
                        )
                    }
                }
            }
        }
        runBlocking {
            state.currentSectionIndex.test {
                assertEquals(0, awaitItem())
                state.scrollToSection(-1)
                expectNoEvents()
                state.scrollToSection(2)
                assertEquals(1, awaitItem())
            }
        }
    }

    @Test
    fun `scrollToSection in list with empty sections`() = runComposeUiTest {
        val data = listOf(
            emptyList(),
            List(10) { item -> "Section 2, Item $item" },
            emptyList(),
            List(10) { item -> "Section 4, Item $item" },
        )
        val state = SectionListState(data.map { it.size }, LazyListState())
        setContent {
            TestContent(
                sectionListState = state,
                modifier = Modifier.height(100.dp)
            ) {
                for (section in data) {
                    items(section) {
                        Text(
                            text = it,
                            modifier = Modifier.height(100.dp)
                        )
                    }
                }
            }
        }
        runBlocking {
            state.currentSectionIndex.test {
                assertEquals(1, awaitItem())
                assertEquals(1, state.firstVisibleSectionIndex)
                assertEquals(1, state.lastVisibleSectionIndex)
                state.scrollToSection(2)
                assertEquals(2, awaitItem())
                assertEquals(3, state.firstVisibleSectionIndex)
                assertEquals(3, state.lastVisibleSectionIndex)
                state.scrollToSection(3)
                assertEquals(3, awaitItem())
                state.resetProgrammaticSectionSelection()
                state.lazyListState.scrollToItem(0)
                assertEquals(1, awaitItem())
                assertEquals(1, state.firstVisibleSectionIndex)
                assertEquals(1, state.lastVisibleSectionIndex)
            }
        }
    }
}