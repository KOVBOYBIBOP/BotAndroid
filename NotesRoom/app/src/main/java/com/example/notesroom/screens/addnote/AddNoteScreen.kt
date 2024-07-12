import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notesroom.screens.addnote.AddNoteViewModel

@Composable
fun AddTaskScreen(navigateToStartScreen: () -> Unit) {
    val viewModel: AddNoteViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Text field for Title
        OutlinedTextField(
            value = viewModel.taskTitle,
            onValueChange = {
                if (it.length <= 10) { // Limit to 10 characters
                    viewModel.taskTitle = it
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Title") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            maxLines = 1 // Allow only 1 line of text
        )
        OutlinedTextField(
            value = viewModel.taskDescription,
            onValueChange = { viewModel.taskDescription = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .fillMaxHeight(0.5f),
            label = { Text("Description") },
            maxLines = 10
        )

        // Button to add task
        Button(
            onClick = { viewModel.saveTask() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Add")
        }

        // Button to cancel and navigate back
        Button(
            onClick = { navigateToStartScreen() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Cancel")
        }
    }
}
