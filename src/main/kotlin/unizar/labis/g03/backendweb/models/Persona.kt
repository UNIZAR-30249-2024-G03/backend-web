package unizar.labis.g03.backendweb.models

class Persona (
    val nombre : String, val apellido : String,  val email : String, val rol : List<Rol>, val departamento : Departamento
){
}