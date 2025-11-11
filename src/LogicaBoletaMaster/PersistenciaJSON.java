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

	

	private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))).registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)).setPrettyPrinting().create();


    public static <T> void guardar(String ruta, T objeto) {
        try (Writer writer = new FileWriter(ruta)) {
            gson.toJson(objeto, writer);
        } catch (IOException e) {
            System.err.println("Error guardando JSON: " + e.getMessage());
        }
    }

    public static <T> T cargar(String ruta, Class<T> clase) {
        try (Reader reader = new FileReader(ruta)) {
            return gson.fromJson(reader, clase);
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + ruta);
            return null;
        } catch (IOException e) {
            System.err.println("Error leyendo JSON: " + e.getMessage());
            return null;
        }
    }

    public static <T> List<T> cargarLista(String ruta, Type tipoLista) {
        try (Reader reader = new FileReader(ruta)) {
            return gson.fromJson(reader, tipoLista);
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + ruta);
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error leyendo lista JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
