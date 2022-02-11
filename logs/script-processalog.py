BFACTOR_INDEX = 5
PLY_TIME_INDEX = 3
ISTERMINAL_INDEX = 9
TURNO_INDEX = 0


doc = open('LOG_Jogo da Velha 4_MCTS-10000-0_75.csv', 'r')
group_by_turn = {}
for line in doc:
    if 'TURNO;NOME_AGENTE;COR_PECA;PLY_TIME;MAX DEPTH; BFACTOR; TOTAL_NODES;MAX BRANCH;MAX BOARD;RESULTADO' in line:
        continue

    vet = line.split(';')
    if vet[TURNO_INDEX] not in group_by_turn:
        group_by_turn[vet[TURNO_INDEX]] = []
    group_by_turn[vet[TURNO_INDEX]].append(vet)

doc.close()

avg_bfactor = {}
avg_plytime = {}
terminal_states = {}
for key in group_by_turn:
    sum_avg_bfactor = 0
    sum_avg_plytime = 0
    element = group_by_turn[key]
    # calcula a média dos branching factors para cada turno
    for line in element:
        sum_avg_bfactor += float(line[BFACTOR_INDEX])
    sum_avg_bfactor/=len(group_by_turn[key])

    if key not in avg_bfactor:
        avg_bfactor[key] = []
    avg_bfactor[key] = [key, str(round(sum_avg_bfactor,4))]

    # contabiliza o número de fins de jogo por turno
    for line in element:
        if line[ISTERMINAL_INDEX] == "VITORIA" or line[ISTERMINAL_INDEX] == "EMPATE":
            if key not in terminal_states:
                terminal_states[key] = [key, 0]
            terminal_states[key][1] += 1
    
    # calcula a média do tempo de cada ply por turno
    for line in element:
        sum_avg_plytime += float(line[PLY_TIME_INDEX])
    sum_avg_plytime/=len(group_by_turn[key])
    if key not in avg_plytime:
        avg_plytime[key] = []
    avg_plytime[key] = [key, str(round(sum_avg_plytime,4))]


#GENERATE OUTPUT#
str_output = 'TURNO;AVG BFACTOR;;TURNO;PLY TIME;;TURNO;COUNT TERMINALS;\n'

aux_keys_terminal_states = [x for x in terminal_states]
index_keys_terminal_states = 0
for key in group_by_turn:
    
    tmp_format = str(avg_bfactor[key][1])
    str_output += avg_bfactor[key][0] + ';' + tmp_format + ';;' 
    
    tmp_format = str(avg_plytime[key][1])
    str_output += avg_plytime[key][0] + ';' + tmp_format + ';;' 
    
    if index_keys_terminal_states < len(aux_keys_terminal_states):
        for e in terminal_states[aux_keys_terminal_states[index_keys_terminal_states]]:
            str_output += str(e) + ';'
        index_keys_terminal_states+=1
    str_output+='\n'


print(str_output)
str_output = str_output.replace(".", ",")
print(str_output)


arquivo = open('resultado-MCTS-10000-0_75.csv', 'w')
arquivo.writelines(str_output)
arquivo.close()

