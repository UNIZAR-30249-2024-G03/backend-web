package unizar.labis.g03.backendweb.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import unizar.labis.g03.backendweb.models.*
import java.sql.Date
import java.time.LocalDate

@Tag(name = "Reservas AdaByron", description = "API para la reserva de espacios en el edificio Ada Byron")
@RestController
class ReservasController {

    // El mapa interactivo de los espacios del Ada Byron permitirá mostrar sus distintas plantas
    @Operation(
        summary = "Obtiene todos los espacios existentes de una planta",
        description = "Obtiene una lista con todos los espacios que existen en la planta pasada como parámetro.")
    @GetMapping("/espacios/planta")
    fun getEspaciosPlanta(@RequestBody @Parameter(name = "idPlanta", description = "Identificador de la planta de la que desea obtener los espacios", example = "2") idPlanta : Int): List<Espacio> {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
        conjuntoEspacios.add(espacio);

        return conjuntoEspacios;
    }

    // ----------------- PERSONA ---------------------
    @Operation(
        summary = "Permite a un usuario con rol de gerente obtener todas los usuarios registrados en el sistema",
        description = "Obtiene una lista con todos los usuarios registrados en el sistema si el usuario con identificador 'id' tiene rol de gerente.")
    @GetMapping("/personas")
    fun getPersonas(@RequestBody @Parameter(name = "idUsuario", description = "Identificador del usuario que desea obtener los usuarios registradas en el sistema", example = "795593") idUsuario : Int): List<Persona> {
        val rol : Rol = Rol.Estudiante;
        val conjuntoRoles : MutableList<Rol> = ArrayList<Rol>();
        conjuntoRoles.add(rol);
        val persona = Persona("Adrian", "Arribas", "795593@gmail.com", conjuntoRoles, Departamento.InformaticaIngenieriaSistemas);
        val conjuntoPersonas : MutableList<Persona> = ArrayList<Persona>();
        conjuntoPersonas.add(persona);

        return conjuntoPersonas;
    }

    @Operation(
        summary = "Obtiene la información de un usuario en concreto",
        description = "Obtiene la información del usuario con identificador igual al pasado como parámetro.")
    @GetMapping("/personas/{id}")
    fun getPersonasUsuario(@PathVariable id : Int): Persona {
        val rol : Rol = Rol.Estudiante;
        val conjuntoRoles : MutableList<Rol> = ArrayList<Rol>();
        conjuntoRoles.add(rol);
        val persona = Persona("Adrian", "Arribas", "795593@gmail.com", conjuntoRoles, Departamento.InformaticaIngenieriaSistemas);

        return persona;
    }

    @Operation(
        summary = "Permite añadir la información de un nuevo usuario al sistema",
        description = "Permite añadir la información de un nuevo usuario al sistema.")
    @PostMapping("/personas")
    fun addPersona(@RequestParam @Parameter(name = "nombre", description = "Nombre de la persona que se desea añadir al sistema", example = "Adrian") nombre : String,
                   @RequestParam @Parameter(name = "apellido", description = "Apellido de la persona que se desea añadir al sistema", example = "Arribas") apellido : String,
                   @RequestParam @Parameter(name = "email", description = "Correo electrónico de la persona que se desea añadir al sistema", example = "795593@unizar.es") email : String,
                   @RequestParam @Parameter(name = "rol", description = "Rol o roles de la persona que se desea añadir al sistema", example = "Estudiante") rol : List<Rol>,
                   @RequestParam @Parameter(name = "departamento", description = "Departamento al que pertenece la persona que se desea añadir al sistema", example = "InformaticaIngenieriaSistemas") departamento: Departamento): Persona {
        val rol : Rol = Rol.Estudiante;
        val conjuntoRoles : MutableList<Rol> = ArrayList<Rol>();
        conjuntoRoles.add(rol);
        val persona = Persona(nombre, apellido, "795593@gmail.com", conjuntoRoles, Departamento.InformaticaIngenieriaSistemas);

        return persona;
    }

    @Operation(
        summary = "Permite cambiar el rol o roles o el departamento a un usuario",
        description = "Permite cambiar el rol o roles o el departamento del usuario con identificador 'id'.")
    @PutMapping("/personas/{id}")
    fun updatePersona(@PathVariable id : Int ,
                      @Parameter(name = "rol", description = "Nuevo rol o roles de la persona que se desea actualizar", example = "Estudiante") @RequestParam(required = false) rol : List<Rol>,
                      @Parameter(name = "departamento", description = "Nuevo departamento de la persona que se desea actualizar", example = "InformaticaIngenieriaSistemas") @RequestParam(required = false) departamento : Departamento) : Persona {
        val rol : Rol = Rol.DocenteInvestigador;
        val conjuntoRoles : MutableList<Rol> = ArrayList<Rol>();
        conjuntoRoles.add(rol);
        val persona = Persona("Adrian", "Arribas", "795593@gmail.com", conjuntoRoles, Departamento.InformaticaIngenieriaSistemas);

        return persona;
    }

    // ---------------- ESPACIO ---------------------
    @Operation(
        summary = "Obtiene todos los espacios del sistema",
        description = "Obtiene una lista con todos los espacios del sistema.")
    @GetMapping("/espacios")
    fun getEspacios(): List<Espacio> {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
        conjuntoEspacios.add(espacio);

        return conjuntoEspacios;
    }

    @Operation(
        summary = "Permite añadir la información de un nuevo espacio al sistema",
        description = "Permite añadir la información de un nuevo espacio al sistema")
    @PostMapping("/espacios")
    fun addEspacio(@Parameter(name = "tamaño", description = "Tamaño que tiene el espacio que se desea añadir al sistema", example = "120.35") @RequestParam(required = true) tamano : Double,
                   @Parameter(name = "id", description = "Identificador del espacio que se desea añadir al sistema", example = "72") @RequestParam(required = true) id : Int,
                   @Parameter(name = "tipo de espacio", description = "El tipo de espacio que es el espacio que se desea añadir al sistema", example = "Aula") @RequestParam(required = true) tipoEspacio : TipoEspacio,
                   @Parameter(name = "número máximo de ocupantes", description = "Número de ocupantes máximos que tiene el espacio que se desea añadir al sistema", example = "150") @RequestParam(required = true) numMaxOcupantes : Int,
                   @Parameter(name = "planta", description = "Planta en la que se encuentra el espacio que se desea añadir al sistema", example = "2") @RequestParam(required = true) planta: Int,
                   @Parameter(name = "reservable", description = "Indica si el espacio que se desea añadir al sistema se puede reservar o no", example = "True") @RequestParam(required = true) reservable: Boolean,
                   @Parameter(name = "categoria de reserva de espacio", description = "Indica la categoría de reserva del espacio que se desea añadir al sistema en caso de que sea reservable", example = "Aula") @RequestParam(required = false) categoriaReserva: TipoEspacio,
                   @Parameter(name = "horario de reserva disponible", description = "Indica el horario disponible de reserva que tiene el espacio que se desea añadir al sistema en caso de que sea reservable", example = "09:00-14:00") @RequestParam(required = false) horarioReservaDisponible: String,
                   @Parameter(name = "porcentaje de uso maximo", description = "Indica el porcentaje máximo permitido del espacio que se desea añadir al sistema", example = "50.0") @RequestParam(required = false) porcentajeUsoMaximo: Double): Espacio {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);

        return espacio;
    }

    @Operation(
        summary = "Permite a un usuario con rol de gerente modificar los siguientes atributos de un espacio: reservable, categoría de reserva de espacio" +
                "horario de reserva disponible, porcentaje de uso máximo permitido.",
        description = "Permite modificar los atributos de un espacio si el usuario con identificador {idUsuario} tiene rol de gerente")
    @PutMapping("/espacios/{id}")
    fun updateEspacio(@PathVariable id : Int,
                   @Parameter(name = "idUsuario", description = "Identificador del usuario que desea modificar los datos del espacio", example = "795593") @RequestParam(required = true) idUsuario : Int,
                   @Parameter(name = "reservable", description = "Indica si el espacio que se desea añadir al sistema se puede reservar o no", example = "True") @RequestParam(required = true) reservable: Boolean,
                   @Parameter(name = "categoria de reserva de espacio", description = "Indica la categoría de reserva del espacio que se desea añadir al sistema en caso de que sea reservable", example = "Aula") @RequestParam(required = false) categoriaReserva: TipoEspacio,
                   @Parameter(name = "horario de reserva disponible", description = "Indica el horario disponible de reserva que tiene el espacio que se desea añadir al sistema en caso de que sea reservable", example = "09:00-14:00") @RequestParam(required = false) horarioReservaDisponible: String,
                   @Parameter(name = "porcentaje de uso maximo", description = "Indica el porcentaje máximo permitido del espacio que se desea añadir al sistema", example = "50.0") @RequestParam(required = false) porcentajeUsoMaximo: Double): Espacio {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);

        return espacio;
    }

    @Operation(
        summary = "Obtiene todos los espacios del sistema que coinciden con el identificador pasado como parámetro",
        description = "Obtiene una lista con todos los espacios del sistema que coinciden con el identificador {id}.")
    @GetMapping("/espacios/{id}")
    fun getEspaciosPorId(@PathVariable id : Int): List<Espacio> {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
        conjuntoEspacios.add(espacio);

        return conjuntoEspacios;
    }

    @Operation(
        summary = "Obtiene todos los espacios del sistema que coinciden con la categoría pasada como parámetro",
        description = "Obtiene una lista con todos los espacios del sistema que coinciden con la categoria {categoria}.")
    @GetMapping("/espacios/{categoria}")
    fun getEspaciosPorCategoria(@PathVariable categoria : TipoEspacio): List<Espacio> {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
        conjuntoEspacios.add(espacio);

        return conjuntoEspacios;
    }

    @Operation(
        summary = "Obtiene todos los espacios del sistema que coinciden con el número máximo de ocupantes pasado como parámetro",
        description = "Obtiene una lista con todos los espacios del sistema que coinciden con el número máximo de ocupantes {numMaxOcupantes}.")
    @GetMapping("/espacios/{numMaxOcupantes}")
    fun getEspaciosPorNumMaxOcupantes(@PathVariable numMaxOcupantes: Int): List<Espacio> {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
        conjuntoEspacios.add(espacio);

        return conjuntoEspacios;
    }

    @Operation(
        summary = "Obtiene todos los espacios del sistema que se encuentran en la planta como parámetro",
        description = "Obtiene una lista con todos los espacios del sistema que coinciden que se encuentran en la planta {planta}.")
    @GetMapping("/espacios/{planta}")
    fun getEspaciosPorPlanta(@PathVariable planta : Int): List<Espacio> {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
        conjuntoEspacios.add(espacio);

        return conjuntoEspacios;
    }

    // ----------------- RESERVA --------------------
    @Operation(
        summary = "Permite a un usuario con rol de gerente obtener todas las reservas vivas del sistema",
        description = "Obtiene una lista con todas las Reservas vivas del sistema asociadas si el usuario con id {idUsuario} .")
    @GetMapping("/reservas")
    fun getReservasVivas(@PathVariable  idUsuario: Int): List<Reserva> {
            val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
            val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
            conjuntoEspacios.add(espacio);
            val reserva = Reserva(conjuntoEspacios, TipoDeUsoReserva.docencia, 15, Date.valueOf("12/04/2024 18:00"), Date.valueOf("12/04/2024 20:00"), "la mejor");
            val conjuntoReservas : MutableList<Reserva> = ArrayList<Reserva>();
            conjuntoReservas.add(reserva);

            return conjuntoReservas;
    }

    @Operation(
        summary = "Permite que un usuario realice una reserva",
        description = "Permite que el usuario con identificador 'idUsuario' realice una reserva.")
    @PostMapping("/reservas")
    fun addReserva(@Parameter(name = "espacio", description = "Espacio o espacios que el usuario desea reservar", example = "espacio1, espacio2, yokse") @RequestParam(required = true) tamano : Double,
                   @Parameter(name = "id", description = "Identificador del espacio que se desea añadir al sistema", example = "72") @RequestParam(required = true) id : Int,
                   @Parameter(name = "tipo de espacio", description = "El tipo de espacio que es el espacio que se desea añadir al sistema", example = "Aula") @RequestParam(required = true) tipoEspacio : TipoEspacio,
                   @Parameter(name = "número máximo de ocupantes", description = "Número de ocupantes máximos que tiene el espacio que se desea añadir al sistema", example = "150") @RequestParam(required = true) numMaxOcupantes : Int,
                   @Parameter(name = "planta", description = "Planta en la que se encuentra el espacio que se desea añadir al sistema", example = "2") @RequestParam(required = true) planta: Int,
                   @Parameter(name = "reservable", description = "Indica si el espacio que se desea añadir al sistema se puede reservar o no", example = "True") @RequestParam(required = true) reservable: Boolean,
                   @Parameter(name = "categoria de reserva de espacio", description = "Indica la categoría de reserva del espacio que se desea añadir al sistema en caso de que sea reservable", example = "Aula") @RequestParam(required = false) categoriaReserva: TipoEspacio,
                   @Parameter(name = "horario de reserva disponible", description = "Indica el horario disponible de reserva que tiene el espacio que se desea añadir al sistema en caso de que sea reservable", example = "09:00-14:00") @RequestParam(required = false) horarioReservaDisponible: String,
                   @Parameter(name = "porcentaje de uso maximo", description = "Indica el porcentaje máximo permitido del espacio que se desea añadir al sistema", example = "50.0") @RequestParam(required = false) porcentajeUsoMaximo: Double): Reserva {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
        conjuntoEspacios.add(espacio);
        val reserva = Reserva(conjuntoEspacios, TipoDeUsoReserva.docencia, 15, Date.valueOf("12/04/2024 18:00"), Date.valueOf("12/04/2024 20:00"), "la mejor");
        return reserva;
    }

    @Operation(
        summary = "Obtiene todas las reservas del sistema asociadas a un usuario",
        description = "Obtiene una lista con todas las Reservas del sistema asociadas a un usuario.")
    @GetMapping("/reservas/{idUsuario}")
    fun obtenerReservasUsuario(@PathVariable idUsuario: Int): List<Reserva> {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
        conjuntoEspacios.add(espacio);
        val reserva = Reserva(conjuntoEspacios, TipoDeUsoReserva.docencia, 15, Date.valueOf("12/04/2024 18:00"), Date.valueOf("12/04/2024 20:00"), "la mejor");
        val conjuntoReservas : MutableList<Reserva> = ArrayList<Reserva>();
        conjuntoReservas.add(reserva);

        return conjuntoReservas;
    }

    @Operation(
        summary = "Obtiene todos los espacios del sistema que están disponibles para reservar",
        description = "Obtiene una lista con todos los espacios del sistema disponibles paara reservar.")
    @GetMapping("/espacios/disponible")
    fun obtenerEspaciosDisponibles(): List<Espacio> {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
        conjuntoEspacios.add(espacio);

        return conjuntoEspacios;
    }

    // ---------- DUDAS -----------
    // HorarioDisponibleReserva -> HorarioDisponibleReservaInicio y HorarioDisponibleReservaFinal ?
    // IDs -> int ? UUID ? String ?
    // Si al buscar numMaxOcupantes y planta coincide???
    // Strings deben ser required siempre?
    // Atributos dependientes de si el espacio es reservable o no?



    @Operation(
        summary = "Informa si un espacio está disponible para reservar o no",
        description = "Informa si el espacio con identificador {id} está disponible para reservar o no.")
    @GetMapping("/espacios/{id}/disponible")
    fun espacioDisponible(): Boolean {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        if(espacio.reservable){
            return true;
        }
        else{
            return false;
        }
    }
}