# PROYECTO2-EDD

## DESCRIPCION
Sistema de gestion y transferencia de libros entre bibliotecas.
El proyecto utiliza estructuras de datos como listas enlazadas, pilas, colas, tablas hash, arboles y grafos.
Permite cargar archivos (bibliotecas, conexiones, catalogo), calcular rutas optimas por tiempo o costo y visualizar las transferencias mediante una interfaz grafica.

---

## REQUISITOS
- Java JDK 17 o superior
- Maven 3.8 o superior
- Graphviz instalado (para generar imagenes de grafos y estructuras)

Verificar la instalacion de Graphviz:
```bash
dot -V
```

---

## COMPILACION
Para compilar el proyecto, ubicarse en la carpeta raiz (donde se encuentra el archivo `pom.xml`) y ejecutar:

```bash
mvn clean package
```

Esto generara el archivo ejecutable dentro de la carpeta:

```
target/Proyecto2-EDD-SNAPSHOT-jar-with-dependencies.jar
```

Si se desea limpiar y recompilar desde cero:
```bash
mvn clean
mvn package
```

Durante el proceso, Maven descargara automaticamente todas las dependencias definidas en el archivo `pom.xml`.

---

## EJECUCION
Para ejecutar el programa, usar el siguiente comando desde la carpeta `target` o indicando la ruta completa del archivo `.jar`:

```bash
java -jar Proyecto2-EDD-SNAPSHOT-jar-with-dependencies.jar
```

**Notas importantes:**
- Si Graphviz no genera imagenes, asegurarse de que su carpeta `bin` este agregada a la variable de entorno `PATH`.
- Ejecutar el programa desde una terminal para visualizar los mensajes del sistema.
- El archivo `jar-with-dependencies` incluye todas las librerias necesarias, por lo que no se requiere configuracion adicional.

---

## ESTRUCTURA DEL PROYECTO
La estructura general del proyecto es la siguiente:

```
Proyecto2-EDD/
│
├── src/
│   ├── main/java/com/mycompany/proyecto2/edd/
│   │   ├── Backend/          # Logica principal del sistema
│   │   ├── Frontend/         # Interfaz grafica con Swing
│   │   ├── Estructuras/      # Implementaciones de colas, pilas, grafos, etc.
│   │   ├── Objetos/          # Clases Libro, Biblioteca, Conexion, etc.
│   │   └── Utilidades/       # Clases de apoyo y generadores de imagenes
│   │
│   └── resources/            # Archivos de recursos si aplica
│
├── target/
│   └── Proyecto2-EDD-SNAPSHOT-jar-with-dependencies.jar
│
└── pom.xml                   # Archivo de configuracion de Maven
```

---

## DEPENDENCIAS PRINCIPALES
El archivo `pom.xml` contiene todas las dependencias necesarias para ejecutar el proyecto correctamente.

Ejemplo de dependencias utilizadas:
- `maven-shade-plugin` → para empaquetado con dependencias
- `guru.nidi.graphviz` → generacion de imagenes de grafos
- `swing` y `awt` → interfaz grafica 
- `org.jfree` → graficos estadisticos 

Estas dependencias se descargan automaticamente al compilar con Maven.

---

## PROBLEMAS COMUNES

**1. No se generan las imagenes del grafo**
- Verificar que Graphviz este correctamente instalado. 
- Ejecutar en consola:
```bash
dot -V
```
- Si no aparece version, agregar la carpeta de Graphviz al PATH.

**2. Error al ejecutar el JAR**
- Revisar que se este utilizando una version de Java 17 o superior:
```bash
java -version
```
- Confirmar el nombre correcto del archivo en la carpeta `target`.

**3. Error de carga de archivos**
- Asegurarse que los archivos CSV esten en la ruta correcta. 
- Verificar que cada archivo tenga los encabezados esperados.

