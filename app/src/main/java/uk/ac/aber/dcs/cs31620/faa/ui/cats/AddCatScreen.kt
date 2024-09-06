package uk.ac.aber.dcs.cs31620.faa.ui.cats

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.datasource.util.LocalDateTimeConverter
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import uk.ac.aber.dcs.cs31620.faa.model.CatsViewModel
import uk.ac.aber.dcs.cs31620.faa.model.Gender
import uk.ac.aber.dcs.cs31620.faa.model.util.ResourceUtil
import uk.ac.aber.dcs.cs31620.faa.ui.components.ButtonSpinner
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AddCatScreenTopLevel(
    navController: NavHostController,
    catsViewModel: CatsViewModel = viewModel()
) {
    AddCatScreen(
        navController = navController,
        insertCat = { newCat ->
            catsViewModel.insertCat(newCat)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCatScreen(
    navController: NavHostController,
    insertCat: (Cat) -> Unit = {}
) {
    // We don't want to show them the default "any" value at the start. That way we don't
    // need to check this when we save. copyOfRange gives us the part we're interested in
    var values = stringArrayResource(R.array.gender_array)
    val genderValues = values.copyOfRange(1, values.size)
    values = stringArrayResource(R.array.breed_array)
    val breedValues = values.copyOfRange(1, values.size)

    var catName by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf(genderValues[0]) }
    var breed by rememberSaveable { mutableStateOf(breedValues[0]) }
    var catDescription by rememberSaveable { mutableStateOf("") }
    val defaultImagePath = stringResource(R.string.default_image_path)
    var imagePath by rememberSaveable { mutableStateOf(defaultImagePath) }
    var dob by remember { mutableStateOf(LocalDate.now()) }

    val formattedDob by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(dob)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    insertCat(
                        name = catName,
                        gender = gender,
                        breed = breed,
                        dob = dob,
                        description = catDescription,
                        imagePath = imagePath,
                        doInsert = { newCat ->
                            insertCat(newCat)
                        }
                    )
                    // We can now go back to caller
                    navController.navigateUp()
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.add_cat)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.addCat))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.goBack)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {


            CatImage(
                imagePath = imagePath,
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp),
                updateImagePath = {
                    imagePath = it
                }
            )

            CatNameInput(
                catName = catName,
                modifier = Modifier
                    .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth(),
                updateName = {
                    catName = it
                }
            )

            GenderInput(
                values = genderValues,
                modifier = Modifier
                    .padding(top = 8.dp),
                updateGender = {
                    gender = it
                }
            )

            BreedInput(
                values = breedValues,
                modifier = Modifier
                    .padding(top = 8.dp),
                updateBreed = {
                    breed = it
                }
            )

            DateOfBirth(
                formattedDob = formattedDob,
                modifier = Modifier
                    .padding(top = 8.dp),
                updateDob = {
                    dob = it
                }
            )

            CatDescriptionInput(
                catDescription = catDescription,
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .weight(1f),
                updateDescription = {
                    catDescription = it
                }
            )
        }
    }
}

private fun insertCat(
    name: String,
    gender: String,
    breed: String,
    dob: LocalDate,
    description: String,
    imagePath: String,
    doInsert: (Cat) -> Unit = {}
) {

    if (name.isNotEmpty() && imagePath.isNotEmpty()) {
        val cat = Cat(
            id = 0,
            name = name,
            gender = Gender.valueOf(gender.uppercase()),
            breed = breed,
            description = description,
            dob = dob.atStartOfDay(), // We don't care about the time
            admissionDate = LocalDateTime.now(),
            imagePath = imagePath
        )
        doInsert(cat)
    }
}

@Composable
fun CatDescriptionInput(
    catDescription: String,
    modifier: Modifier,
    updateDescription: (String) -> Unit
) {
    OutlinedTextField(
        value = catDescription,
        label = {
            Text(text = stringResource(R.string.enterDescription))
        },
        onValueChange = { updateDescription(it) },
        modifier = modifier
    )
}

@Composable
fun DateOfBirth(
    formattedDob: String,
    modifier: Modifier,
    updateDob: (LocalDate) -> Unit
) {
    // State used to show the dialog
    var openDialog by rememberSaveable { mutableStateOf(false) }

    OutlinedButton(
        onClick = {
            openDialog = true
        },
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.dob, formattedDob),
            fontSize = 16.sp
        )
    }

    // Define the date picker dialog. Will be recomposed if openDialog changes state
    if (openDialog) {
        // Keeps track of the current DatePicker state
        // It is possible to configure this, e.g. so that dates after today are not
        // selectable
        val datePickerState = rememberDatePickerState()
        // We don't want the Confirm button to be enabled until
        // user selects a date
        val confirmEnabled by remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = {
                // Leave empty so that user has to click a button to leave
                // rather than just outside the dialog or on the back button
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Close the dialog
                        openDialog = false
                        // We convert from milliseconds to LocalDate and call updateDob to hoist
                        // the state
                        val selectedDate =
                            LocalDateTimeConverter.toLocalDate(datePickerState.selectedDateMillis!!)
                        updateDob(selectedDate.toLocalDate())
                    },
                    enabled = confirmEnabled
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDialog = false
                }) { Text(stringResource(R.string.cancel)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun BreedInput(
    values: Array<String>,
    modifier: Modifier,
    updateBreed: (String) -> Unit
) {
    ButtonSpinner(
        items = values.asList(),
        modifier = modifier,
        itemClick = {
            updateBreed(it)
        }
    )
}

@Composable
fun GenderInput(
    values: Array<String>,
    modifier: Modifier,
    updateGender: (String) -> Unit
) {
    ButtonSpinner(
        items = values.asList(),
        modifier = modifier,
        itemClick = {
            updateGender(it)
        }
    )
}

@Composable
fun CatNameInput(
    catName: String,
    modifier: Modifier,
    updateName: (String) -> Unit
) {
    OutlinedTextField(
        value = catName,
        label = {
            Text(text = stringResource(id = R.string.cat_name))
        },
        onValueChange = { updateName(it) },
        modifier = modifier
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CatImage(
    imagePath: String,
    modifier: Modifier,
    updateImagePath: (String) -> Unit = {}
) {
    var photoFile: File? = remember { null }
    val ctx = LocalContext.current

    val resultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                updateImagePath(
                    "file://${photoFile!!.absolutePath}"
                )
            }
        }

    // Should recompose if imagePath changes as a result of taking the picture
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        GlideImage(
            model = Uri.parse(imagePath),
            contentDescription = stringResource(R.string.cat_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .clickable {
                    takePicture(
                        ctx = ctx,
                        resultLauncher = resultLauncher
                    ) {
                        photoFile = it
                    }
                }
        )
        Text(text = stringResource(id = R.string.enterImageMsg))
    }
}

private fun takePicture(
    ctx: Context,
    resultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    updateFile: (File) -> Unit
) {
    // See configuration instructions added to AndroidManifest.xml
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    var photoFile: File? = null

    // Create the File where the photo should go
    try {
        photoFile = ResourceUtil.createImageFile(ctx)
    } catch (ex: IOException) {
        // Error occurred while creating the File
        Toast.makeText(
            ctx,
            ctx.getString(R.string.cannot_create_image_file),
            Toast.LENGTH_SHORT
        ).show()
    }

    // Continue only if the File was successfully created
    photoFile?.let {
        val photoUri = FileProvider.getUriForFile(
            ctx,
            ctx.packageName,
            it
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        // Request will fail if a camera app not available.
        try {
            resultLauncher.launch(takePictureIntent)
            updateFile(photoFile)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(ctx, R.string.cannotTakePicture, Toast.LENGTH_LONG)
                .show()
        }
    }
}

// Not required or used. But here for your reference
@Composable
private fun HandleBackButton(navController: NavHostController) {
    // When back button is pressed we will navigate up the Compose
    // hierarchy. navigateUp will pop the Compose navigation back stack automatically.
    val backCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
            }
        }

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}
