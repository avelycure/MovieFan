package com.avelycure.person.presentation

import BaseScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.avelycure.domain.models.Person
import com.avelycure.person.constants.PersonConstants.BUFFER_SIZE
import infinite_list.OnBottomReached

@Composable
fun PersonScreen(
    state: PersonState,
    fetchPopularPerson: (Int) -> Unit,
    showMoreInfo: () -> Unit
) {
    BaseScreen(
        queue = state.errorQueue,
        progressBarState = state.progressBarState,
        onRemoveHeadFromQueue = {}
    ) {

        PersonsList(state.persons, fetchPopularPerson, showMoreInfo)

    }

}

@Composable
fun PersonCard(person: Person, modifier: Modifier) {
    Text(text = person.name, modifier = modifier)
}

@Composable
fun PersonsList(
    persons: List<Person>,
    fetchPopularPerson: (Int) -> Unit,
    showMoreInfo: () -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(persons) { person ->
            PersonCard(
                person = person,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(251.dp)
                    .background(Color.Blue)
                    .padding(vertical = 4.dp)
                    .clickable {
                        showMoreInfo()
                    }
            )
        }
    }
    listState.OnBottomReached(
        buffer = BUFFER_SIZE
    ) {
        fetchPopularPerson(5)
    }
}