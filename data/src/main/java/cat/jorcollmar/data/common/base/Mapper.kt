package cat.jorcollmar.data.common.base

abstract class Mapper<U, M> : BaseMapper<U, M> {
    override fun map(unmappedList: List<U>): List<M> = unmappedList.map { map(it) }
}