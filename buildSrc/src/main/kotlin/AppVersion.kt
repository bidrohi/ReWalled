import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object AppVersion {
    private val now by lazy {
        OffsetDateTime.now(ZoneOffset.UTC)
    }

    val code by lazy {
        DateTimeFormatter.ofPattern("yyMMddHH")
            .format(now)
            .toInt()
    }

    val name by lazy {
        DateTimeFormatter.ofPattern("yyyy.MM.dd")
            .format(now)
    }
}
