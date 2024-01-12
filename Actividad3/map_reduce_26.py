#!/usr/bin/env python

import argparse
import random
import re
import time
from multiprocessing import Pool


def map_function(document):
    words = re.findall(r"\b\w+\b", document)
    word_count = {}
    for word in words:
        word_count[word] = word_count.get(word, 0) + 1
    return list(word_count.items())


def reduce_function(item):
    word, counts = item
    return (word, sum(counts))


def leer_archivo(nombre_archivo):
    try:
        with open(nombre_archivo, "rt") as archivo:
            contenido = archivo.read()
        return contenido
    except FileNotFoundError:
        print("El archivo '{}' no se encontro.".format(nombre_archivo))
        return None
    except Exception as e:
        print("Ocurrio un error al leer el archivo: {}".format(e))
        return None


def generar_archivo_log(
    nombre_archivo, num_lineas, word_list, id_app="wallet-rest-api", delay=0.0
):
    """
    Funcion para generar un archivo de tipo log de prueba.
    Se generan aleatoriamente lineas de mensajes con los terminos de la lista.
    nombre_archivo: str, nombre del archivo
    num_lineas: int, numero de lineas de log que tendra el archivo
    word_list: list, lista con las palabras claves a introducir en la generacion de mensajes
    id_app: str, nombre de una supuesta aplicacion que generase el log. Por defecto, wallet-rest-api
    delay: float, tiempo de retardo en la generacion de mensajes. Por defecto, cero
    """
    with open(nombre_archivo, "w") as archivo:
        for i in range(num_lineas):
            timestamp = time.strftime("%Y-%m-%d %H:%M:%S")
            nivel = random.choice(word_list)

            if nivel == "[INFO]":
                linea = "mensaje de informacion, linea numero " + str(i + 1)
            elif nivel == "[WARN]":
                linea = "mensaje de aviso, linea numero " + str(i + 1)
            elif nivel == "[SEVERE]":
                linea = "mensaje de alarma, linea numero " + str(i + 1)
            else:
                linea = "linea numero " + str(i + 1)

            archivo.write("{0} {1} {2} {3}\n".format(id_app, timestamp, nivel, linea))
            # Se introduce un retardo de x milisegundos para modificar el timestamp
            time.sleep(delay)
    print(
        "Se ha generado el archivo {0} con {1} lineas.".format(
            nombre_archivo, num_lineas
        )
    )


def main(nombre_archivo, num_lineas, num_repeticion):
    # Generacion de archivo log de prueba
    generar_archivo_log(
        nombre_archivo,
        num_lineas,
        ["[INFO]", "[WARN]", "[SEVERE]", "[MS]", "[RECORD]"],
    )

    documents = []
    for num_repeticion in range(num_repeticion):
        documents.append(leer_archivo(nombre_archivo))
        if documents == None:
            return

    # with Pool() as pool:
    # En las versiones de Python 2.x,los objeto multiprocessing.Pool, no se pueden usar
    # en un bloque with, ya que no tienen implementado el metodo __exit__
    # Para adaptar el codigo a Python 3.x, hay que crear y cerrar el pool de procesos
    # manualmente
    pool = Pool()
    mapped_results = pool.map(map_function, documents)
    pool.close()
    pool.join()

    flattened_results = [item for sublist in mapped_results for item in sublist]

    reduced_results = {}
    for item in flattened_results:
        # En el recorrido de item, se chequea si es alguna de las palabras buscadas
        if item[0] == "INFO" or item[0] == "WARN" or item[0] == "SEVERE":
            reduced_results[item[0]] = reduced_results.get(item[0], []) + [item[1]]

    final_results = list(map(reduce_function, reduced_results.items()))
    for clave, valor in final_results:
        print("{0} \t {1}".format(clave, valor))
    # print(final_results)


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("nombre_archivo", help="El nombre del archivo")
    parser.add_argument(
        "num_lineas", type=int, help="Numero de lineas del archivo de log"
    )
    parser.add_argument(
        "num_repeticion", type=int, help="Numero de veces que se repite el archivo tipo"
    )
    args = parser.parse_args()
    main(args.nombre_archivo, args.num_lineas, args.num_repeticion)
