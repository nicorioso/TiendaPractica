package com.sena.vistastienda.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sena.vistastienda.Catalogo
import com.sena.vistastienda.R
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable(
            route = "detail/{imageId}",
            arguments = listOf(navArgument("imageId") { type = NavType.IntType })
        ) { backStackEntry ->
            val imageId = backStackEntry.arguments?.getInt("imageId")
            if (imageId != null) {
                DetailScreen(imageId)
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Catalogo", modifier = Modifier.padding(16.dp))
        Catalogo { imageId ->
            navController.navigate("detail/$imageId")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailScreen(imageId: Int) {
    val productImages = listOf(
        imageId,
        R.drawable.img2,
        R.drawable.img3
    )

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { productImages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Carrusel
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) { page ->
            Image(
                painter = painterResource(id = productImages[page]),
                contentDescription = "Imagen del producto",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        //Indicadores
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(productImages.size) { index ->
                val color = if (pagerState.currentPage == index) Color.Black else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Camisa Clásica", color = Color.Black)
        Text(text = "Precio: $24.00", color = Color.Gray)
        Text(text = "Tallas: S, M, L, XL", color = Color.Gray)
    }
}