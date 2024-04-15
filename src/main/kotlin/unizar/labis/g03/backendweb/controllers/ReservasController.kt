package unizar.labis.g03.backendweb.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.*
import unizar.labis.g03.backendweb.models.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeoutException


@Tag(name = "Reservas AdaByron", description = "API para la reserva de espacios en el edificio Ada Byron")
@RestController
class ReservasController(val rabbitTemplate: RabbitTemplate) {
    init {
        /*
        val messageConverter = object : Jackson2JsonMessageConverter(){
            override fun fromMessage(message: Message): Any {
                message.messageProperties.contentType = "application/json"
                return super.fromMessage(message)
            }
            /*
            override fun createMessage(objectToConvert: Any, messageProperties: MessageProperties): Message {
                messageProperties.contentType = "application/json"
                return super.createMessage(objectToConvert, messageProperties)
            }
             */
        }
        val utf16 = "application/json; charset=utf-16"

        messageConverter.setSupportedContentType(MimeTypeUtils.parseMimeType(utf16))
        rabbitTemplate.messageConverter = messageConverter

         */
    }

    // ----------------- PERSONA ---------------------
    @Operation(
        summary = "Permite a un usuario con rol de gerente obtener todas los usuarios registrados en el sistema",
        description = "Obtiene una lista con todos los usuarios registrados en el sistema si el usuario con identificador 'id' tiene rol de gerente.")
    @GetMapping("/personas")
    fun getPersonas(@RequestParam @Parameter(name = "idUsuario", description = "Identificador del usuario que desea obtener los usuarios registradas en el sistema", example = "795593") idUsuario : Int): List<Persona> {
        val rol : Rol = Rol.Estudiante;
        val conjuntoRoles : MutableList<Rol> = ArrayList();
        conjuntoRoles.add(rol);
        val persona = Persona("Adrian", "Arribas", "795593@gmail.com", conjuntoRoles, Departamento.InformaticaIngenieriaSistemas);
        val conjuntoPersonas : MutableList<Persona> = ArrayList();
        conjuntoPersonas.add(persona);

        return conjuntoPersonas;
    }

    @Operation(
        summary = "Obtiene la información de un usuario en concreto",
        description = "Obtiene la información del usuario con identificador igual al pasado como parámetro.")
    @GetMapping("/personas/{id}")
    fun getPersonasUsuario(@PathVariable id : Int): ResponseEntity<Persona> {
        val rol : Rol = Rol.Estudiante;
        val conjuntoRoles : MutableList<Rol> = ArrayList<Rol>();
        conjuntoRoles.add(rol);
        val persona = Persona("Adrian", "Arribas", "795593@gmail.com", conjuntoRoles, Departamento.InformaticaIngenieriaSistemas);


        return ResponseEntity.ok(persona);
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

        val response = rabbitTemplate.convertSendAndReceive("cambiarCaracteristicasPersonal", "{}")

        return persona;
    }

    // ---------------- ESPACIO ---------------------
    @Operation(
        summary = "Obtiene todos los espacios del sistema con posibilidad de añadir filtros por identificador, planta, máximo de ocupantes y categoría",
        description = "Obtiene una lista con todos los espacios del sistema con la posibilidad de añadir distintos filtros.")
    @GetMapping(value = ["/espacios"], produces = ["application/json"])
    fun getEspacios(
        @Parameter(name = "id", description = "Identificador del espacio que se desea añadir al sistema", example = "72") @RequestParam(required = false) id : Int,
        @Parameter(name = "planta", description = "Planta en la que se encuentra el espacio que se desea añadir al sistema", example = "2") @RequestParam(required = false) planta: Int,
        @Parameter(name = "numMaxOcupantes", description = "Número de ocupantes máximos que tiene el espacio que se desea añadir al sistema", example = "150") @RequestParam(required = false) numMaxOcupantes : Int,
        @Parameter(name = "categoriaReserva", description = "Indica la categoría de reserva del espacio que se desea añadir al sistema en caso de que sea reservable", example = "Aula") @RequestParam(required = false) categoriaReserva: TipoEspacio): Any? {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
        conjuntoEspacios.add(espacio);


        val response = rabbitTemplate.convertSendAndReceive("buscarEspacios",
            "{\"id\": \"$id\"," +
                    "\"categoria\": \"$categoriaReserva\"," +
                    "\"maxOcupantes\": \"$numMaxOcupantes\"," +
                    "\"planta\": \"$planta\"" +
                    "}")

        return conjuntoEspacios;
    }

    @Operation(
        summary = "Permite añadir la información de un nuevo espacio al sistema",
        description = "Permite añadir la información de un nuevo espacio al sistema")
    @PostMapping("/espacios")
    fun addEspacio(@Parameter(name = "tamano", description = "Tamaño que tiene el espacio que se desea añadir al sistema", example = "120.35") @RequestParam(required = true) tamano : Double,
                   @Parameter(name = "id", description = "Identificador del espacio que se desea añadir al sistema", example = "72") @RequestParam(required = true) id : Int,
                   @Parameter(name = "tipoEspacio", description = "El tipo de espacio que es el espacio que se desea añadir al sistema", example = "Aula") @RequestParam(required = true) tipoEspacio : TipoEspacio,
                   @Parameter(name = "numMaxOcupantes", description = "Número de ocupantes máximos que tiene el espacio que se desea añadir al sistema", example = "150") @RequestParam(required = true) numMaxOcupantes : Int,
                   @Parameter(name = "planta", description = "Planta en la que se encuentra el espacio que se desea añadir al sistema", example = "2") @RequestParam(required = true) planta: Int,
                   @Parameter(name = "reservable", description = "Indica si el espacio que se desea añadir al sistema se puede reservar o no", example = "True") @RequestParam(required = true) reservable: Boolean,
                   @Parameter(name = "categoriaReserva", description = "Indica la categoría de reserva del espacio que se desea añadir al sistema en caso de que sea reservable", example = "Aula") @RequestParam(required = false) categoriaReserva: TipoEspacio,
                   @Parameter(name = "horarioReservaDisponible", description = "Indica el horario disponible de reserva que tiene el espacio que se desea añadir al sistema en caso de que sea reservable", example = "09:00-14:00") @RequestParam(required = false) horarioReservaDisponible: String,
                   @Parameter(name = "porcentajeUsoMaximo", description = "Indica el porcentaje máximo permitido del espacio que se desea añadir al sistema", example = "50.0") @RequestParam(required = false) porcentajeUsoMaximo: Double): Espacio {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);

        return espacio;
    }

    @Operation(
        summary = "Permite a un usuario con rol de gerente modificar los siguientes atributos de un espacio: reservable, categoría de reserva de espacio" +
                "horario de reserva disponible, porcentaje de uso máximo permitido.",
        description = "Permite modificar los atributos de un espacio si el usuario con identificador 'idUsuario' tiene rol de gerente")
    @PutMapping("/espacios/{id}")
    fun updateEspacio(@PathVariable id : Int,
                   @Parameter(name = "idUsuario", description = "Identificador del usuario que desea modificar los datos del espacio", example = "795593") @RequestParam(required = true) idUsuario : Int,
                   @Parameter(name = "reservable", description = "Indica si el espacio que se desea añadir al sistema se puede reservar o no", example = "True") @RequestParam(required = true) reservable: Boolean,
                   @Parameter(name = "categoriaReserva", description = "Indica la categoría de reserva del espacio que se desea añadir al sistema en caso de que sea reservable", example = "Aula") @RequestParam(required = false) categoriaReserva: TipoEspacio,
                   @Parameter(name = "horarioReservaDisponible", description = "Indica el horario disponible de reserva que tiene el espacio que se desea añadir al sistema en caso de que sea reservable", example = "09:00-14:00") @RequestParam(required = false) horarioReservaDisponible: String,
                   @Parameter(name = "porcentajeUsoMaximo", description = "Indica el porcentaje máximo permitido del espacio que se desea añadir al sistema", example = "50.0") @RequestParam(required = false) porcentajeUsoMaximo: Double): Espacio {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);

        val response = rabbitTemplate.convertSendAndReceive("cambiarCaracteristicasEspacio", "{}")
        println("Response: " + response.toString())

        return espacio;
    }

    // ----------------- RESERVA --------------------
    @Operation(
        summary = "Permite a un usuario con rol de gerente obtener todas las reservas vivas del sistema",
        description = "Obtiene una lista con todas las Reservas vivas del sistema asociadas si el usuario con id 'idUsuario' tiene rol de gerente .")
    @GetMapping("/reservas")
    fun getReservasVivas(@Parameter(name = "idUsuario", description = "Identificador del usuario que desea obtener los espacios", example = "795593") @RequestParam(required = true) idUsuario : Int): List<Reserva> {
            val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
            val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
            conjuntoEspacios.add(espacio);
            val reserva = Reserva(conjuntoEspacios, TipoDeUsoReserva.Docencia, 15, Date(Date.parse("12/04/2024 18:00")), Date(Date.parse("12/04/2024 20:00")) , "la mejor");
            val conjuntoReservas : MutableList<Reserva> = ArrayList<Reserva>();
            conjuntoReservas.add(reserva);

            val response = rabbitTemplate.convertSendAndReceive("consultarReservas", idUsuario.toString())

            return conjuntoReservas;
    }

    @Operation(
        summary = "Permite que un usuario realice una reserva",
        description = "Permite que el usuario con identificador 'idUsuario' realice una reserva.")
    @PostMapping("/reservas")
    fun addReserva(@Parameter(name = "idUsuario", description = "Identificador del usuario que se desea realizar la reserva", example = "795593") @RequestParam(required = true) idUsuario : Int,
                   @Parameter(name = "espacio", description = "Espacio o espacios que el usuario desea reservar", example = "espacio1, espacio2, yokse") @RequestParam(required = true) espacio : List<String>,
                   @Parameter(name = "tipoUsoReserva", description = "El tipo de uso de reserva que va a tener la reserva que se va a añadir al sistema", example = "Docencia") @RequestParam(required = true) tipoUsoReserva : TipoDeUsoReserva,
                   @Parameter(name = "numMaxOcupantes", description = "Número de ocupantes máximos que va a tener la reserva que se va a añadir al sistema", example = "8") @RequestParam(required = true) numMaxOcupantes : Int,
                   @Parameter(name = "fechaInicio", description = "Fecha y hora de inicio a la que dará comienzo la reserva que se va a añadir al sistema", example = "2000-10-31T01:30:00.000-05:00") @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) fechaInicio: Date,
                   @Parameter(name = "fechaFinal", description = "Fecha y hora de inicio a la que dará comienzo la reserva que se va a añadir al sistema", example = "2000-10-31T01:30:00.000-05:00") @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) fechaFinal: Date,
                   @Parameter(name = "descripcion", description = "Descripción de la reserva que se va a añadir al sistema", example = "Reserva hecha por el grupo 03") @RequestParam(required = false) descripcion: String) : Reserva {
        val espacioAux = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
        conjuntoEspacios.add(espacioAux);
        val reserva = Reserva(conjuntoEspacios, TipoDeUsoReserva.Docencia, 15, fechaInicio, fechaFinal, "la mejor");

        /*
        rabbitTemplate.messageConverter = jsonConverter()

        val typeRef: ParameterizedTypeReference<PersonaOut> = object : ParameterizedTypeReference<PersonaOut>() {}
        val response = rabbitTemplate.convertSendAndReceiveAsType(
            "reservarEspacio",
            "{\"idPersona\": $idUsuario}",
            typeRef
        )

        val typeRef: ParameterizedTypeReference<Boolean> = object : ParameterizedTypeReference<Boolean>() {}

        */
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")



        val response = rabbitTemplate.convertSendAndReceive(
            "reservarEspacio",
            "{\"idUsuario\": \"$idUsuario\"," +
                    "\"idEspacio\": \"$espacio\"," +
                    "\"fechaInicio\": \"${formatter.format(fechaInicio)}\"," +
                    "\"fechaFinal\": \"${formatter.format(fechaFinal)}\"," +
                    "\"numAsistentesPrevistos\": $numMaxOcupantes," +
                    "\"descripcion\": \"$descripcion\"" +
                    "}",
        )
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
        val reserva = Reserva(conjuntoEspacios, TipoDeUsoReserva.Docencia, 15, Date(Date.parse("12/04/2024 18:00")), Date(Date.parse("12/04/2024 20:00")), "la mejor");
        val conjuntoReservas : MutableList<Reserva> = ArrayList<Reserva>();
        conjuntoReservas.add(reserva);

        val response = rabbitTemplate.convertSendAndReceive("consultarReservas", idUsuario.toString())

        return conjuntoReservas;
    }


    // FIX: faltan params
    @Operation(
        summary = "Permite a un usuario con rol de gerente eliminar una reserva del sistema",
        description = "Permite eliminar del sistema la reserva con identificador 'id' si el usuario con identificador 'idUsuario' tiene el rol de gerente.")
    @DeleteMapping("/reservas/{id}")
    fun eliminarReserva(@PathVariable id: Int): List<Reserva> {
        val espacio = Espacio(400.30, 45, TipoEspacio.Despacho, 150, 2, true, TipoEspacio.SalaComun, "18:30", 50.0);
        val conjuntoEspacios : MutableList<Espacio> = ArrayList<Espacio>();
        conjuntoEspacios.add(espacio);
        val reserva = Reserva(conjuntoEspacios, TipoDeUsoReserva.Docencia, 15, Date(Date.parse("12/04/2024 18:00")), Date(Date.parse("12/04/2024 20:00")), "la mejor");
        val conjuntoReservas : MutableList<Reserva> = ArrayList<Reserva>();
        conjuntoReservas.add(reserva);


        /*
         rabbitTemplate.messageConverter = jsonConverter()
        val typeRef: ParameterizedTypeReference<String> = object : ParameterizedTypeReference<String>() {}
        val response = rabbitTemplate.convertSendAndReceiveAsType(
            "eliminarReserva",
            //"{\"idPersona\": $id, \"idReserva\": $id}",
            "{}",
            typeRef
        )

         */

        val response = rabbitTemplate.convertSendAndReceive(
            "eliminarReserva",
            "{\"idPersona\": $id, \"idReserva\": $id}",
        ) as Int?

        // println(response)

        return conjuntoReservas;
    }

    /*
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

     */

    // ---------- DUDAS -----------
    // HorarioDisponibleReserva -> HorarioDisponibleReservaInicio y HorarioDisponibleReservaFinal ?
    // IDs -> int ? UUID ? String ?
    // Si al buscar numMaxOcupantes y planta coincide???
    // Strings deben ser required siempre?
    // Atributos dependientes de si el espacio es reservable o no?

    @GetMapping("/duplica")
    @Throws(TimeoutException::class)
    fun duplica(@RequestParam(value = "value") value: Int): String {
        // Al usar el convertSendAndReceive dejamos que RabbitTemplate implemente el
        // Request-Reply por nosotros. Envia un mensaje con value (compone automáticamente
        // un objeto Message con ese int), lo envía a la centralita "" con clave
        // de enrutado duplicar, y para la respuesta usa el direct-reply-to de
        // RabbitMQ o crea una cola temporal (podemos configurarlo). Escucha en esa
        // cola hasta que llega una respuesta, saca el payload y lo devuelve, en este
        // caso como Integer

        // Esto gestiona cualquier escenario normal automáticamente, incluyendo si tenemos
        // varios clientes concurrentes haciendo peticiones, puesto que RabbitTemplate
        // crea un consumidor (o reusa uno) para la respuesta de cada petición y
        // así se asegura de que las respuestas llegan a quien hizo las peticiones.

        val response = rabbitTemplate.convertSendAndReceive(
            "duplicar", value
        ) as Int?

        if (response == null) {
            throw TimeoutException()
        }

        // Lo que conseguimos es que efectivamente este método que responde a una
        // petición GET enviando un mensaje y esperando su respuesta parezca un
        // método síncrono, normal(con la diferencia de que hay un timeout y saltará una
        // excepción si la respuesta no llega en X segundos).
        return String.format("Tu resultado es %d!", response)
    }

    @ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(TimeoutException::class)
    fun timeout(): String {
        return "La petición no puede resolverse ahora"
    }
}