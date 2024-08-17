package com.psaanalytics.psademocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.psaanalytics.psademocompose.ui.SchemaDetailViewModel
import com.psaanalytics.psademocompose.ui.SchemaListViewModel
import com.psaanalytics.psademocompose.ui.theme.ComposeDemoTheme

class MainActivity : ComponentActivity() {
    private val listVm = SchemaListViewModel()
    private val detailVm = SchemaDetailViewModel()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                ComposeDemoApp(
                    listViewModel = listVm, 
                    detailViewModel = detailVm
                )
            }
        }
    }
}
