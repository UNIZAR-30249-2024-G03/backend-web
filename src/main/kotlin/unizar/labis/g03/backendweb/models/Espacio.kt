package unizar.labis.g03.backendweb.models

class Espacio (
    val tamano : Double, val id : Int, val tipo : TipoEspacio,
    val numMaxOcupantes : Int, val planta : Int, val reservable : Boolean,
    val categoriaReservaEspacio : TipoEspacio, val horarioReservaDisponible : String,
    val porcentajeUsoMaximo : Double
)
{

}