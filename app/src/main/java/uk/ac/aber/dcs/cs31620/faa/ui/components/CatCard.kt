package uk.ac.aber.dcs.cs31620.faa.ui.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import uk.ac.aber.dcs.cs31620.faa.model.Gender
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme

/**
 * Represents a single cat as a Card with image, name and
 * delete icon button.
 * @param modifier Standard modifier to help format the card
 * @param cat The cat data to display
 * @param selectAction The operation to perform when the cat image is tapped
 * @param deleteAction The operation to perform when the delete icon is tapped
 * @author Chris Loftus
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CatCard(
    modifier: Modifier = Modifier,
    cat: Cat,
    selectAction: (Cat) -> Unit = {},
    deleteAction: (Cat) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxSize()
    ) {
        ConstraintLayout {
            val (imageRef, nameRef, deleteRef) = createRefs()

            // There is a more efficient way to use Glide in LazyLists
            // The problem is that we are using a LazyVerticalGrid in the
            // caller which is incompatible with the more efficient version.
            // See https://bumptech.github.io/glide/int/compose.html
            GlideImage(
                //model = Uri.parse("file:///android_asset/images/${cat.imagePath}"),
                model = Uri.parse(cat.imagePath),
                contentDescription = stringResource(R.string.cat_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(190.dp)
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .clickable { selectAction(cat) }
            )

            Text(
                text = cat.name,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .constrainAs(nameRef) {
                        start.linkTo(parent.start)
                        top.linkTo(imageRef.bottom)
                        bottom.linkTo(parent.bottom)
                    }
            )

            IconButton(
                onClick = { deleteAction(cat) },
                modifier = Modifier.constrainAs(deleteRef) {
                    end.linkTo(parent.end)
                    top.linkTo(imageRef.bottom)
                    bottom.linkTo(parent.bottom)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = stringResource(R.string.remove_cat)
                )
            }
        }
    }
}

// Does not work well with GlideImage, i.e. it wont try and obtain the image
@Preview
@Composable
private fun CatCardPreview(){
    FAATheme(darkTheme = true,
        dynamicColor = false) {
        CatCard(cat = Cat(
            name = "Test Cat",
            breed = "Moggy",
            gender = Gender.FEMALE,
            description = "A test cat",
            imagePath = "cat1.png"
        ))
    }
}