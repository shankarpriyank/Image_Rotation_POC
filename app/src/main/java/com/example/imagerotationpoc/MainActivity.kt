package com.example.imagerotationpoc

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.imagerotationpoc.ui.theme.ImageRotationPOCTheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageRotationPOCTheme {

                var selectedImageUri by remember {
                    mutableStateOf<Uri?>(null)
                }
                var selectedImageUris by remember {
                    mutableStateOf<List<Uri>>(emptyList())
                }
                val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia(),
                    onResult = { uri -> selectedImageUri = uri }
                )
                val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickMultipleVisualMedia(),
                    onResult = { uris -> selectedImageUris = uris }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(onClick = {
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }) {
                                Text(text = "Pick one photo")
                            }

                        }
                        Button(onClick = {ct(selectedImageUri!!) }) {
                            Text(text = "Crop")

                        }
                        Button(onClick = {rt(selectedImageUri!!) }) {
                            Text(text = "Rotate")

                        }
//
//

                    }

                    item {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { rt(selectedImageUri!!) },
                            contentScale = ContentScale.Crop
                        )
                    }

                    items(selectedImageUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }


            }


    }
    }


    fun rt(f: Uri) {

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(f, projection, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        val file: File = File(s)

        Rotation().rotateImage(file)
    }
    fun ct(f: Uri) {

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(f, projection, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        val file: File = File(s)

        Rotation().cropImage(file)
    }
}