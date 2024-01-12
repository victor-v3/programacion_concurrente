#!/usr/bin/env python3

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


def leer_archivo(nombre_archivo: str):
    try:
        with open(nombre_archivo, "rt") as archivo:
            contenido = archivo.read()
        return contenido
    except FileNotFoundError:
        print(f"El archivo '{nombre_archivo}' no se encontró.")
        return None
    except Exception as e:
        print(f"Ocurrió un error al leer el archivo: {e}")
        return None


def generar_archivo_log(
    nombre_archivo: str,
    num_lineas: int,
    word_list: list,
    id_app: str = "wallet-rest-api",
    delay: float = 0.0,
):
    """
    Función para generar un archivo de tipo log de prueba.
    Se generan aleatoriamente líneas de mensajes con los terminos de la lista.
    nombre_archivo: str, nombre del archivo
    num_lineas: int, número de líneas de log que tendrá el archivo
    word_list: list, lista con las palabras claves a introducir en la generación de mensajes
    id_app: str, nombre de una supuesta aplicación que generase el log. Por defecto, wallet-rest-api
    delay: float, tiempo de retardo en la generación de mensajes. Por defecto, cero
    """
    with open(nombre_archivo, "w") as archivo:
        for i in range(num_lineas):
            timestamp = time.strftime("%Y-%m-%d %H:%M:%S")
            nivel = random.choice(word_list)

            if nivel == "[INFO]":
                linea = "mensaje de información, línea número " + str(i + 1)
            elif nivel == "[WARN]":
                linea = "mensaje de aviso, línea número " + str(i + 1)
            elif nivel == "[SEVERE]":
                linea = "mensaje de alarma, línea número " + str(i + 1)
            else:
                linea = "línea número " + str(i + 1)

            archivo.write(f"{id_app} {timestamp} {nivel} {linea}\n")
            # Se introduce un retardo de x milisegundos para modificar el timestamp
            time.sleep(delay)
    print(f"Se ha generado el archivo {nombre_archivo} con {num_lineas} líneas.")


def main(nombre_archivo: str, num_lineas: int, num_repeticion: int):
    # Generación de archivo log de prueba
    generar_archivo_log(
        nombre_archivo,
        num_lineas,
        ["[INFO]", "[WARN]", "[SEVERE]", "[MSG]", "[RECORD]"],
    )

    documents = []
    for num_repeticion in range(num_repeticion):
        documents.append(leer_archivo(nombre_archivo))
        if documents == None:
            return

    with Pool() as pool:
        mapped_results = pool.map(map_function, documents)

    flattened_results = [item for sublist in mapped_results for item in sublist]

    reduced_results = {}
    for item in flattened_results:
        # En el recorrido de item, se chequea si es alguna de las palabras buscadas
        if item[0] == "INFO" or item[0] == "WARN" or item[0] == "SEVERE":
            reduced_results[item[0]] = reduced_results.get(item[0], []) + [item[1]]

    final_results = list(map(reduce_function, reduced_results.items()))
    for clave, valor in final_results:
        print(clave, "\t", valor)
    # print(final_results)


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("nombre_archivo", help="El nombre del archivo")
    parser.add_argument(
        "num_lineas", type=int, help="Número de líneas del archivo de log"
    )
    parser.add_argument(
        "num_repeticion", type=int, help="Numero de veces que se repite el archivo tipo"
    )
    args = parser.parse_args()
    main(args.nombre_archivo, args.num_lineas, args.num_repeticion)
