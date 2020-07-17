package cat.jorcollmar.data.model.dto

data class GoogleApiResultDetailDto(
    val status: String?,
    val error_message: String?,
    val result: PlaceDto?
)