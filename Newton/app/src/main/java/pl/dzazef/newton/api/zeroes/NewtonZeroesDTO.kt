package pl.dzazef.newton.api.zeroes

data class NewtonZeroesDTO(var operation : String, var expression : String, var result : List<String>){
    override fun toString(): String {
        return "op: $operation expr: $expression res: $result"
    }
}