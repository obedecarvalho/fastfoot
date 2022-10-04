#!/usr/bin/python3
import requests
import time
import sys

def printAjuda():
	print('Comando: temporada-outras-opcoes.py <opcao>')
	print('\t<opcao>:')
	print('\t\t1 - MudancaClubeNivel')
	print('\t\t2 - CalcularTrajetoriaForca')
	print('\t\t3 - GerarTransferencias')

urlMudancaClubeNivel = 'http://localhost:8081/gerarMudancaClubeNivel'
urlCalcularTrajetoriaForca = 'http://localhost:8081/calcularTrajetoriaForcaClube'
urlGerarTransferencias = 'http://localhost:8081/gerarTransferencias'
opcao = 0

if len(sys.argv) == 2:
	opcao = int(sys.argv[1])
	if not 1 <= opcao <= 3:
		print('Error: <opcao> inválida')
		printAjuda()
		exit()
else:
	printAjuda()
	exit()

start_time = time.time()
if opcao == 1:
	r = requests.get(urlMudancaClubeNivel)
elif opcao == 2:
	r = requests.get(urlCalcularTrajetoriaForca)
elif opcao == 3:
	r = requests.get(urlGerarTransferencias)
finish_time = time.time()

if r.status_code == 200:
	print('Sucesso: ' + str(r.json()))
else:
	print('Error: ' + str(r.status_code))
	exit()

