package cat.jorcollmar.data.model.dto

data class GoogleApiResultListDto(
    val status: String?,
    val error_message: String?,
    val next_page_token: String?,
    val results: List<PlaceDto>?
)