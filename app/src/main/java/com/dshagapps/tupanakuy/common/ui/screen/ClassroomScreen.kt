package com.dshagapps.tupanakuy.common.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.dshagapps.tupanakuy.common.ui.component.loader.Loader
import com.dshagapps.tupanakuy.common.ui.util.OnLifecycleEvent
import com.dshagapps.tupanakuy.common.ui.viewmodel.ClassroomScreenViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ClassroomScreen(
    state: StateFlow<ClassroomScreenViewModel.State>,
    updateState: (ClassroomScreenViewModel.State) -> Unit = {},
    onInitScreen: () -> Unit = {},
    goToPreviousScreen: () -> Unit = {}
) {
    val context: Context = LocalContext.current

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> onInitScreen()
            else -> Unit
        }
    }
    
    when (val s = state.collectAsState().value) {
        is ClassroomScreenViewModel.State.Idle -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(listOf(s.teacher)) { teacher ->
                    Text(text = "TeacherUID: ${teacher.uid}")
                    Text(text = "Name: ${teacher.displayName}")
                    Spacer(modifier = Modifier.padding(24.dp))
                }
                items(s.students) {student ->
                    Text(text = "StudentUID: ${student.uid}")
                    Text(text = "Name: ${student.displayName}")
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
        ClassroomScreenViewModel.State.Loading -> Loader()
        is ClassroomScreenViewModel.State.OnError -> {
            Toast.makeText(context, s.exception.message, Toast.LENGTH_SHORT).show()
            goToPreviousScreen()
        }
    }
}