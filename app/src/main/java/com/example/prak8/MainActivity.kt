import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.prak8.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}
private fun downloadImage(url: String): Bitmap {
    val inputStream = URL(url).openStream()
    return BitmapFactory.decodeStream(inputStream)
}
//private fun saveImage(bitmap: Bitmap, context: Context) {
//    val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//    val file = File(directory, "image.jpg")
//    val outputStream = FileOutputStream(file)
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//    outputStream.flush()
//    outputStream.close()
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            var url by remember { mutableStateOf("") }
            var bitmap by remember { mutableStateOf<Bitmap?>(null) }

            TextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Enter image URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(

                onClick = {

                    GlobalScope.launch(Dispatchers.IO) {
                        val downloadedBitmap = downloadImage(url)
                        launch(Dispatchers.Main) {
                            bitmap = downloadedBitmap
                        }

                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Download")
            }

            Spacer(modifier = Modifier.height(16.dp))

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Downloaded image",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.ic_menu_report_image),
                contentDescription = "Placeholder image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

