package org.example;
import org.iesalandalus.programacion.utilidades.Entrada;

import java.io.File;


/**
 * Clase Principal donde se ejecuta el código y muestra la aplicación de gestión de departamentos a través de su menú.
 */

public class Main {

    private static File fichero = new File("datos/AleatorioDepart.dat");
    private static GestionarDepartamentos gestion = new GestionarDepartamentos(fichero);
    private static boolean modoDiagnostico = false;

    public static void main(String[] args) {
        int opcion;
        // Bloqueo de seguridad al arrancar si hay datos corruptos.
        if (!gestion.integridad()){
            modoDiagnostico = true;
            System.out.println("AVISO: Se han detectado anomalías. Aplicación en MODO DIAGNÓSTICO.");
            System.out.println("Solo se permiten operaciones de inspección, estadísticas e integridad.");
        }

        do{
            mostrarMenu();
            opcion=Entrada.entero();

            if(modoDiagnostico && opcion >= 1 && opcion <=6 ){
                System.out.println("Operación bloqueada: La aplicación está en modo de diagnóstico.");
                System.out.println("Solo se permiten operaciones de lectura: inspección (7)," +
                        " estadísticas (8), integridad (9) o salir (0).");
            }else{
                short numDepartamento;
                String nombre;
                String localidad;
                short empleados;
                switch (opcion){
                    case 1:
                        numDepartamento = pedirNumDepartamentoValido();
                        nombre = pedirTextoValido("Introduce el nombre del departamento: ");
                        localidad = pedirTextoValido("Introduce la localidad del departamento: ");
                        empleados = pedirEmpleadosValidos("Introduce el número de empleados (0 - 32767): ");
                        gestion.insertaDepartamento(numDepartamento,nombre,localidad,empleados);
                        break;
                    case 2:
                        numDepartamento = pedirNumDepartamentoValido();
                        gestion.consultaDepartamentos(numDepartamento);
                        break;
                    case 3:
                        numDepartamento = pedirNumDepartamentoValido();
                        empleados = pedirEmpleadosValidos("Introduce el número de empleados (0 - 32767): ");
                        gestion.actualizaNumeroEmpleados(numDepartamento,empleados);
                        break;
                    case 4:
                        numDepartamento = pedirNumDepartamentoValido();
                        nombre = pedirTextoValido("Introduce el nuevo nombre: ");
                        localidad = pedirTextoValido("Introduce la nueva localidad: ");
                        gestion.actualizaNombreLocalidad(numDepartamento,nombre,localidad);
                        break;
                    case 5:
                        numDepartamento = pedirNumDepartamentoValido();
                        System.out.print("¿Estás seguro de que deseas eliminar el departamento " + numDepartamento + "? (S/N): ");
                        String confirmacion = Entrada.cadena();
                        if (confirmacion.equalsIgnoreCase("S")) {
                            gestion.eliminaDepartamento(numDepartamento);
                        } else {
                            System.out.println("Operación de borrado cancelada.");
                        }
                        break;
                    case 6:
                        gestion.listadoDepartamentos();
                        break;
                    case 7:
                        gestion.inspeccionFisica();
                        break;
                    case 8:
                        gestion.estadisticas();
                        break;
                    case 9:
                        modoDiagnostico = !gestion.integridad();
                        break;
                    case 0:
                        System.out.println("Saliendo de la aplicación....");
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                }
            }

        }while(opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("Bienvenido al programa de gestión de departamentos ,¿Qué deseas realizar? ");
        System.out.println("1. Insertar departamento");
        System.out.println("2. Consultar departamento");
        System.out.println("3. Actualizar número de empleados");
        System.out.println("4. Actualizar nombre y localidad");
        System.out.println("5. Eliminar departamento");
        System.out.println("6. Listar departamentos activos");
        System.out.println("7. Inspección física del fichero");
        System.out.println("8. Estadísticas del fichero");
        System.out.println("9. Comprobar integridad");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
    }

    // Método que pide por teclado el número de departamento y usa la clase Departamento para validar el rango.
    private static short pedirNumDepartamentoValido(){
    int id;
        do{
            System.out.print("Introduce el número de departamento (1 - 32767): ");
            id = Entrada.entero();
        }while(!Departamento.validarDepartamento((short) id, (short) 0));
        return (short) id;
    }

    // Método que sirve para asegurar que los textos cumplen las normas usando la clase Departamento.
    private static String pedirTextoValido(String mensaje){
        String texto;
        do{
            System.out.print(mensaje);
            texto = Entrada.cadena();
            if(!Departamento.validarTexto(texto)){
                System.out.println("Error: El texto no puede estar vacío o ni superar los 15 carácteres.");
            }
        }while(!Departamento.validarTexto(texto));
    return texto;
    }

    //Método que pide empleados y usa la clase Departamento para validar que no sea negativo.
    private static short pedirEmpleadosValidos(String mensaje){
    int empleados;
    do{
        System.out.println(mensaje);
        empleados = Entrada.entero();
        if (empleados < 0 || empleados > 32767) {
            System.out.println("Error: El número de empleados no puede ser negativo ni superar 32767.");
        }
    }while(empleados < 0 || empleados > 32767);
    return (short) empleados;
    }
}