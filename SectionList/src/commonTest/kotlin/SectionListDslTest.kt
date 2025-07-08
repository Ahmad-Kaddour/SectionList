import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.ahmadkaddour.sectionlist.section
import com.ahmadkaddour.sectionlist.sectionIndexed
import com.ahmadkaddour.sectionlist.sections
import com.ahmadkaddour.sectionlist.sectionsIndexed
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class SectionListDslTest {
    @Test
    fun `section with header and footer`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                section(
                    itemsCount = 10,
                    headerContent = { Text("Header") },
                    headerContentType = 1,
                    headerKey = 100,
                    footerContent = { Text("Footer") },
                    footerKey = 200,
                    footerContentType = 2,
                    itemContent = { i -> Text("Item $i") },
                    itemKey = { i -> i },
                    itemContentType = { 3 }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(12)
        listNode.onChildren().fetchSemanticsNodes().subList(1, 11).forEachIndexed { i, node ->
            assertEquals(
                "Item $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
        listNode.onChildren().onFirst().assertTextEquals("Header")
        listNode.onChildren().onLast().assertTextEquals("Footer")
    }

    @Test
    fun `section with items only`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                section(
                    itemsCount = 10,
                    itemContent = { i -> Text("Item $i") },
                    itemKey = { i -> i },
                    itemContentType = { 3 }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(10)
        listNode.onChildren().fetchSemanticsNodes().forEachIndexed { i, node ->
            assertEquals(
                "Item $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `section with header only`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                section(
                    itemsCount = 10,
                    headerContent = { Text("Header") },
                    headerContentType = 1,
                    headerKey = 100,
                    itemContent = { i -> Text("Item $i") },
                    itemKey = { i -> i },
                    itemContentType = { 3 }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(11)
        listNode.onChildren().onFirst().assertTextEquals("Header")
        listNode.onChildren().fetchSemanticsNodes().subList(1, 11).forEachIndexed { i, node ->
            assertEquals(
                "Item $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `section with sticky header`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                section(
                    itemsCount = 10,
                    headerContent = { Text("Header") },
                    headerContentType = 1,
                    headerKey = 100,
                    isStickyHeader = true,
                    itemContent = { i -> Text("Item $i") },
                    itemKey = { i -> i },
                    itemContentType = { 3 }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(11)
        listNode.onChildren().fetchSemanticsNodes().subList(0, 10).forEachIndexed { i, node ->
            assertEquals(
                "Item $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
        listNode.onChildren().onLast().assertTextEquals("Header")
    }

    @Test
    fun `section with footer only`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                section(
                    itemsCount = 10,
                    footerContent = { Text("Footer") },
                    footerKey = 200,
                    footerContentType = 2,
                    itemContent = { i -> Text("Item $i") },
                    itemKey = { i -> i },
                    itemContentType = { 3 }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(11)
        listNode.onChildren().onLast().assertTextEquals("Footer")
        listNode.onChildren().fetchSemanticsNodes().subList(0, 10).forEachIndexed { i, node ->
            assertEquals(
                "Item $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }


    @Test
    fun `section using data list with header and footer`() = runComposeUiTest {
        val data = List(10) { i -> "Item $i" }
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                section(
                    items = data,
                    headerContent = { Text("Header") },
                    headerContentType = 1,
                    headerKey = 1,
                    footerContent = { Text("Footer") },
                    footerKey = 2,
                    footerContentType = 2,
                    itemContent = { item -> Text(item) },
                    itemKey = { it },
                    itemContentType = { 3 }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(12)
        listNode.onChildren().fetchSemanticsNodes().subList(1, 11).forEachIndexed { i, node ->
            assertEquals(
                data[i],
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
        listNode.onChildren().onFirst().assertTextEquals("Header")
        listNode.onChildren().onLast().assertTextEquals("Footer")
    }

    @Test
    fun `section using data list with items only`() = runComposeUiTest {
        val data = List(10) { i -> "Item $i" }
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                section(
                    items = data,
                    itemContent = { item -> Text(item) },
                    itemKey = { it },
                    itemContentType = { 0 }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(10)
        listNode.onChildren().fetchSemanticsNodes().forEachIndexed { i, node ->
            assertEquals(
                data[i],
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `section using data list with header only`() = runComposeUiTest {
        val data = List(10) { i -> "Item $i" }
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                section(
                    items = data,
                    headerContent = { Text("Header") },
                    headerContentType = 1,
                    headerKey = 100,
                    itemContent = { item -> Text(item) },
                    itemKey = { it },
                    itemContentType = { 2 }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(11)
        listNode.onChildren().onFirst().assertTextEquals("Header")
        listNode.onChildren().fetchSemanticsNodes().subList(1, 11).forEachIndexed { i, node ->
            assertEquals(
                data[i],
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `section using data list with footer only`() = runComposeUiTest {
        val data = List(10) { i -> "Item $i" }
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                section(
                    items = data,
                    footerContent = { Text("Footer") },
                    footerKey = 1,
                    footerContentType = 1,
                    itemContent = { item -> Text(item) },
                    itemKey = { it },
                    itemContentType = { 2 }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(11)
        listNode.onChildren().onLast().assertTextEquals("Footer")
        listNode.onChildren().fetchSemanticsNodes().subList(0, 10).forEachIndexed { i, node ->
            assertEquals(
                data[i],
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `section with empty data list`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                section(
                    items = emptyList<Unit>(),
                    itemContent = { _ -> },
                    itemKey = { it },
                    itemContentType = { 1 }
                )
            }
        }

        onNodeWithTag("list").onChildren().assertCountEquals(0)
    }


    @Test
    fun `sectionIndexed with header and footer`() = runComposeUiTest {
        val data = List(10) { i -> "Item $i" }
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionIndexed(
                    items = data,
                    headerContent = { Text("Header") },
                    headerContentType = 1,
                    headerKey = 1,
                    footerContent = { Text("Footer") },
                    footerKey = 2,
                    footerContentType = 2,
                    itemContent = { i, item ->
                        assertEquals(item, data[i])
                        Text(data[i])
                    },
                    itemKey = { i, item ->
                        assertEquals(item, data[i])
                        item
                    },
                    itemContentType = { i, item ->
                        assertEquals(item, data[i])
                        3
                    }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(12)
        listNode.onChildren().fetchSemanticsNodes().subList(1, 11).forEachIndexed { i, node ->
            assertEquals(
                data[i],
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
        listNode.onChildren().onFirst().assertTextEquals("Header")
        listNode.onChildren().onLast().assertTextEquals("Footer")
    }

    @Test
    fun `sectionIndexed with items only`() = runComposeUiTest {
        val data = List(10) { i -> "Item $i" }
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionIndexed(
                    items = data,
                    itemContent = { i, item ->
                        assertEquals(item, data[i])
                        Text(data[i])
                    },
                    itemKey = { i, item ->
                        assertEquals(item, data[i])
                        item
                    },
                    itemContentType = { i, item ->
                        assertEquals(item, data[i])
                        1
                    }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(10)
        listNode.onChildren().fetchSemanticsNodes().forEachIndexed { i, node ->
            assertEquals(
                data[i],
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `sectionIndexed with header only`() = runComposeUiTest {
        val data = List(10) { i -> "Item $i" }
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionIndexed(
                    items = data,
                    headerContent = { Text("Header") },
                    headerContentType = 1,
                    headerKey = 1,
                    itemContent = { i, item ->
                        assertEquals(item, data[i])
                        Text(data[i])
                    },
                    itemKey = { i, item ->
                        assertEquals(item, data[i])
                        item
                    },
                    itemContentType = { i, item ->
                        assertEquals(item, data[i])
                        2
                    }
                )
            }
        }


        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(11)
        listNode.onChildren().onFirst().assertTextEquals("Header")
        listNode.onChildren().fetchSemanticsNodes().subList(1, 11).forEachIndexed { i, node ->
            assertEquals(
                data[i],
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `sectionIndexed with footer only`() = runComposeUiTest {
        val data = List(10) { i -> "Item $i" }
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionIndexed(
                    items = data,
                    footerContent = { Text("Footer") },
                    footerKey = 1,
                    footerContentType = 1,
                    itemContent = { i, item ->
                        assertEquals(item, data[i])
                        Text(data[i])
                    },
                    itemKey = { i, item ->
                        assertEquals(item, data[i])
                        item
                    },
                    itemContentType = { i, item ->
                        assertEquals(item, data[i])
                        2
                    }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(11)
        listNode.onChildren().onLast().assertTextEquals("Footer")
        listNode.onChildren().fetchSemanticsNodes().subList(0, 10).forEachIndexed { i, node ->
            assertEquals(
                data[i],
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `sectionIndexed with empty data list`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionIndexed(
                    items = emptyList<Unit>(),
                    itemContent = { i, item -> },
                    itemKey = { i, item -> },
                    itemContentType = { i, item -> }
                )
            }
        }

        onNodeWithTag("list").onChildren().assertCountEquals(0)
    }

    @Test
    fun `test sections`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sections(2) { s ->
                    section(5) { i ->
                        Text("Section $s Item $i")
                    }
                }
            }
        }

        onNodeWithTag("list").onChildren().assertCountEquals(10)
        onNodeWithTag("list").onChildren().fetchSemanticsNodes().forEachIndexed { i, node ->
            assertEquals(
                "Section ${i / 5} Item ${i % 5}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `sections with 0 count`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sections(0) { s ->
                    section(5) { i ->
                        Text("Section $s Item $i")
                    }
                }
            }
        }

        onNodeWithTag("list").onChildren().assertCountEquals(0)
    }


    @Test
    fun `sections with header and footer`() = runComposeUiTest {
        val data = listOf(
            listOf("Section 0 Item 0", "Section 0 Item 1", "Section 0 Item 2"),
            listOf("Section 1 Item 0", "Section 1 Item 1", "Section 1 Item 2"),
        )
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sections(
                    sections = data,
                    headerContent = { Text("Header $it") },
                    headerKey = { "header $it" },
                    headerContentType = { "header" },
                    footerContent = { Text("Footer $it") },
                    footerKey = { "footer $it" },
                    footerContentType = { "footer" },
                    itemContent = { Text(it) },
                    itemKey = { "item $it" },
                    itemContentType = { "item" }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(10)

        val semanticsNodes = listNode.onChildren().fetchSemanticsNodes()
        semanticsNodes.filterIndexed { i, _ -> i % 5 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Header $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> (i - 4) % 5 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Footer $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> (i % 5 != 0) && ((i - 4) % 5 != 0) }
            .forEachIndexed { i, node ->
                assertEquals(
                    "Section ${i / 3} Item ${i % 3}",
                    node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
                )
            }
    }

    @Test
    fun `sections with header`() = runComposeUiTest {
        val data = listOf(
            listOf("Section 0 Item 0", "Section 0 Item 1", "Section 0 Item 2"),
            listOf("Section 1 Item 0", "Section 1 Item 1", "Section 1 Item 2"),
        )
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sections(
                    sections = data,
                    headerContent = { Text("Header $it") },
                    headerKey = { "header $it" },
                    headerContentType = { "header" },
                    itemContent = { Text(it) },
                    itemKey = { "item $it" },
                    itemContentType = { "item" }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(8)

        val semanticsNodes = listNode.onChildren().fetchSemanticsNodes()
        semanticsNodes.filterIndexed { i, _ -> i % 4 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Header $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> i % 4 != 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Section ${i / 3} Item ${i % 3}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `sections with footer`() = runComposeUiTest {
        val data = listOf(
            listOf("Section 0 Item 0", "Section 0 Item 1", "Section 0 Item 2"),
            listOf("Section 1 Item 0", "Section 1 Item 1", "Section 1 Item 2"),
        )
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sections(
                    sections = data,
                    footerContent = { Text("Footer $it") },
                    footerKey = { "footer $it" },
                    footerContentType = { "footer" },
                    itemContent = { Text(it) },
                    itemKey = { "item $it" },
                    itemContentType = { "item" }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(8)

        val semanticsNodes = listNode.onChildren().fetchSemanticsNodes()
        semanticsNodes.filterIndexed { i, _ -> (i - 3) % 4 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Footer $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> (i - 3) % 4 != 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Section ${i / 3} Item ${i % 3}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `sections with empty data`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sections(
                    sections = emptyList<List<String>>(),
                    itemContent = { Text(it) },
                )
            }
        }

        onNodeWithTag("list").onChildren().assertCountEquals(0)
    }


    @Test
    fun `sections using sections list with header and footer`() = runComposeUiTest {
        val data = listOf("Section 0", "Section 1")
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sections(
                    sections = data,
                    sectionItems = { s -> listOf("$s Item 0", "$s Item 1", "$s Item 2") },
                    headerContent = { Text("Header $it") },
                    headerKey = { "header $it" },
                    headerContentType = { "header" },
                    footerContent = { Text("Footer $it") },
                    footerKey = { "footer $it" },
                    footerContentType = { "footer" },
                    itemContent = { Text(it) },
                    itemKey = { "item $it" },
                    itemContentType = { "item" }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(10)

        val semanticsNodes = listNode.onChildren().fetchSemanticsNodes()
        semanticsNodes.filterIndexed { i, _ -> i % 5 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Header ${data[i]}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> (i - 4) % 5 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Footer ${data[i]}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> (i % 5 != 0) && ((i - 4) % 5 != 0) }
            .forEachIndexed { i, node ->
                assertEquals(
                    "Section ${i / 3} Item ${i % 3}",
                    node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
                )
            }
    }

    @Test
    fun `sections using sections list with header`() = runComposeUiTest {
        val data = listOf("Section 0", "Section 1")
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sections(
                    sections = data,
                    sectionItems = { s -> listOf("$s Item 0", "$s Item 1", "$s Item 2") },
                    headerContent = { Text("Header $it") },
                    headerKey = { "header $it" },
                    headerContentType = { "header" },
                    itemContent = { Text(it) },
                    itemKey = { "item $it" },
                    itemContentType = { "item" }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(8)

        val semanticsNodes = listNode.onChildren().fetchSemanticsNodes()
        semanticsNodes.filterIndexed { i, _ -> i % 4 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Header ${data[i]}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> i % 4 != 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Section ${i / 3} Item ${i % 3}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `sections using sections list with footer`() = runComposeUiTest {
        val data = listOf("Section 0", "Section 1")
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sections(
                    sections = data,
                    sectionItems = { s -> listOf("$s Item 0", "$s Item 1", "$s Item 2") },
                    footerContent = { Text("Footer $it") },
                    footerKey = { "footer $it" },
                    footerContentType = { "footer" },
                    itemContent = { Text(it) },
                    itemKey = { "item $it" },
                    itemContentType = { "item" }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(8)

        val semanticsNodes = listNode.onChildren().fetchSemanticsNodes()
        semanticsNodes.filterIndexed { i, _ -> (i - 3) % 4 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Footer ${data[i]}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> (i - 3) % 4 != 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Section ${i / 3} Item ${i % 3}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `sections using sections with empty data`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sections(
                    sections = emptyList<String>(),
                    sectionItems = { emptyList<String>() },
                    itemContent = { Text(it) },
                )
            }
        }

        onNodeWithTag("list").onChildren().assertCountEquals(0)
    }


    @Test
    fun `sectionsIndexed with header and footer`() = runComposeUiTest {
        val data = listOf(
            listOf("Section 0 Item 0", "Section 0 Item 1", "Section 0 Item 2"),
            listOf("Section 1 Item 0", "Section 1 Item 1", "Section 1 Item 2"),
        )
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionsIndexed(
                    sections = data,
                    headerContent = { Text("Header $it") },
                    headerKey = { "header $it" },
                    headerContentType = { "header" },
                    footerContent = { Text("Footer $it") },
                    footerKey = { "footer $it" },
                    footerContentType = { "footer" },
                    itemContent = { sectionIndex, itemIndex, item ->
                        assertEquals("Section $sectionIndex Item $itemIndex", item)
                        Text(item)
                    },
                    itemKey = { sectionIndex, itemIndex, item ->
                        assertEquals("Section $sectionIndex Item $itemIndex", item)
                        "item $item"
                    },
                    itemContentType = { sectionIndex, itemIndex, item ->
                        assertEquals("Section $sectionIndex Item $itemIndex", item)
                        "item"
                    }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(10)

        val semanticsNodes = listNode.onChildren().fetchSemanticsNodes()
        semanticsNodes.filterIndexed { i, _ -> i % 5 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Header $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> (i - 4) % 5 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Footer $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> (i % 5 != 0) && ((i - 4) % 5 != 0) }
            .forEachIndexed { i, node ->
                assertEquals(
                    "Section ${i / 3} Item ${i % 3}",
                    node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
                )
            }
    }

    @Test
    fun `sectionsIndexed with header`() = runComposeUiTest {
        val data = listOf(
            listOf("Section 0 Item 0", "Section 0 Item 1", "Section 0 Item 2"),
            listOf("Section 1 Item 0", "Section 1 Item 1", "Section 1 Item 2"),
        )
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionsIndexed(
                    sections = data,
                    headerContent = { Text("Header $it") },
                    headerKey = { "header $it" },
                    headerContentType = { "header" },
                    itemContent = { sectionIndex, itemIndex, item ->
                        assertEquals("Section $sectionIndex Item $itemIndex", item)
                        Text(item)
                    },
                    itemKey = { sectionIndex, itemIndex, item ->
                        assertEquals("Section $sectionIndex Item $itemIndex", item)
                        "item $item"
                    },
                    itemContentType = { sectionIndex, itemIndex, item ->
                        assertEquals("Section $sectionIndex Item $itemIndex", item)
                        "item"
                    }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(8)

        val semanticsNodes = listNode.onChildren().fetchSemanticsNodes()
        semanticsNodes.filterIndexed { i, _ -> i % 4 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Header $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> i % 4 != 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Section ${i / 3} Item ${i % 3}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `sectionsIndexed with footer`() = runComposeUiTest {
        val data = listOf(
            listOf("Section 0 Item 0", "Section 0 Item 1", "Section 0 Item 2"),
            listOf("Section 1 Item 0", "Section 1 Item 1", "Section 1 Item 2"),
        )
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionsIndexed(
                    sections = data,
                    footerContent = { Text("Footer $it") },
                    footerKey = { "footer $it" },
                    footerContentType = { "footer" },
                    itemContent = { sectionIndex, itemIndex, item ->
                        assertEquals("Section $sectionIndex Item $itemIndex", item)
                        Text(item)
                    },
                    itemKey = { sectionIndex, itemIndex, item ->
                        assertEquals("Section $sectionIndex Item $itemIndex", item)
                        "item $item"
                    },
                    itemContentType = { sectionIndex, itemIndex, item ->
                        assertEquals("Section $sectionIndex Item $itemIndex", item)
                        "item"
                    }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(8)

        val semanticsNodes = listNode.onChildren().fetchSemanticsNodes()
        semanticsNodes.filterIndexed { i, _ -> (i - 3) % 4 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Footer $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> (i - 3) % 4 != 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Section ${i / 3} Item ${i % 3}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `sectionsIndexed with empty data`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionsIndexed(
                    sections = emptyList<List<String>>(),
                    itemContent = { _, _, item -> Text(item) },
                )
            }
        }

        onNodeWithTag("list").onChildren().assertCountEquals(0)
    }


    @Test
    fun `sectionsIndexed using sections list with header and footer`() = runComposeUiTest {
        val data = listOf("Section 0", "Section 1")
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionsIndexed(
                    sections = data,
                    sectionItems = { i, s ->
                        assertEquals(data[i], s)
                        listOf("$s Item 0", "$s Item 1", "$s Item 2")
                    },
                    headerContent = { i, s ->
                        assertEquals(data[i], s)
                        Text("Header $i")
                    },
                    headerKey = { i, s ->
                        assertEquals(data[i], s)
                        "header $i"
                    },
                    headerContentType = { i, s ->
                        assertEquals(data[i], s)
                        "header"
                    },
                    footerContent = { i, s ->
                        assertEquals(data[i], s)
                        Text("Footer $i")
                    },
                    footerKey = { i, s ->
                        assertEquals(data[i], s)
                        "footer $i"
                    },
                    footerContentType = { i, s ->
                        assertEquals(data[i], s)
                        "footer"
                    },
                    itemContent = { sectionIndex, itemIndex, item ->
                        assertEquals("${data[sectionIndex]} Item $itemIndex", item)
                        Text(item)
                    },
                    itemKey = { sectionIndex, itemIndex, item ->
                        assertEquals("${data[sectionIndex]} Item $itemIndex", item)
                        item
                    },
                    itemContentType = { sectionIndex, itemIndex, item ->
                        assertEquals("${data[sectionIndex]} Item $itemIndex", item)
                        "item"
                    }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(10)

        val semanticsNodes = listNode.onChildren().fetchSemanticsNodes()
        semanticsNodes.filterIndexed { i, _ -> i % 5 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Header $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> (i - 4) % 5 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Footer $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> (i % 5 != 0) && ((i - 4) % 5 != 0) }
            .forEachIndexed { i, node ->
                assertEquals(
                    "Section ${i / 3} Item ${i % 3}",
                    node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
                )
            }
    }

    @Test
    fun `sectionsIndexed using sections list with header`() = runComposeUiTest {
        val data = listOf("Section 0", "Section 1")
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionsIndexed(
                    sections = data,
                    sectionItems = { i, s ->
                        assertEquals(data[i], s)
                        listOf("$s Item 0", "$s Item 1", "$s Item 2")
                    },
                    headerContent = { i, s ->
                        assertEquals(data[i], s)
                        Text("Header $i")
                    },
                    headerKey = { i, s ->
                        assertEquals(data[i], s)
                        "header $i"
                    },
                    headerContentType = { i, s ->
                        assertEquals(data[i], s)
                        "header"
                    },
                    itemContent = { sectionIndex, itemIndex, item ->
                        assertEquals("${data[sectionIndex]} Item $itemIndex", item)
                        Text(item)
                    },
                    itemKey = { sectionIndex, itemIndex, item ->
                        assertEquals("${data[sectionIndex]} Item $itemIndex", item)
                        item
                    },
                    itemContentType = { sectionIndex, itemIndex, item ->
                        assertEquals("${data[sectionIndex]} Item $itemIndex", item)
                        "item"
                    }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(8)

        val semanticsNodes = listNode.onChildren().fetchSemanticsNodes()
        semanticsNodes.filterIndexed { i, _ -> i % 4 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Header $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> i % 4 != 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Section ${i / 3} Item ${i % 3}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `sectionsIndexed using sections list with footer`() = runComposeUiTest {
        val data = listOf("Section 0", "Section 1")
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionsIndexed(
                    sections = data,
                    sectionItems = { i, s ->
                        assertEquals(data[i], s)
                        listOf("$s Item 0", "$s Item 1", "$s Item 2")
                    },
                    footerContent = { i, s ->
                        assertEquals(data[i], s)
                        Text("Footer $i")
                    },
                    footerKey = { i, s ->
                        assertEquals(data[i], s)
                        "footer $i"
                    },
                    footerContentType = { i, s ->
                        assertEquals(data[i], s)
                        "footer"
                    },
                    itemContent = { sectionIndex, itemIndex, item ->
                        assertEquals("${data[sectionIndex]} Item $itemIndex", item)
                        Text(item)
                    },
                    itemKey = { sectionIndex, itemIndex, item ->
                        assertEquals("${data[sectionIndex]} Item $itemIndex", item)
                        item
                    },
                    itemContentType = { sectionIndex, itemIndex, item ->
                        assertEquals("${data[sectionIndex]} Item $itemIndex", item)
                        "item"
                    }
                )
            }
        }

        val listNode = onNodeWithTag("list")
        listNode.onChildren().assertCountEquals(8)

        val semanticsNodes = listNode.onChildren().fetchSemanticsNodes()
        semanticsNodes.filterIndexed { i, _ -> (i - 3) % 4 == 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Footer $i",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }

        semanticsNodes.filterIndexed { i, _ -> (i - 3) % 4 != 0 }.forEachIndexed { i, node ->
            assertEquals(
                "Section ${i / 3} Item ${i % 3}",
                node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
            )
        }
    }

    @Test
    fun `sectionsIndexed using sections with empty data`() = runComposeUiTest {
        setContent {
            LazyColumn(modifier = Modifier.testTag("list")) {
                sectionsIndexed(
                    sections = emptyList<String>(),
                    sectionItems = { _, _ -> emptyList<String>() },
                    itemContent = { _, _, item -> Text(item) }
                )
            }
        }

        onNodeWithTag("list").onChildren().assertCountEquals(0)
    }
}