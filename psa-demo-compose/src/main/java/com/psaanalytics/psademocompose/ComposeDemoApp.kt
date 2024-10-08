package com.psaanalytics.psademocompose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.psaanalytics.psademocompose.data.Tracking
import com.psaanalytics.psademocompose.ui.SchemaDetailScreen
import com.psaanalytics.psademocompose.ui.SchemaDetailViewModel
import com.psaanalytics.psademocompose.ui.SchemaListScreen
import com.psaanalytics.psademocompose.ui.SchemaListViewModel
import java.util.*

object Destinations {
    const val SCHEMA_LIST_ROUTE = "list"
    const val SCHEMA_DETAIL_ROUTE = "detail/{schema}"
}

// This demo app displays the publicly available schemas from Iglu Central
// http://iglucentral.com/
@Composable
fun ComposeDemoApp(
    listViewModel: SchemaListViewModel,
    detailViewModel: SchemaDetailViewModel
) {
    val navController = rememberNavController()
    
    // Initialises the Psa tracker and sets up screen view autotracking
    // A ScreenView event will be generated every time the navigation destination changes
    Tracking.setup("compose_demo")
    Tracking.AutoTrackScreenView(navController = navController)
    
    NavHost(
        navController = navController, 
        startDestination = Destinations.SCHEMA_LIST_ROUTE
    ) {
        composable(Destinations.SCHEMA_LIST_ROUTE) {
            SchemaListScreen(
                vm = listViewModel, 
                onSchemaClicked = {
                    val encoded = Base64.getEncoder().encodeToString(it.toByteArray())
                    navController.navigate("detail/$encoded")
                }
            )
        }

        composable(Destinations.SCHEMA_DETAIL_ROUTE) { 
            val schemaUrl = it.arguments?.getString("schema")
            SchemaDetailScreen(
                vm = detailViewModel,
                schemaUrl = String(Base64.getDecoder().decode(schemaUrl)).drop(5),
                onBackButtonClicked = { navController.navigate(Destinations.SCHEMA_LIST_ROUTE) }
            )
        }
    }
}
