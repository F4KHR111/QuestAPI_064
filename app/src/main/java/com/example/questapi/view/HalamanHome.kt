package com.example.questapi.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    //edit 1.1 : tambahkan parameter navigateToItemEntry
    navigateToItemEntry: () -> Unit,
    //edit 2.4 : tambahkan parameter navigateToItemUpdate
    navigateToItemUpdate:(Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiHome.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                //edit 1.2 : event onClick
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.entry_siswa)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            statusUiSiswa = viewModel.listSiswa,
            onSiswaClick = navigateToItemUpdate,
            retryAction = viewModel::loadSiswa,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}
@Composable
fun HomeBody(
    statusUiSiswa: StatusUiSiswa,
    //edit 2.3 tambahkan parameter onSiswaClick
    onSiswaClick: (Int) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        when(statusUiSiswa){
            is StatusUiSiswa.Loading -> LoadingScreen()
            //edit 2.5 : tambahkan event onSiswaClick
            is StatusUiSiswa.Success -> DaftarSiswa(itemSiswa = statusUiSiswa.Siswa,
                onSiswaClick = {onSiswaClick(it.id)} )
            is StatusUiSiswa.Error -> ErrorScreen(
                retryAction,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}