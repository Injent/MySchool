package me.injent.myschool.feature.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.injent.myschool.core.designsystem.component.DynamicAsyncImage
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.feature.accounts.model.ExpandedAccount

@Composable
fun AccountCards(
    accountsUiState: AccountsUiState,
    onSelectAccount: (ExpandedAccount) -> Unit,
    onAddAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.select_account)
        )
        Spacer(Modifier.height(48.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) {
            items(accountsUiState.accounts) { account ->
                AccountCard(
                    account = account,
                    onClick = { onSelectAccount(account) }
                )
            }
            item {
                AddAccount(onClick = onAddAccount)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountCard(account: ExpandedAccount, onClick: () -> Unit) {
    Surface(shape = MaterialTheme.shapes.medium, onClick = onClick) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .size(128.dp)
                .padding(16.dp),
        ) {
            if (account.avatarUrl != null) {
                DynamicAsyncImage(
                    imageUrl = account.avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            } else {
                Spacer(
                    Modifier
                        .size(64.dp)
                        .background(
                            color = MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                )
            }
            Text(
                text = account.name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddAccount(
    onClick: () -> Unit
) {
    Surface(shape = MaterialTheme.shapes.medium, onClick = onClick) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .size(128.dp)
                .padding(16.dp),
        ) {
            Icon(
                imageVector = MsIcons.Add,
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = null,
                modifier = Modifier.aspectRatio(.5f)
            )
        }
    }
}