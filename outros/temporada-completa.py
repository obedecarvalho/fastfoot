#!/usr/bin/python3
import requests
import time
import sys

url = 'http://localhost:8081/proximaSemana'
urlNovaTemporada = 'http://localhost:8081/novaTemporada'

numero = 0

if len(sys.argv) == 4:
    novaTemporada = (sys.argv[1] == '1')
    if sys.argv[2] == '1':
    	rodadaFinal = int(sys.argv[3])
    	numTemporada = 1
    elif sys.argv[2] == '2':
    	numTemporada = int(sys.argv[3])
    	rodadaFinal = 25
elif len(sys.argv) == 1:#equivalente a 'temporada-completa 1 1 25' ou 'temporada-completa 1 2 1'
    novaTemporada = True
    rodadaFinal = 25
    numTemporada = 1
else:
	print('Comando: temporada-completa <nova_temporada> <modo> <n>')
	print('\t<nova_temporada> Indica se deve requisitar criação de nova temporada. Valores [0, 1] ')
	print('\t<modo> Valores [1, 2]:\n\t1: Executa ate <n> rodadas\n\t2:executa ate <n> temporadas. ')
	exit()

for i in range(1, numTemporada+1):

	print('Temporada: ' + str(i))

	if novaTemporada:
		r = requests.get(urlNovaTemporada)

		if r.status_code == 200:
			print('Nova Temporada: ' + str(r.json()['ano']))
		else:
			print('Error (Criando Temporada): ' + str(r.status_code))
			exit()
	
	novaTemporada = True #Tratar caso de mais de uma temporada
	numero = 0

	while numero < rodadaFinal:
		time.sleep(0.5)
		r = requests.get(url)
		if r.status_code == 200:
			numero = int(r.json()['numero'])
			print(numero)
		else:
			print('Error (Executando Rodadas): ' + str(r.status_code))
			exit()

#print(r.status_code)
#print(r.content)
#print(r.json())
