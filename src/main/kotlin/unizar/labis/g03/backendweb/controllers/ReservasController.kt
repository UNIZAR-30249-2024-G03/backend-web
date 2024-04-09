package unizar.labis.g03.backendweb.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable

@Tag(name = "Reservas AdaByron", description = "API para la reserva de espacios en el edificio Ada Byron")
@RestController
class ReservasController {
    @Operation(
        summary = "Obtiene las reservas de un usuario",
        description = "Obtiene una lista de Reservas pertenecientes al usuario con identificador 'id'. Una reserva consta de blabla")
    @GetMapping("/reservas/{idUsuario}")
    fun obtenerReservasUsuario(@PathVariable idUsuario: String): String {
        return "reservas"
    }

    @PostMapping("/reservas")
    fun addReserva(@RequestBody reserva: String) {
        print("preuba")
    }

    @Operation(
        summary = "Obtiene todas las reservas del sistema",
        description = "Obtiene una lista cont todas las Reservas del sistema. Una reserva consta de blabla")
    @GetMapping("/reservas")
    fun obtenerReservas(@RequestBody @Parameter(name = "idUsuario", description = "Identificador del usuario que desea obtener la lista", example = "paco") idUsuario: String): String {
        return "reservas"
    }
}