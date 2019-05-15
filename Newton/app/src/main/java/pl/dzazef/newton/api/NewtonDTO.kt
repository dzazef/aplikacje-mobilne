package pl.dzazef.newton.api

data class NewtonDTO(var operation : String, var expression : String, var result : String) {
    override fun toString(): String {
        return "op: $operation expr: $expression res: $result"
    }
}