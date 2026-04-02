package com.example.codefusion.presentation.ui.screens.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.codefusion.domain.model.User
import com.example.codefusion.presentation.ui.screens.common.SimpleTopBar
import com.example.codefusion.presentation.ui.viewmodel.UserViewModel
import com.example.codefusion.presentation.ui.viewmodel.listing_states.SortOrder
import com.example.codefusion.presentation.ui.viewmodel.listing_states.UserState


@Composable
fun UserListScreen(
    viewModel: UserViewModel = hiltViewModel(),
    onUserClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    val state = viewModel.state
    val users = viewModel.filteredUsers


    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(onBackClick = onBackClick) }
    )
    { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            when {
                state.isLoading && users.isEmpty() -> {
                    FullScreenLoader()
                }

                state.error != null && users.isEmpty() -> {
                    ErrorView(
                        message = state.error ?: "",
                        onRetry = { viewModel.loadUsers() }
                    )
                }

                else -> {
                    Column {
                        SearchBar(viewModel)
                        FilterRow(viewModel)

                        UserList(
                            viewModel,
                            users = users,
                            state = state,
                            onUserClick = onUserClick,
                            onRefresh = { viewModel.refresh() }
                        )
                    }

                    if (users.isEmpty()) {
                        EmptyView()
                    }
                }
            }
        }
    }
}

@Composable
fun FullScreenLoader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // shimmer effect
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(12) { ShimmerItem() }
        }
        // with circular indicator
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Something went wrong", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Text(message,textAlign = TextAlign.Center, fontWeight = FontWeight.Normal)
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun EmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No users found")
    }
}

@Composable
fun TopBar(onBackClick: () -> Unit) {
    SimpleTopBar(false,"Listing Screen", onBackClick)
}

@Composable
fun SearchBar(viewModel: UserViewModel) {
    TextField(
        value = viewModel.filterState.searchQuery,
        onValueChange = { viewModel.updateSearch(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text("Search users by name...") },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )

    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterRow(viewModel: UserViewModel) {

    val state = viewModel.filterState

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row {
            Text("Filter: ", Modifier.padding(start = 8.dp, top = 12.dp))

            FilterChip(
                selected = state.gender == null,
                onClick = { viewModel.updateGender(null) },
                label = { Text("All") }
            )
            Spacer(modifier = Modifier.width(4.dp))
            FilterChip(
                selected = state.gender == "male",
                onClick = { viewModel.updateGender("male") },
                label = { Text("Male") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = state.gender == "female",
                onClick = { viewModel.updateGender("female") },
                label = { Text("Female") }
            )
            Spacer(modifier = Modifier.width(16.dp))
        }


        Row {
            Text("Sort:", Modifier.padding(top = 12.dp))
            IconButton(onClick = { viewModel.updateSort(SortOrder.AZ) }) {
                Icon(Icons.Default.KeyboardArrowUp, null)
            }
            IconButton(onClick = { viewModel.updateSort(SortOrder.ZA) }) {
                Icon(Icons.Default.KeyboardArrowDown, null)
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserList(
    viewModel: UserViewModel,
    users: List<User>,
    state: UserState,
    onUserClick: (User) -> Unit,
    onRefresh: () -> Unit
) {
    val listState = rememberLazyListState()

             // Pagination
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->

                val lastVisibleIndex = visibleItems.lastOrNull()?.index ?: return@collect

                if (viewModel.shouldLoadMore(lastVisibleIndex)) {
                    viewModel.loadUsers()
                }
            }
    }


    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(users, key = { it.id }) { user ->
                UserCard(user, onUserClick)
            }

            if (state.isLoading) {
                items(6) { ShimmerItem() }
            }
        }
    }
}

@Composable
fun ShimmerItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp)
            .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
    )
}

@Composable
fun UserCard(user: User, onClick: (User) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(user) },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {

            AsyncImage(
                model = user.image,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(user.fullName, style = MaterialTheme.typography.titleMedium)
                Text(user.email, style = MaterialTheme.typography.bodySmall)
                Text(user.city, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}