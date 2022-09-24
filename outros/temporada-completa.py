#!/usr/bin/python3
import requests
import time
import sys

url = 'http://localhost:8081/proximaSemana'
urlNovaTemporada = 'http://localhost:8081/novaTemporada'
urlTrans = 'http://localhost:8081/gerarTransferencias'

numero = 0

if len(sys.argv) == 5:
    novaTemporada = (sys.argv[1] != '0')
    transferencias = (sys.argv[4] != '0')
    if sys.argv[2] == '1':
    	rodadaFinal = int(sys.argv[3])
    	numTemporada = 1
    elif sys.argv[2] == '2':
    	numTemporada = int(sys.argv[3])
    	rodadaFinal = 25
elif len(sys.argv) == 1:#equivalente a 'temporada-completa 1 1 25 1' ou 'temporada-completa 1 2 1 1'
    novaTemporada = True
    rodadaFinal = 25
    numTemporada = 1
    transferencias = True
else:
	print('Comando: temporada-completa <nova_temporada> <modo> <n> <transferencias>')
	print('\t<nova_temporada> Indica se deve requisitar criação de nova temporada. Valores [0, 1] ')
	print('\t<modo> Valores [1, 2]:\n\t1: Executa ate <n> rodadas\n\t2:executa ate <n> temporadas. ')
	print('\t<transferencias> Indica se deve requisitar executar transferencias. Valores [0, 1] ')
	exit()

for i in range(1, numTemporada+1):

	print('Temporada: ' + str(i))

	if novaTemporada:
		start_time = time.time()
		r = requests.get(urlNovaTemporada)
		finish_time = time.time()

		if r.status_code == 200:
			print('Nova Temporada: ' + str(r.json()['ano']))
			print('Tempo:' + str(finish_time - start_time))
		else:
			print('Error (Criando Temporada): ' + str(r.status_code))
			exit()

		if transferencias:
			start_time = time.time()
			r = requests.get(urlTrans)
			finish_time = time.time()

			if r.status_code == 200:
				print('Transferencias Tempo:' + str(finish_time - start_time))
			else:
				print('Error (Transferencias): ' + str(r.status_code))
				exit()
	
	novaTemporada = True #Tratar caso de mais de uma temporada
	numero = 0

	while numero < rodadaFinal:
		time.sleep(0.5)
		start_time = time.time()
		r = requests.get(url)
		finish_time = time.time()
		if r.status_code == 200:
			numero = int(r.json()['numero'])
			print(str(numero) + '\t[t:' + str(finish_time - start_time) + ']')
		else:
			print('Error (Executando Rodadas): ' + str(r.status_code))
			exit()

#print(r.status_code)
#print(r.content)
#print(r.json())
