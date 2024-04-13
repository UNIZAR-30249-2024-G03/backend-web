package unizar.labis.g03.backendweb.models

import java.util.Date

class Reserva(
    val espacios : List<Espacio>, val tipoDeUso : TipoDeUsoReserva, val numMaxOcupantes : Int,
    val fechaInicio : Date, val fechaFinal : Date, val descripcion : String
) {

}