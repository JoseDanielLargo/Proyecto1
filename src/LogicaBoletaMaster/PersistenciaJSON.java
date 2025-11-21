package LogicaBoletaMaster;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PersistenciaJSON {

	

	private static final Gson gson = new GsonBuilder()
			.registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))) //dice como convertir un objeto LocalDateTime a JSON, src.format lo convierte a texto y newJsonPrimitive lo mete al JSON
			.registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)) //convierte el texto del JSON a un objeto LocalDateTime de java
			.setPrettyPrinting().create(); //solo hace que se vea mejor osea mas claro

	//funcion que guarda el objeto de java en un JSON
    public static <T> void guardar(String ruta, T objeto) { //recibe T (evento, usuario...)
        try (Writer writer = new FileWriter(ruta)) { //abre el archivo JSON con la ruta especificada (usuarios, eventos..) cierra el archivo al terminar
            gson.toJson(objeto, writer); //convierte el objeto que recibe usando la definicion del gson de arriba
        } catch (IOException e) {
            System.err.println("Error guardando JSON: " + e.getMessage());//muestra error si no puede escribir en la ruta
        }
    }

    public static <T> T cargar(String ruta, Class<T> clase) { //carga desde un archio JSON un objeto java
        try (Reader reader = new FileReader(ruta)) { //lee el archivo JSON de la ruta
            return gson.fromJson(reader, clase); //convierte el JSON al tipo de la clase (usuario, evento...)
        } catch (FileNotFoundException e) { //si el archivo no existe o no lo puede leer avisa y retorna null
            System.out.println("Archivo no encontrado: " + ruta);
            return null;
        } catch (IOException e) {
            System.err.println("Error leyendo JSON: " + e.getMessage());
            return null;
        }
    }

    public static <T> List<T> cargarLista(String ruta, Type tipoLista) { //carga un archivo JSON que contenga una lista con las listas compatibles con GSON
        try (Reader reader = new FileReader(ruta)) {
            return gson.fromJson(reader, tipoLista); //convierte JSON a lista
        } catch (FileNotFoundException e) { //si no existe el archivo en la ruta devuelve una lista vacia
            System.out.println("Archivo no encontrado: " + ruta);
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error leyendo lista JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
