# Bitácora de evolución del repositorio

**Repositorio:** [manuelmartinez4/procesoDesarrolloSoftware](https://github.com/manuelmartinez4/procesoDesarrolloSoftware)  
**Fecha de revisión:** 21 de septiembre de 2026.  
**Período:** desde el primer commit del 12 de agosto de 2026 hasta [`c8da5f7`](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/c8da5f787bf5000a2cc472ff9a1eb913658b47c1).  
**Cobertura:** los 29 commits alcanzables desde `main` hasta ese punto, incluidos el borrado inicial y el merge del PR #1. La publicación de esta bitácora se registra como entrega documental al final.

## Cómo se elaboró

Se revisaron los cambios reales de cada commit contra su padre, además del código inicial y actual. Las fechas son las de autor registradas por Git, con zona UTC−03:00, y el orden respeta la ascendencia de los commits. Cada entrada enlaza al diff original.

Las asociaciones con GRASP, SOLID y code smells son una interpretación del código observable. No se atribuye una intención al autor a partir del título del commit. Cuando un cambio es de estructura, sintaxis o integración, se indica; no se le asigna un principio artificialmente.

Un **code smell** es una señal de un problema de diseño, como duplicar lógica o ubicar una responsabilidad en la clase equivocada. Se distingue de un **defecto**, como una llamada que no compila, y de una **decisión de alcance**, como retirar una función que el menú no utiliza.

Se extrajo cada versión en una carpeta aislada y se compilaron todas sus fuentes de producción con **javac 11.0.30**, codificación UTF-8 y el `lib/gson.jar` de esa revisión. Los archivos `out/*.class` del repositorio no se usaron como evidencia de compilación.

| Resultado histórico | Cantidad |
|---|---:|
| Compila | 17 |
| No compila | 11 |
| Sin fuentes, por borrado de la carpeta del proyecto | 1 |
| Total de commits revisados | 29 |

Los errores históricos son estados intermedios conservados por Git. **La versión actual compila y pasa 43 comprobaciones de contrato.** El anexo [verificación histórica](docs/verificacion-historial.json) conserva el estado, la cantidad de fuentes y los diagnósticos por commit. Compilar una versión no equivale a verificar toda su funcionalidad.

## Dónde se observa SOLID

| Principio | Aplicación concreta | Evidencia principal |
|---|---|---|
| **S: responsabilidad única** | El depósito administra su auditoría; el árbol cuenta sus nodos; la lectura JSON sale del menú; la búsqueda devuelve un resultado y el menú lo muestra. | `884305d`, `42c981e`, `0db87a9`, `cdb0f85`. Son mejoras puntuales, no una separación completa de toda la aplicación. |
| **O: abierto a extensión y cerrado a modificación** | La fuente de carga se cambia mediante implementaciones de las interfaces, manteniendo la lógica de opciones del menú. | Punto de extensión en `cdb0f85`; uso concreto con JSON y memoria en `c8da5f7`. La composición sí debe elegir qué fuente inyectar. |
| **L: sustitución de Liskov** | JSON y memoria cumplen el mismo contrato de carga: respetan IDs, prioridad, datos previos, auditorías y conexiones. | `c8da5f7`: comprobaciones comunes y recorridos del menú con ambas implementaciones. La afirmación se limita a los casos y condiciones verificados. |
| **I: segregación de interfaces** | Inventario y depósitos tienen contratos separados, cada uno con una sola operación. Se pueden usar implementaciones independientes. | `c8da5f7`: `CargadorInventario`, `CargadorDepositos` y constructor con dos dependencias. |
| **D: inversión de dependencias** | El menú recibe abstracciones por constructor; no crea el cargador concreto dentro de su lógica. | `cdb0f85` y `c8da5f7`. La inversión se aplica a la carga; otros colaboradores del menú siguen siendo clases concretas. |

## Cronología completa

### 01. 12/08/2026 · 6277555

**Commit:** [initial commit](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/6277555bc08a29e58c9bd515d14beb81f6ace8be)  
**Verificación:** Compila (9 archivos de producción).

Se incorpora el sistema completo bajo `tp/`: paquetes, camión, centro de distribución, ABB, depósitos, red de rutas, menú por consola, datos JSON, Gson y archivos compilados.

- **GRASP:** Experto en información: `RedDepositos` calcula rutas usando su matriz; `CentroDistribucion` administra sus colas. Creador: `ABB` construye los depósitos que contiene. Son responsabilidades presentes desde la primera versión.
- **SOLID:** Hay una separación inicial por clases, pero no permite afirmar cumplimiento integral de SRP: `MenuPrincipal` reúne interacción, lectura JSON y coordinación; varias clases del dominio también imprimen.
- **Code smells y arreglos:** Se observan duplicación entre descargar/deshacer carga y entre los métodos de agregar rutas; estado de `Deposito` accesible desde otras clases; conteo de nodos del ABB ubicado en `RedDepositos`; métodos extensos en el menú. No son correcciones realizadas en este commit.
- **Observación:** Es la línea de base del análisis. La primera versión ya compila; los `.class` incluidos no se usaron para comprobarlo.

### 02. 12/08/2026 · 5fde6ff

**Commit:** [Delete tp directory](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/5fde6ff4a2b69a806e100b645fb4fab6cfd40d9e)  
**Verificación:** Sin fuentes Java.

Se elimina la carpeta `tp/` con todos sus archivos.

- **GRASP:** Sin aplicación específica: es una operación de estructura del repositorio.
- **SOLID:** Sin aplicación específica.
- **Code smells y arreglos:** No constituye por sí sola una corrección de diseño ni de code smells.
- **Observación:** Este estado no contiene fuentes Java para compilar. Forma parte de la reubicación completada en el commit siguiente.

### 03. 12/08/2026 · 62966bd

**Commit:** [Add files via upload](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/62966bda1610dd646029dbc2ad4800f794e2030e)  
**Verificación:** Compila (9 archivos de producción).

Se vuelven a agregar los archivos en la raíz del repositorio, sin el prefijo `tp/`.

- **GRASP:** Se mantienen las responsabilidades de la versión inicial; no se agregan patrones nuevos.
- **SOLID:** Sin cambio de diseño específico.
- **Code smells y arreglos:** Reorganización de carpetas; no corrige los problemas del código inicial.
- **Observación:** Se verificó que el árbol Git de esta revisión coincide exactamente con el subárbol `tp/` del primer commit. No se atribuyen mejoras funcionales a esta recarga.

### 04. 18/08/2026 · 508ebcc

**Commit:** [placeholder ui](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/508ebccf0491158a4f1253548c5201e13b26b8da)  
**Verificación:** Compila (10 archivos de producción).

Se agrega `PlaceholderUI`, una ventana Swing con texto de ejemplo, y `Main` pasa de iniciar el menú por consola a mostrar esa ventana.

- **GRASP:** Se ubica la presentación gráfica en una clase propia, lo que favorece cohesión. No hay evidencia de una arquitectura MVC completa ni de un Controlador GRASP independiente.
- **SOLID:** Separación puntual de la creación de la ventana respecto de `Main`; no resuelve las responsabilidades mezcladas del menú existente.
- **Code smells y arreglos:** No se identifica una corrección de code smell específica. Es un cambio de punto de entrada y una interfaz provisional.
- **Observación:** Desde aquí `Main` ya no abre `MenuPrincipal`. Ese comportamiento sigue presente en la versión auditada; las comprobaciones del menú lo instancian expresamente.

### 05. 25/08/2026 · 348fc04

**Commit:** [Refactor deshacerUltimaCargaDelCamion method](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/348fc04f0ce21cd88d9d34343699f6ef582f5828)  
**Verificación:** Compila (10 archivos de producción).

`Camion.deshacerUltimaCargaDelCamion()` deja de repetir la validación y el `pop()` de la pila; delega en `descargarPaqueteDelCamion()`.

- **GRASP:** Alta cohesión: la extracción del último paquete queda concentrada en una operación del camión, que posee la pila.
- **SOLID:** No introduce un principio SOLID nuevo; conserva la responsabilidad del camión.
- **Code smells y arreglos:** Duplicate Code: se elimina la repetición del mismo algoritmo de descarga.
- **Observación:** Se conserva el comportamiento de devolver `null` cuando la pila está vacía. DRY es una regla de reutilización, no una sexta letra de SOLID.

### 06. 25/08/2026 · 8835436

**Commit:** [Refactor RedDepositos class and methods](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/8835436e9b024b21dec2f701dec9b528740e8994)  
**Verificación:** Compila (10 archivos de producción).

`RedDepositos.agregarRuta(origen, destino)` delega en `agregarRutaPonderada(origen, destino, 1)`.

- **GRASP:** Alta cohesión y Experto: la red concentra la modificación de su matriz de adyacencia.
- **SOLID:** No agrega una extensión polimórfica ni demuestra OCP por sí solo; es reutilización interna.
- **Code smells y arreglos:** Duplicate Code: se centralizan la ampliación de capacidad y la asignación bidireccional de la conexión.
- **Observación:** Mantiene el peso 1 para las rutas no ponderadas y reutiliza un método que ya existía.

### 07. 25/08/2026 · 8a07721

**Commit:** [Refactor Deposito class to use private fields](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/8a077217467b97fb85c2811a6aa5db363448f097)  
**Verificación:** No compila (10 archivos de producción).

Los cinco atributos de `Deposito` pasan de visibilidad de paquete a `private`.

- **GRASP:** Primer paso hacia bajo acoplamiento: otras clases ya no deberían acceder directamente a la representación del depósito.
- **SOLID:** La encapsulación apoya un diseño más controlado, pero no equivale por sí sola a aplicar SRP, DIP o cualquier otro principio completo.
- **Code smells y arreglos:** Se intenta reducir Inappropriate Intimacy, la dependencia de otras clases respecto de los campos internos del depósito.
- **Observación:** El cambio queda incompleto: `ABB` y `RedDepositos` todavía acceden a esos campos. Esta revisión deja de compilar.

### 08. 25/08/2026 · c5f7216

**Commit:** [Enhance Deposito class with additional methods](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/c5f72168c706793f729fa4506d244be7978694f1)  
**Verificación:** No compila (10 archivos de producción).

Se agregan accesores para los hijos izquierdo/derecho y un setter para la fecha de auditoría.

- **GRASP:** Facilita que los clientes utilicen operaciones públicas en lugar de campos, como parte del paso hacia menor acoplamiento.
- **SOLID:** No hay un principio SOLID nuevo directamente acreditable.
- **Code smells y arreglos:** Continúa la corrección del acceso directo a datos internos, aunque los setters aún permiten que los clientes controlen detalles del depósito.
- **Observación:** Agregar getters y setters no basta: los consumidores no fueron actualizados en este commit, por lo que persisten los errores de compilación.

### 09. 25/08/2026 · 0b3f03e

**Commit:** [Refactor ABB class for better encapsulation](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/0b3f03e04aced078834b020fd5a43e3e44e64029)  
**Verificación:** No compila (10 archivos de producción).

`ABB.raiz` se vuelve privada y se reemplazan numerosos accesos directos a `Deposito` por getters y setters.

- **GRASP:** Bajo acoplamiento: se avanza en ocultar el estado y acceder mediante operaciones públicas.
- **SOLID:** La mejora es de encapsulación; no se presenta como una implementación completa de un principio SOLID.
- **Code smells y arreglos:** Reduce parcialmente Inappropriate Intimacy.
- **Observación:** La refactorización introduce llamadas sin `()` y el nombre incorrecto `bucarRecursivo`; también quedan accesos a campos privados. No compila. Las correcciones se completan después, principalmente en `30114fd`.

### 10. 25/08/2026 · 4519de7

**Commit:** [Refactor RedDepositos class and methods](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/4519de7777436e52d6c98b8efdd987ada821c502)  
**Verificación:** No compila (10 archivos de producción).

El conteo de `RedDepositos` intenta sustituir `raiz.izquierdo` y `raiz.derecho` por getters.

- **GRASP:** Intención de reducir acoplamiento con la representación de `Deposito`.
- **SOLID:** Sin aplicación nueva demostrable.
- **Code smells y arreglos:** La corrección del acceso directo queda incompleta; además, contar un ABB sigue siendo una responsabilidad ajena a la red de rutas.
- **Observación:** Los getters se escriben sin paréntesis. El método no queda corregido y la revisión no compila.

### 11. 25/08/2026 · 065c49b

**Commit:** [Refactor RedDepositos class and fix method calls](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/065c49b39a1e3e796949b72fef7ffeddacf8f1f3)  
**Verificación:** No compila (10 archivos de producción).

Se agregan los paréntesis faltantes a `getIzquierdo()` y `getDerecho()` en el conteo de `RedDepositos`.

- **GRASP:** Completa el acceso mediante operaciones públicas en ese método.
- **SOLID:** Corrección de sintaxis; no introduce un principio SOLID nuevo.
- **Code smells y arreglos:** Corrige un defecto de compilación concreto, que debe distinguirse de un code smell.
- **Observación:** Los errores de ese método se solucionan, pero todavía quedan errores en `ABB`. La revisión completa no compila.

### 12. 07/09/2026 · 30114fd

**Commit:** [Refactor ABB class for improved readability and fixes](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/30114fd9c40eba4b3110540c703dfbe5e73d209a)  
**Verificación:** Compila (10 archivos de producción).

Se corrigen las llamadas a getters, el nombre de `buscarRecursivo` y los accesos privados restantes en búsqueda e impresión por nivel de `ABB`.

- **GRASP:** Consolida el menor acoplamiento entre `ABB` y los campos de `Deposito`.
- **SOLID:** No agrega un principio nuevo; termina una refactorización de encapsulación iniciada antes.
- **Code smells y arreglos:** Corrige llamadas inválidas y referencias a campos privados. Es reparación de defectos y cierre de una mejora de encapsulación.
- **Observación:** El proyecto vuelve a compilar. La lógica de auditoría y los mensajes de búsqueda todavía siguen dentro de `ABB`.

### 13. 07/09/2026 · 18df0df

**Commit:** [Implement audit methods in Deposito class](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/18df0df533a4ff4b5cf02a4bc9728a2bf700222a)  
**Verificación:** Compila (10 archivos de producción).

Se incorporan `Deposito.necesitaAuditoria(limite)` y `Deposito.auditar()`.

- **GRASP:** Experto en información: el depósito conoce su fecha y su estado, y ahora puede decidir si necesita auditoría y actualizarse. Alta cohesión: ambas operaciones quedan junto a esos datos.
- **SOLID:** Favorece SRP al concentrar el comportamiento del depósito, pero la delegación desde el árbol aún no se realiza.
- **Code smells y arreglos:** Prepara la reducción de Feature Envy y del modelo centrado únicamente en getters/setters.
- **Observación:** En este commit `ABB` todavía ejecuta la lógica anterior. La mejora entra en uso en el siguiente.

### 14. 07/09/2026 · 884305d

**Commit:** [Refactor ABB class for improved auditing and reporting](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/884305d5dfdebf112d2810e4d09924309739bcfc)  
**Verificación:** Compila (10 archivos de producción).

`ABB.auditarDepositosRecursivo()` llama a `nodo.necesitaAuditoria(limite)` y `nodo.auditar()` en lugar de leer la fecha y actualizar dos campos por separado.

- **GRASP:** Experto, alta cohesión y bajo acoplamiento: el árbol recorre nodos y el depósito administra su auditoría.
- **SOLID:** SRP: se separan el recorrido del árbol y las reglas de estado del depósito.
- **Code smells y arreglos:** Feature Envy: el árbol deja de ejecutar reglas basadas principalmente en datos internos de otro objeto. También disminuye el acoplamiento a la secuencia de setters.
- **Observación:** El cálculo del límite una sola vez y el recorrido en postorden ya existían. Este commit mejora la distribución de responsabilidades, no introduce esas optimizaciones.

### 15. 07/09/2026 · 393b935

**Commit:** [Refactor RedDepositos class to improve structure](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/393b9358bea2a449db3e533d777f13e8816b5014)  
**Verificación:** Compila (10 archivos de producción).

Se elimina el método público `bfs(int idOrigen)` que imprimía el recorrido, además del comentario del método de conteo.

- **GRASP:** Alta cohesión: se reduce la mezcla entre operaciones de la red y presentación del recorrido por consola.
- **SOLID:** Avance puntual de SRP al retirar esa salida de presentación de la clase.
- **Code smells y arreglos:** Se elimina código sin llamadas internas localizadas y duplicación parcial del recorrido BFS. La afirmación de desuso se limita a los consumidores del repositorio.
- **Observación:** El BFS privado utilizado por `cantidadSaltos()` permanece. No se elimina el cálculo de rutas por cantidad de saltos.

### 16. 07/09/2026 · 4247c9e

**Commit:** [Refactor RedDepositos class structure](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/4247c9e9500adb954e046e00ac91519820e5b30c)  
**Verificación:** Compila (10 archivos de producción).

Se elimina `RedDepositos.dijkstra(origen, destino)`.

- **GRASP:** No se agrega una responsabilidad o patrón nuevo.
- **SOLID:** No corresponde atribuir automáticamente SRP u OCP a eliminar un algoritmo válido del grafo.
- **Code smells y arreglos:** Se retira funcionalidad sin llamadas internas localizadas, reduciendo código mantenido que el menú no utiliza.
- **Observación:** Es una reducción de funcionalidad: desaparece el cálculo de distancia ponderada. No se afirma que Dijkstra fuera incorrecto ni que borrarlo sea siempre una mejora.

### 17. 07/09/2026 · af56c06

**Commit:** [Refactor RedDepositos class for enhanced functionality](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/af56c061309f77a5d34d594a1b68f0e4d55aec6c)  
**Verificación:** Compila (10 archivos de producción).

Se elimina `RedDepositos.contarDeposito(Deposito raiz)`.

- **GRASP:** Alta cohesión y bajo acoplamiento: una red de rutas deja de recorrer una estructura de árbol ajena.
- **SOLID:** SRP: la clase se concentra en la red y deja de contar nodos del ABB.
- **Code smells y arreglos:** Responsabilidad mal ubicada, asociada a Feature Envy: el método utilizaba los enlaces del árbol desde una clase dedicada al grafo.
- **Observación:** Aunque el mensaje original habla de mejorar funcionalidad, el diff muestra una eliminación. El conteo se agrega en su lugar adecuado en el commit siguiente.

### 18. 07/09/2026 · 42c981e

**Commit:** [Implement deposit counting method](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/42c981e8b38275034b00f8d2a51120a7d8c38d71)  
**Verificación:** Compila (10 archivos de producción).

Se agregan `ABB.contarDepositos()` y un auxiliar recursivo privado que parte de la raíz interna.

- **GRASP:** Experto en información: el árbol tiene la raíz y los enlaces necesarios para contar sus nodos. Alta cohesión: el conteo queda dentro del ABB.
- **SOLID:** SRP: completa la reasignación de la operación que antes estaba en `RedDepositos`.
- **Code smells y arreglos:** Corrige la responsabilidad mal ubicada y evita que el cliente tenga que proporcionar la raíz del árbol.
- **Observación:** Es una reubicación realizada en dos commits (`af56c06` y este), no dos correcciones independientes del mismo problema.

### 19. 07/09/2026 · bfc7298

**Commit:** [Refactor Paquete class to use enum for ZonaDestino](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/bfc729859b2b7985ccac845937a7357d803d2812)  
**Verificación:** No compila (10 archivos de producción).

`Paquete<T>` pasa a `Paquete` con contenido `String`; la zona de destino pasa de texto libre a `enum ZonaDestino`. Se conserva la clasificación dentro del paquete.

- **GRASP:** Se mantiene Experto en información: el paquete clasifica su destino. El experto ya existía; el cambio principal está en la representación de la zona.
- **SOLID:** No introduce OCP ni LSP por el solo hecho de usar un enum o retirar genéricos.
- **Code smells y arreglos:** Primitive Obsession: las cuatro zonas pasan a un tipo explícito. También se simplifica una genericidad que los consumidores observados usaban con `String`.
- **Observación:** Cambia la API y todavía quedan usos de `Paquete<String>` y `new Paquete<>()`. La revisión no compila; los consumidores se adaptan en pasos posteriores.

### 20. 07/09/2026 · 2cc1177

**Commit:** [Refactor Camion methods and simplify names](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/2cc117766c4887e9171b3b25fa168917c1131d04)  
**Verificación:** No compila (10 archivos de producción).

`Camion` se adapta a `Paquete` sin genéricos y acorta sus operaciones a `cargarPaquete`, `descargarPaquete`, `deshacerUltimaCarga` y `mostrarPaquetes`.

- **GRASP:** Mantiene las responsabilidades del camión; no agrega un patrón nuevo.
- **SOLID:** No hay una aplicación SOLID adicional directamente demostrable.
- **Code smells y arreglos:** Mejora nombres redundantes que repetían el contexto de la clase. La eliminación de duplicación en deshacer carga ya había ocurrido en `348fc04`.
- **Observación:** El menú todavía usa los nombres anteriores y otras clases conservan referencias genéricas. El proyecto aún no compila.

### 21. 07/09/2026 · 32f2323

**Commit:** [Refactor CentroDistribucion class and remove comments](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/32f2323fc3620f83cc65b71fbefbb8a6a5b7419d)  
**Verificación:** No compila (10 archivos de producción).

`CentroDistribucion` se adapta al nuevo `Paquete`, renombra `p` a `paquete` y elimina `estaVacio()` y varios bloques de comentarios de conteo.

- **GRASP:** Mantiene la gestión de colas en el centro; la regla de prioridad todavía está escrita allí.
- **SOLID:** Sin principio nuevo específico.
- **Code smells y arreglos:** Mejora el nombre de la variable y elimina un método sin llamadas internas localizadas. La reducción de comentarios es limpieza, no prueba por sí sola de un patrón.
- **Observación:** La política de prioridad no se encapsula todavía; eso ocurre en `4a7e3b7`. Persisten errores de consumidores en `MenuPrincipal`.

### 22. 07/09/2026 · 28147ff

**Commit:** [Create CargadorJson for loading JSON data](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/28147ff5bf7a8f3288c0ed6ac2a272721debaebf)  
**Verificación:** No compila (11 archivos de producción).

Se agrega `CargadorJson` con operaciones para cargar inventario y depósitos.

- **GRASP:** Fabricación pura: se crea una clase técnica para la lectura de datos. Alta cohesión: reúne operaciones de esa fuente.
- **SOLID:** Prepara la aplicación de SRP, pero la extracción aún está incompleta porque el menú conserva sus métodos originales.
- **Code smells y arreglos:** Todavía no elimina la duplicación ni todas las responsabilidades sobrantes del menú; durante esta transición conviven ambas implementaciones.
- **Observación:** La nueva clase no se utiliza desde el menú en este commit. Los errores de compilación anteriores permanecen.

### 23. 07/09/2026 · cde8a8a

**Commit:** [Refactor MenuPrincipal.java to remove unused imports](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/cde8a8a9657a5e07b00c74bbc1adeca4fc4e88d6)  
**Verificación:** No compila (11 archivos de producción).

Se eliminan los imports de `Gson` y `FileReader` de `MenuPrincipal`.

- **GRASP:** Sin mejora efectiva acreditable en este estado.
- **SOLID:** Sin mejora efectiva acreditable en este estado.
- **Code smells y arreglos:** No se puede registrar como limpieza de imports sin uso: ambos tipos todavía se utilizan en los métodos JSON que siguen dentro del menú.
- **Observación:** El título del commit no coincide con el estado real del código. La eliminación anticipada agrega errores de compilación; la extracción se completa en `0db87a9`.

### 24. 07/09/2026 · 819c317

**Commit:** [Refactor MenuPrincipal to include CargadorJson](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/819c317b6bcebb6cfafe5dbef52bc0f66bdda8fd)  
**Verificación:** No compila (11 archivos de producción).

Se agrega a `MenuPrincipal` el campo `new CargadorJson()`.

- **GRASP:** Prepara una futura delegación, que todavía no se usa en los casos del menú.
- **SOLID:** No aplica DIP: el menú sigue dependiendo de una clase concreta y construyéndola directamente.
- **Code smells y arreglos:** No resuelve aún la mezcla de responsabilidades ni la duplicación de los métodos JSON.
- **Observación:** Es un paso de integración incompleto. Los métodos viejos y sus errores siguen presentes.

### 25. 07/09/2026 · 0db87a9

**Commit:** [Refactor MenuPrincipal for better structure](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/0db87a9a19e25a4af1f54643b894d2ba9277a15c)  
**Verificación:** Compila (11 archivos de producción).

Se extraen `mostrarMenu()` y `procesarOpcion()`, se delegan las dos cargas en `CargadorJson`, se eliminan los métodos JSON del menú y se actualizan nombres y usos de `Paquete`.

- **GRASP:** Fabricación pura y alta cohesión: la lectura JSON queda en su clase. El menú conserva la coordinación de opciones, aunque sigue siendo una clase de presentación y no un Controlador GRASP completamente separado.
- **SOLID:** SRP: el menú deja de leer archivos. DIP aún queda pendiente, porque crea un `CargadorJson` concreto.
- **Code smells y arreglos:** Reduce Long Method y responsabilidades acumuladas en `MenuPrincipal`; elimina la duplicación temporal de las cargas; actualiza llamadas que habían quedado obsoletas.
- **Observación:** El proyecto vuelve a compilar. Dividir el método del menú mejora su estructura, pero no elimina todas las razones de cambio de la clase.

### 26. 21/09/2026 · 4a7e3b7

**Commit:** [Aplicar GRASP a prioridad de paquetes e inicializacion de depositos](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/4a7e3b73bd37d3dddd8afa246deab438b248e2dd)  
**Verificación:** Compila (11 archivos de producción).

Se agrega `Paquete.esPrioritario()`; el centro lo utiliza. Se agrega `Deposito(id, auditado)` y `ABB` lo usa para inicializar el depósito sin configurar la auditoría con setters.

- **GRASP:** Experto: el paquete decide su prioridad y el depósito administra su estado inicial. Creador ya estaba presente en `ABB`; ahora construye el objeto con el estado requerido. Favorece cohesión y bajo acoplamiento.
- **SOLID:** Favorece SRP al trasladar las reglas a los objetos que poseen los datos. No introduce por sí solo una política intercambiable de prioridad.
- **Code smells y arreglos:** Reduce Feature Envy en la comprobación de prioridad y el acoplamiento temporal de crear un depósito y completar su estado desde fuera.
- **Observación:** La regla sigue siendo urgente o peso mayor a 50.0. La fecha para no auditados sigue siendo de hace 45 días. El constructor `Deposito(int)` conserva su comportamiento original.

### 27. 21/09/2026 · a9bd799

**Commit:** [Merge pull request #1 from manuelmartinez4/codex/grasp-prioridad-auditoria](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/a9bd799ceb9b1a7399fdda87b261bf9d0eb6a92d)  
**Verificación:** Compila (11 archivos de producción).

Se fusiona el PR #1 que contiene `4a7e3b7` en `main`.

- **GRASP:** Integra las mejoras GRASP del commit anterior; no incorpora otra implementación.
- **SOLID:** Integra los efectos descritos para el commit anterior.
- **Code smells y arreglos:** No se cuenta como una segunda corrección de los mismos smells.
- **Observación:** Se verificó que el árbol del merge coincide con el del segundo padre (`4a7e3b7`). Es un hito de integración sin cambios de código adicionales.

### 28. 21/09/2026 · cdb0f85

**Commit:** [Aplicar SRP a busqueda e invertir dependencia del cargador](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/cdb0f857f5e6e5b508348ecafa4deacac53f3757)  
**Verificación:** Compila (12 archivos de producción).

`ABB.buscar()` devuelve `Deposito` y el menú muestra el resultado. Se agrega `CargadorDatos`, `CargadorJson` lo implementa y `MenuPrincipal` lo recibe por constructor. Los textos de carga dejan de fijar el formato JSON.

- **GRASP:** Bajo acoplamiento, indirección y variaciones protegidas en la carga: el menú utiliza un contrato. La búsqueda queda separada de la presentación.
- **SOLID:** S: búsqueda separada de impresión. D: el menú depende de una abstracción recibida desde fuera. O: queda un punto de extensión para otros cargadores sin cambiar la lógica de opciones.
- **Code smells y arreglos:** Reduce la mezcla entre dominio y consola en la búsqueda, y la dependencia rígida respecto del cargador concreto.
- **Observación:** En este punto hay una sola implementación. No se presenta como una demostración de LSP ni como la separación explícita de las dos interfaces de carga.

### 29. 21/09/2026 · c8da5f7

**Commit:** [Aplicar ISP y verificar LSP con cargadores intercambiables](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commit/c8da5f787bf5000a2cc472ff9a1eb913658b47c1)  
**Verificación:** Compila (16 archivos de producción).

Se agregan `CargadorInventario` y `CargadorDepositos`; el menú recibe ambas dependencias por separado. Se agrega `CargadorEnMemoria`, se comparte el registro en `RegistroDatos` y se permiten rutas JSON configurables manteniendo las predeterminadas. Se incorpora una prueba común de contrato.

- **GRASP:** Polimorfismo: JSON y memoria atienden las mismas operaciones. Variaciones protegidas e indirección: el menú usa interfaces. Fabricación pura y alta cohesión: `RegistroDatos` concentra la aplicación de los datos a las estructuras.
- **SOLID:** I: un consumidor o cargador puede depender solo de inventario o solo de depósitos. L: ambas implementaciones cumplen las mismas postcondiciones comprobadas. D y O se mantienen al cambiar o combinar fuentes mediante los constructores.
- **Code smells y arreglos:** Se evita duplicar las reglas de registro al agregar memoria y se reduce la dependencia de rutas fijas para las comprobaciones. La interfaz anterior tenía solo dos métodos: esta separación es una extensión solicitada, no la reparación de una interfaz enorme.
- **Observación:** `CargadorDatos` queda como interfaz compuesta de compatibilidad y el constructor anterior del menú se conserva. Compilan 16 fuentes de producción y una prueba; pasan 43 comprobaciones con datos válidos, cargas repetidas, duplicados, fuentes vacías y menú con fuentes iguales o combinadas. No es una prueba formal para cualquier entrada posible.

## Relación con GRASP

| Patrón o principio GRASP | Evidencia y alcance |
|---|---|
| **Experto en información** | `Paquete.esPrioritario()` usa peso y urgencia; `Deposito` decide y ejecuta su auditoría; `ABB` cuenta los nodos que contiene. |
| **Creador** | `ABB` crea los depósitos que incorpora. Estaba presente al inicio y se refuerza pasando el estado inicial al constructor en `4a7e3b7`. |
| **Alta cohesión** | La auditoría se concentra en el depósito, el conteo en el árbol y las reglas de registro en `RegistroDatos`. |
| **Bajo acoplamiento** | Se reducen accesos directos a campos y reglas ajenas; el menú usa contratos de carga. Getters y setters por sí solos no eliminan todo acoplamiento. |
| **Polimorfismo** | JSON y memoria implementan las mismas operaciones; el menú las invoca a través de las interfaces. No necesita un `if` para distinguir esas implementaciones. |
| **Fabricación pura** | `CargadorJson` y `RegistroDatos` son clases técnicas introducidas para organizar lectura y registro, no entidades del negocio logístico. |
| **Indirección** | Las interfaces median entre el menú y la fuente concreta de los datos. |
| **Variaciones protegidas** | Los contratos aíslan al menú de cambios en la fuente de carga. No aíslan automáticamente todos los cambios del modelo o del formato de datos. |
| **Controlador** | `MenuPrincipal.procesarOpcion()` coordina acciones, pero sigue dentro de una clase con entrada y salida de consola. Es una aproximación a esa responsabilidad, no evidencia de un controlador de aplicación separado de la interfaz. |

## Qué mejoró y qué queda pendiente

Los ejemplos más claros de corrección de **Duplicate Code** son la delegación de deshacer carga en la descarga del camión (`348fc04`) y la delegación de agregar rutas en su variante ponderada (`8835436`). La clase compartida `RegistroDatos` evita duplicar las reglas al incorporar el segundo cargador.

La reducción de **Feature Envy** aparece al mover reglas de auditoría a `Deposito` (`18df0df` y `884305d`), ubicar el conteo en `ABB` (`af56c06` y `42c981e`) y ubicar la prioridad en `Paquete` (`4a7e3b7`). El **acceso excesivo a detalles internos** se reduce con la secuencia de encapsulación entre `8a07721` y `30114fd`.

La zona tipada mediante un enum reduce **Primitive Obsession** (`bfc7298`). La extracción de métodos y de lectura JSON reduce **Long Method** y la acumulación de responsabilidades del menú (`0db87a9`). Son mejoras localizadas; no se afirma que desaparecieron todos los problemas de diseño.

Al cierre de esta revisión:

- `Main` sigue mostrando `PlaceholderUI`, como desde `508ebcc`. Los recorridos de consola verificados instancian `MenuPrincipal` directamente.
- `ABB.imprimirNivel()`, `Camion.mostrarPaquetes()` y los cargadores todavía imprimen en consola. La separación entre lógica y presentación no está terminada en toda la aplicación.
- `CargadorJson` conserva su manejo general de excepciones mediante mensajes. La comprobación de sustitución cubre cargas exitosas con datos válidos y estructuras de destino no nulas; no demuestra equivalencia universal ante archivos inaccesibles, JSON malformado o entradas inválidas.
- Los valores de prioridad y auditoría (50 kg, 30 días y 45 días) conservan su comportamiento. No se registra una eliminación de esos valores fijos.
- Los archivos compilados de `out/` siguen versionados. Las verificaciones generan clases nuevas en una carpeta temporal; no certifican que esos binarios antiguos estén actualizados.

## Cómo comprobar L e I

El contrato común permite cargar los mismos datos desde JSON o desde memoria sin cambiar la lógica del menú. Para inventario, se preservan los datos y el orden de ingreso, se omiten IDs ya registrados y una carga repetida no duplica paquetes. Para depósitos, se conservan los existentes, se respeta la auditoría inicial de los nuevos y se incorporan sus rutas bidireccionales.

`CargadorDatos` se conserva como interfaz compuesta para mantener el constructor anterior. Los nuevos consumidores pueden usar solo `CargadorInventario` o solo `CargadorDepositos`; no necesitan implementar una operación que no utilizan.

Ejemplos de composición:

```java
CargadorJson json = new CargadorJson();
CargadorEnMemoria memoria = new CargadorEnMemoria(paquetes, depositos);

MenuPrincipal desdeJson = new MenuPrincipal(json, json);
MenuPrincipal desdeMemoria = new MenuPrincipal(memoria, memoria);
MenuPrincipal combinado = new MenuPrincipal(json, memoria);
```

En el ejemplo, `paquetes` es un `Paquete[]` y `depositos` es un `DepositoJson[]`, ambos no nulos. El constructor `new MenuPrincipal(json)` sigue siendo válido. Las pruebas incluyen clientes de una sola operación y un menú que combina inventario JSON con depósitos en memoria.

### Reproducir la compilación y las comprobaciones

Desde la raíz del repositorio, con JDK 11 y PowerShell:

```powershell
$salida = Join-Path $env:TEMP 'logistica-contratos'
New-Item -ItemType Directory -Path $salida -Force | Out-Null
$fuentes = @(Get-ChildItem -Path src, tests -Recurse -Filter '*.java' |
    Select-Object -ExpandProperty FullName)
javac -encoding UTF-8 -cp lib/gson.jar -d $salida @fuentes
if ($LASTEXITCODE -ne 0) { throw 'Fallo de compilación' }
java '-Dfile.encoding=UTF-8' -cp "$salida;lib/gson.jar" logica.CargadoresContratoTest
if ($LASTEXITCODE -ne 0) { throw 'Fallo de contrato' }
```

Resultado obtenido el 21/09/2026: **16 archivos de producción y 1 archivo de comprobaciones compilados; 43 comprobaciones aprobadas**. Los JSON de la prueba se crean temporalmente y se eliminan al terminar; no se modifican los inventarios del proyecto.

La prueba cubre duplicados, cargas repetidas, orden de prioridad, límite de 50 kg, conservación de datos y estructuras previas, estados y fechas de auditoría, rutas bidireccionales, fuentes vacías y recorridos del menú con JSON, memoria y una combinación. Demuestra el contrato en esos casos; implementar una interfaz sin cumplir ese comportamiento no sería suficiente para justificar Liskov.

## Entrega documental del 21/09/2026

Se agregan esta bitácora y el anexo de compilación histórica. Su finalidad es dejar trazabilidad de los 29 commits revisados, con enlaces y evidencia. Esta publicación documental no cambia el comportamiento del programa ni corrige por sí misma otro code smell.

El commit que contiene esta entrega puede consultarse en el [historial de la bitácora](https://github.com/manuelmartinez4/procesoDesarrolloSoftware/commits/main/BITACORA.md). Se mantiene fuera del conteo de versiones de código auditadas para evitar atribuirle una implementación adicional.
