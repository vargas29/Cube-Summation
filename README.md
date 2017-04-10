# Cube-Summation

You are given a 3-D Matrix in which each block contains 0 initially. The first block is defined by the coordinate (1,1,1) and the last block is defined by the coordinate (N,N,N). There are two types of queries.

UPDATE x y z W
updates the value of block (x,y,z) to W.

QUERY x1 y1 z1 x2 y2 z2
calculates the sum of the value of blocks whose x coordinate is between x1 and x2 (inclusive), y coordinate between y1 and y2 (inclusive) and z coordinate between z1 and z2 (inclusive).


**Capa de presentacion** La capa del cliente es ejecutada en el navegador web y desarrollada en la carpeta resources  su función es capturar datos desde el cliente, enviarlos al backend y recibir respuesta  la carpeta Public  El contenido de esta carpeta será accesible por tus usuarios. Almacena acá los archivos estáticos de tu aplicación. Coloca aquí los archivos html, javascript y css. 
**Capa de Servicios**: Esta capa se encarga de procesar la información que llega mediante peticiones del cliente y dar respuesta al mismo, consiste en las siguientes clases: 

>Controller.php:Es el controlador que se encarga de procesar las peticiones del usuario, requiriendo CubeController.php para realizar operaciones y retornar los datos: 
 

>CubeController.php: que contiente los metodos que ejecutan las operaciones que solucionan el problema 
 
**Capa de persistencia**:Almacena y recupera los datos de la aplicación. En el proyecto no existe ninguna base datos que almacena todos los datos, sin embargo la clase contenida en CubeController.php realiza el trabajo de almacenar y manejar los datos la matriz . 
