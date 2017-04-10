<?php

/*
 * 1) El mayor problema encontrado fue sobre complejidad ciclomática que era bastante elevada dificultando la
 * lectura del codigo asi como posible errore futuros por dicha razón, además variables mal declaradas y llamados a la
 * base de datos redundantes y problemas de estilo como mezclar comillas dobles con simple.
 *
 * 2) Para solucionar la complejidad ciclomática simplifique el codigo eliminando condicionales reduntantes, para reducir las llamadas a la base de datos
 * elimine las llamadas innecesarias y unifique otras, asi como validar desde el inicio el 'Driver', corrigi los estilos usando solo comilla simple, como
 * resultado quedo un codigo con mejor legibilidad y con menor lineas
 */
class testClass
{
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
}