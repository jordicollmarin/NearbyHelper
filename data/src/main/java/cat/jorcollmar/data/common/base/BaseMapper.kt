package cat.jorcollmar.data.common.base

interface BaseMapper<U, M> {
    fun map(unmappedList: List<U>): List<M>
    fun map(unmapped: U): M
}