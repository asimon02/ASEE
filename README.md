# 🎶 ASEE Music Platform  
ASEE Music Platform es una propuesta arquitectónica inspirada en plataformas como **Bandcamp** o **Spotify**, diseñada para la **gestión, distribución y análisis de contenido musical**. El proyecto se centra en modelar un sistema con microservicios, flujos de datos, gestión de usuarios y recomendaciones personalizadas, integrando buenas prácticas de arquitectura de software empresarial.  

---

## 🚀 Tecnologías planteadas  
| Herramienta      | Descripción |
|------------------|-------------|
| **MongoDB**      | Almacenamiento de canciones, álbumes y metadatos. |
| **Stripe (sandbox)** | Validación de pagos y compras seguras. |
| **JWT**          | Autenticación y autorización de usuarios. |
| **Microservicios** | Separación de responsabilidades en Contenidos, Usuarios y Estadísticas. |
| **Frontend Web/Móvil** | Interfaz gráfica para artistas, clientes y oyentes. |

---

## 🔐 Autenticación y Seguridad  
- Registro y login mediante **JWT**.  
- Control de roles (usuarios, artistas, administradores).  
- Validación de pagos y accesos seguros a descargas.  
- Reporte y moderación automática de contenido inapropiado.  

---

## 🧠 Funcionalidades principales  
🎵 **Gestión de contenidos**:  
- Crear, actualizar y eliminar **álbumes y canciones**.  
- Almacenamiento de metadatos en MongoDB y ficheros de audio en repositorio seguro.  

👥 **Interacciones de usuarios**:  
- Registro/login, actualización de perfil y favoritos.  
- Creación de listas de reproducción.  
- Seguir a artistas y recibir notificaciones.  

📈 **Estadísticas y rankings**:  
- Top de canciones más reproducidas.  
- Ranking de discos más vendidos.  
- Recomendaciones automáticas basadas en favoritos, compras y reproducciones.  

🛒 **Compras y descargas**:  
- Validación de pagos mediante **Stripe sandbox**.  
- Descargas seguras de canciones y álbumes adquiridos.  

---

## 🧩 Arquitectura del sistema  
```plaintext
├── Contenidos/         # Gestión de canciones, discos y artistas
├── Usuarios/           # Registro, login, roles y pagos
├── Estadísticas/       # Rankings, métricas y recomendaciones
└── Frontend/           # Aplicación web/móvil para la interacción
```

---

## 📦 Modelo de datos

**Entidades principales**:
- Canciones
- Álbumes
- Artistas
- Discográficas
- Usuarios
- Compras

**Relaciones jerárquicas**:
- Canciones → Álbumes → Artistas.
- Usuarios → Compras → Acceso a contenidos.

---

## 📄 Documentación
- Flujos de comunicación entre módulos descritos con ejemplos (compra de canción, subida de disco, reproducciones).
- Diseño orientado a microservicios y APIs REST con comunicación desacoplada.
- Consistencia eventual entre servicios a través de APIs REST y colas ligeras (opcional).

---

## 👤 Autores

Proyecto realizado en el marco de la asignatura Arquitectura Software para Entornos Empresariales (ASEE).

**Grupo GC03**:
- Miguel Ángel Campón Iglesias
- José María Gordillo Gragera
- César Sánchez Montes
- Alberto Simón Fernández de la Mela

✨ Una propuesta de arquitectura moderna para la distribución de música digital, con foco en escalabilidad, seguridad y experiencia de usuario.
