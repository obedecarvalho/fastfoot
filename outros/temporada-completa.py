#!/usr/bin/python3
import requests
import time

url = 'http://localhost:8081/proximaSemana'
urlNovaTemporada = 'http://localhost:8081/novaTemporada'

numero = 0;
novaTemporada = False;

rodadaFinal = 24

while numero < rodadaFinal:
	time.sleep(0.5)
	r = requests.get(url)
	if r.status_code == 200:
		numero = int(r.json()['numero'])
		print(numero)
	else:
		print('Error (Executando Rodadas): ' + str(r.status_code))
		exit()

if novaTemporada:
	r = requests.get(urlNovaTemporada)

	if r.status_code == 200:
		print('Nova Temporada: ')
		print(r.json()['ano'])
	else:
		print('Error (Criando Temporada): ' + str(r.status_code))
		exit()


#print(r.status_code)
#print(r.content)
#print(r.json())
