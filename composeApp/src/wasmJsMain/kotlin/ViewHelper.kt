import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import danigutiadanportfolio.composeapp.generated.resources.Res
import danigutiadanportfolio.composeapp.generated.resources.ic_github
import danigutiadanportfolio.composeapp.generated.resources.ic_google_play
import danigutiadanportfolio.composeapp.generated.resources.ic_linkedin
import org.jetbrains.compose.resources.DrawableResource

object ViewHelper {
    val socialDataList = listOf(
        SocialData(icon = Res.drawable.ic_linkedin, url = "https://www.linkedin.com/in/daniel-gutierrez-adan/", name = "linkedIn"),
        SocialData(icon = Res.drawable.ic_github, url = "https://github.com/danigutiadan", name = "Github"),
        SocialData(
            icon = Res.drawable.ic_google_play,
            url = "https://play.google.com/store/apps/developer?id=Danigutiadan", name = "Play Store",
            modifier = Modifier.size(38.dp)
        )

    )
}

data class SocialData(
    val icon: DrawableResource,
    val url: String,
    val modifier: Modifier = Modifier,
    val name: String
)