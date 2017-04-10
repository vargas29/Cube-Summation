CODE REFACTORING (20 puntos) 



## Código inicial

```
public function post_confirm(){
    $id = Input::get('service_id');
    $servicio = Service::find($id);
    //dd($Servicio);
    if ($servicio != NULL){
        if  ($servicio->status_id == '6'){
            return Response::json(array('error' => '2'));
        }
        if ($servicio->driver_id == NULL && $servicio->status_id == '1'){
            $servicio = Service::update($id, array(
                'driver_id' => Input::get('driver_id'),
                'status_id' => '2'
                //Up Carro
                //,'pwd' => md5(Input::get('pwd'))
            ));
            Driver::update(Input::get('driver_id'), array(
                'available' => '0'
            ));
            $driverTmp = Driver::find(Input::get('driver_id'));
            Service::update($id, array(
                'car_id'=>$driverTmp->car_id
                //Up Carro
                //,'pwd' => md5(Input::get('pwd'))
            ));
            //Notificar a usuario!!
            $pushMessage = 'Tu servicio ha sido confirmado!';
            /*$servicio = Service::find($id);
            $push = Push::make();
            if ($servicio->user->type=='1'){//iPhone
                $pushAns = $push->ios($servicio->user->uuid, $pushMessage);
            }else{
                $pushAns = $push->ios($servicio->user->uuid, $pushMessage);
            }*/
            $servicio = Service::find($id);
            $push = Push::make();
            if ($servicio->user->uuid == ''){
                return Response::json(array('error'=>'0'));
            }
            if($servicio->user->type == '1'){//iPhone
                $result = $push->ios($servicio->user->uuid, $pushMessage, 1, 'honk.wav',
                                        'Open', array('service_id'=>$servicio->id));
            } else{
                $result = $push->android2($servicio->user->uuid, $pushMessage, 1, 'default',
                                        'Open', array('service_id'=>$servicio->id));
            }
            return Response::json(array('error'=>'0'));
        }else{
            return Response::json(array('error'=>'1'));
        }
    }else{
        return Response::json(array('error'=>'3'));
    }
}

```
## Código refactorizado

```
public function post_confirm() {
    $servicio_id = Input::get('service_id');
    $servicio = Service::find($servicio_id);
    if ($servicio != NULL) {
        if ($servicio->status_id == '6') {
            return Response::json(array('error' => '2'));
        }
        if ($servicio->driver_id == NULL && $servicio->status_id == '1') {
            $driver_id = Input::get('driver_id');
            $driverTmp = Driver::find($driver_id);
            $servicio = Service::update($servicio_id, array(
                'driver_id' => $driver_id,
                'status_id' => '2',
                'car_id' => $driverTmp->car_id
                //Up Carro
                //,'pwd' => md5(Input::get('pwd'))
            ));
            Driver::update($driver_id, array(
                'available' => '0'
            ));
            if ($servicio->user->uuid != '') {
                //Notificar a usuario!!
                $pushMessage = 'Tu servicio ha sido confirmado!';
                $push = Push::make();
                if ($servicio->user->type == '1') {//iPhone
                    $push->ios($servicio->user->uuid, $pushMessage, 1, 'honk.wav',
                        'Open', array('service_id' => $servicio->id));
                } else {
                    $push->android2($servicio->user->uuid, $pushMessage, 1, 'default',
                        'Open', array('service_id' => $servicio->id));
                }
            }
            return Response::json(array('error' => '0'));
        } else {
            return Response::json(array('error' => '1'));
        }
    } else {
        return Response::json(array('error' => '3'));
    }
}

```

El siguiente código muestra el método de un controlador que: 
1. Recibe dos parámetros por POST: El id de un servicio, el id de un conductor 
2. Cambia el estado de varias entidades en la base de datos basado en la lógica del negocio. 
3. Envía notificaciones y retorna una respuesta. 
Refactorice y envíe el código y en un documento explique: 
1. Las malas prácticas de programación que en su criterio son evidenciadas en el código 
2. Cómo su refactorización supera las malas prácticas de programación 
 
 
 
Yo describiría como no está bien diseñado, y ciertamente no orientado a objetos. Para un programa sencillo es esto, que en realidad no importa. No hay nada malo con un rápido y sucio sencillo programa. Pero si nos imaginamos esto como un fragmento de un sistema más complejo, entonces tengo algunos problemas reales con este programa. el metodo POST_CONFIRM demasiado. Muchas de las cosas que realmente debe ser realizado por las otras clases. 
Se observa que el metodo "Post_confirm" , se le asigna muchas a responsabilidades tan importante, aun solo método es el encargado de cambiar  el estado de la lógica de negocio y   retornar una respuesta.  
No es recomendable que dentro de una función en este caso  POST_CONFIRM , se encuentra muchas retorno de respuesta  lo que puede genera confusión a la hora de documentarlo y da impresión de un código sucio :Se repite la línea que retorna error 0 cuando se valida que si el UUID del usuario es nulo y cuando se pasa la validación de las notificaciones. Mucho 
 
No es acto para prueba unitarias ,Se obtiene driver_id en diferentes partes en vez de obtenerlo una vez y guardarlo en una variable pueden utilizarse como parámetro del método. La falta de acoplamiento en las entidades. El estado del servicio depende de La primera que guarda el driver_id y el status_id, y la segunda guarda el car_id. Esto se puede condensar en una única operación. 
Es un código desordenado y sucio: por la falta de comentarios fundamentales y el uso incorrecto de algunos patrones de diseño del código 
La carencia de documentación a la hora de entender el código , hace que se vuelva complejo para  una futura modificación. 
La falta de uso de un patrón de diseño , es mala practica porque no permite entender los estados que toman las entidades  
 
Las correcciones realizadas son: 

Bajo acoplamiento: Input::get('driver_id') , se guarda en una variable y es llamado solo cuando es necesario .Así solo se cambia el nombre solo una vez .Se actualiza el servicio una sola vez, incluyendo el car_id que se hacía en la segunda operación. 
Retorno y validaciones: Se elimina  la línea que retorna cuando el UUID del usuario es nulo. En vez de esto, se valida que cuando no es nulo se envíe notificaciones, de lo contrario se salta dicho paso e igualmente retorna error 0. 
 
Se omite guardar la respuesta del método de notificar ya que no es usada. 
 
 
 
### PREGUNTAS (10 puntos) 
  
Responde y envía en un documento las siguientes preguntas: 
**1. ¿En qué consiste el principio de responsabilidad única? ¿Cuál es su propósito?**

 
El principio de Responsabilidad Única nos viene a decir que un objeto debe realizar una única cosa. Es muy habitual, si no prestamos atención a esto, que acabemos teniendo clases que tienen varias responsabilidades lógicas a la vez , lo que quiere decir esta afirmación es que un objeto debe tener un conjunto coherente de comportamientos entorno a una única responsabilidad de tal forma que si esta responsabilidad cambia implica cambiar la definición del objeto. El principio De responsabilidad unica esta diseñado para cumplir dos características principales  
La alta cohesión tiene que ver con la forma en la que agrupamos unidades de software en una unidad mayor. Por otro lado, el acoplamiento mide el grado de relación de un módulo con el resto. Mientras el grado de acoplamiento sea menor, esto favorece la testeabilidad, diseño, mantenibilidad, etc. 
El Principio de Responsabilidad Única es una mecanismo  de diseño preservar y garantizar  nuestro código frente a cambios, ya que implica que sólo debería haber . En ele ejercicio del desarrollo de software , muchas veces nos encontraremos con que estos límites tendrán más que ver con lo que realmente necesitemos que con complicadas técnicas de disección. 
 
**2.¿Qué características tiene según tu opinión “buen” código o código limpio?**
el código de revela su claro funcionamiento y si no debe darnos una idea para luego estudiarlo a detalle. El codigo su objetivo no es solo para ser ejcutado si no tambien leído e interpretado. No importa en que lenguaje se encuentro escrito o la orientación que reciba, si expresa claramente su proposito se puede llmar codigo limpio. 
  
Las principales caraccteristica que debe cumplir un codigo limpio es 
1. Codigo limpio es enfocado:debe cumplir el principio de rsponabilidad unica es decir una clases debe hacer un sola cosa 
2.no redundante:La modificacion de un elemento , no afecta oros elmentos que no tenga relacion logica. 
3.Pocas dependencia:  entre mas dependiente sea mas facilsera su mantenimiento 
4.Pruebas unitarias: por medio de ellas nos  garantiza que si este cumpliendo su objetivo 
5.pocas lineas de codigo.entre mas dividamos el codigo  y menos lineas tenga sra mas facil leerlo y entenderlo. 
6. Debe basarse en patrón SOLID 
 
