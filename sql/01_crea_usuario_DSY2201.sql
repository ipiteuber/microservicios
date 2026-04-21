/*
 * =============================================================================
 * DSY2201 - Desarrollo Full Stack I - Semana 6
 * Script de creacion de usuario Oracle
 * =============================================================================
 *
 * Crea el usuario DSY2201_S6 que sera utilizado por los dos microservicios:
 *   - ms-citas-medicas
 *   - ms-ordenes-mascotas
 *
 * Credenciales:
 *   Usuario:   DSY2201_S6
 *   Password:  Semana6Duoc#2026
 *
 * =============================================================================
 */


CREATE USER DSY2201_S6
IDENTIFIED BY "Semana6Duoc#2026"
DEFAULT TABLESPACE DATA
TEMPORARY TABLESPACE TEMP
QUOTA UNLIMITED ON DATA;

GRANT CREATE SESSION, RESOURCE, CONNECT TO DSY2201_S6;
GRANT CREATE TABLE, CREATE SEQUENCE, CREATE VIEW TO DSY2201_S6;

ALTER USER DSY2201_S6 DEFAULT ROLE RESOURCE;

