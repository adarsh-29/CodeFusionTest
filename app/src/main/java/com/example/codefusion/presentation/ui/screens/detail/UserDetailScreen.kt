package com.example.codefusion.presentation.ui.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.codefusion.presentation.ui.screens.common.SimpleTopBar
import com.example.codefusion.presentation.ui.viewmodel.UserViewModel

@Composable
fun UserDetailScreen(
    userId: Int,
    onBackClick: () -> Unit,
    viewModel: UserViewModel = hiltViewModel()
) {

    val user = viewModel.getUserById(userId)

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(onBackClick = onBackClick) }
    )
    { innerPadding ->
        if (user == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("User not found")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            AsyncImage(
                model = user.image,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(16.dp))

            Text(text = user.fullName, modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,)
            Text(user.email, fontWeight = FontWeight.Normal,textAlign = TextAlign.Center,)
            Text(user.phone, fontWeight = FontWeight.Normal,textAlign = TextAlign.Center,)

            Spacer(Modifier.height(8.dp))

            Text("${user.city}, ${user.state}, ${user.country}",
                fontWeight = FontWeight.Normal,textAlign = TextAlign.Center,)
            Text("DOB: ${user.birthDate} | Age: ${user.age}",
                fontWeight = FontWeight.Normal,textAlign = TextAlign.Center,)
        }
    }
}

@Composable
fun TopBar(onBackClick: () -> Unit) {
    SimpleTopBar(true,"Detail Screen", onBackClick)
}