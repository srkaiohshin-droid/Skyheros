# 🦸 Skyheros

**Uma plataforma modular de super-heróis para Minecraft Java Edition**, criada para desenvolver poderes, trajes, voo, combate, animações e progressão sem depender da arquitetura antiga do Fisk Heroes.

> **Estado atual: desenvolvimento experimental.** O projeto já possui uma base Forge, registros de trajes e poderes, comandos e sistemas iniciais de movimento. Nem tudo que aparece nos catálogos está completamente implementado ou validado dentro do jogo. Este README separa o que existe no código do que ainda precisa ser concluído e testado.

---

## 📌 Visão do projeto

A ideia do **Skyheros** é construir uma experiência de super-heróis expansível para Minecraft, com poderes que tenham comportamentos próprios, movimento convincente e espaço para adicionar novos personagens e estilos de jogo.

A meta não é apenas criar uma lista enorme de habilidades. Queremos que cada categoria tenha mecânicas, animações, efeitos visuais, limitações e interações próprias, mantendo o código organizado para que novos poderes possam ser adicionados sem reescrever o mod inteiro.

### Princípios de desenvolvimento

- **Modularidade:** separar trajes, poderes, habilidades, execução, rede, entidades e apresentação visual.
- **Poderes com comportamento real:** um poder cadastrado não deve ser considerado concluído só por existir no catálogo; ele precisa ter execução funcional e ser testado.
- **Movimentação responsiva:** voo e habilidades de movimento devem responder à direção que o jogador está olhando, com controle previsível.
- **Animações próprias:** trabalhar em voo pairado, decolagem, deslocamento, curvas e aterrissagem, evitando poses quebradas ou movimentos rígidos.
- **Expansão gradual:** começar com uma base funcional e ampliar os sistemas de forma verificável.
- **Independência:** o Skyheros é um projeto próprio para Forge 1.20.1. Referências visuais ou ideias de outros mods não significam que o código antigo do Fisk Heroes tenha sido portado.

---

## 🧱 Plataforma e dependências

| Item | Configuração |
|---|---|
| Jogo | Minecraft Java Edition 1.20.1 |
| Mod loader | Minecraft Forge 47.x |
| Java | Java 17 |
| ID do mod | `skyheros` |
| Versão atual declarada | `0.1.0` |
| Animação de entidades | GeckoLib 4.7.3 ou compatível com a faixa declarada |
| Linguagem | Java |

As versões exatas instaladas no ambiente de desenvolvimento podem ser diferentes das versões usadas por quem clonar o repositório. Consulte `build.gradle`, `gradle.properties` e `src/main/resources/META-INF/mods.toml` antes de preparar um ambiente.

---

## ✅ O que já existe no código

Esta lista indica estruturas encontradas no projeto, não uma garantia de que cada recurso esteja pronto para uso em uma partida normal.

### Base do mod
- [x] Estrutura de mod Forge com ID `skyheros`.
- [x] Classe de inicialização e registro dos catálogos.
- [x] Registro de trajes e poderes.
- [x] Canal de rede inicial para pacotes relacionados a habilidades de movimento.
- [x] Comandos de jogador para acionar poderes e habilidades.
- [x] Sugestões de catálogo para facilitar o preenchimento de IDs nos comandos.
- [x] Estrutura Gradle configurada para Java 17 e Forge 1.20.1.

### Trajes e heróis
- [x] Estrutura de definição e registro de trajes.
- [x] Primeiro traje registrado no catálogo: **Superman**.
- [ ] Sistema completo de vestir/remover traje e transformação validado de ponta a ponta.
- [ ] Modelos, texturas e variações visuais finais de todos os trajes.

O traje do Superman já aparece no catálogo com referências a superforça, voo, supervelocidade, resistência aprimorada, visão de calor e sopro congelante. Isso representa a definição inicial do traje; **não significa que todas essas habilidades estejam totalmente implementadas ou ligadas ao traje no jogo**.

### Catálogo de poderes
O código contém catálogos que geram poderes por combinação de temas e mecânicas, além de catálogos adicionais para poderes de estilo de quadrinhos, poderes especiais e combinações.

Entre os temas cadastrados estão:

- Fogo, gelo, eletricidade e água
- Energia cósmica, plasma, radiação e energia cinética
- Sombras, luz, natureza, vento e terra
- Gravidade, tempo e espaço
- Poderes psíquicos, magia e tecnologia

Entre os tipos de mecânica previstos nos catálogos estão projéteis, raios, explosões, ondas, auras, escudos, barreiras, impulsos de movimento, voo, saltos, velocidade, força, resistência, sentidos especiais, cura, drenagem, imobilização, atração, repulsão, invocação, construções e transformação.

O projeto também possui um catálogo de poderes avançados com temas como cósmico, fogo, gelo, tempestade, sombras, radiante, natureza, cinético e tecnologia.

**Importante:** IDs, nomes, parâmetros, cooldowns e custos cadastrados não comprovam que todas as habilidades tenham efeitos finais, balanceamento ou animações. O número de entradas do catálogo não deve ser confundido com o número de poderes prontos.

### Voo e animação
- [x] Comando/sistema inicial de ativação do voo já foi exercitado no ambiente de desenvolvimento.
- [x] Existe código de controle de movimento e comunicação de rede relacionado às habilidades de movimento.
- [x] Existem arquivos de modelo e animação do *Flight Rig* para experimentação com GeckoLib.
- [ ] A animação de voo aplicada diretamente ao modelo do jogador ainda precisa ser integrada e validada.
- [ ] Pairar com os braços alinhados, decolagem, voo para a direção da câmera, curvas e aterrissagem precisam de refinamento e testes.
- [ ] A transição entre voo parado e voo em movimento ainda precisa parecer natural.
- [ ] A animação experimental do Flight Rig não deve ser confundida com uma animação de jogador pronta.

**Problema conhecido:** o voo pode ser ativado, mas a animação correspondente no jogador ainda não está funcionando como desejado. A prioridade é fazer o personagem apontar e deslocar-se na direção observada pelo jogador, com pose estável e transições suaves.

---

## 🧪 O que ainda não está confirmado como funcional

Os itens abaixo devem ser tratados como **em desenvolvimento ou pendentes de validação**, mesmo quando já existe uma classe, registro ou arquivo para eles.

- **Os 900 poderes:** há validação de quantidade no inicializador, mas isso não comprova que os 900 efeitos funcionem individualmente.
- **As habilidades do catálogo:** cada uma precisa ser testada quanto a alvo, alcance, dano, duração, custo, recarga, efeitos colaterais e mensagens de falha.
- **As 1.000 habilidades do catálogo de abilities:** há comandos para consultar e auditar o catálogo, mas o total cadastrado não significa que todas estejam prontas.
- **Progressão e energia:** existem referências a verificações de energia, registro de uso e recargas no fluxo de poderes; o sistema completo precisa ser testado e equilibrado em jogo.
- **Combos:** há estrutura de catálogo, mas cada combinação precisa ter execução real, requisitos claros e resultado verificável.
- **Transformação e gerenciamento de trajes:** ainda é necessário validar o fluxo completo dentro do jogo.
- **Animações do jogador:** o Flight Rig é uma base experimental; sua presença no projeto não garante que as animações sejam aplicadas ao jogador.
- **Multiplayer:** a existência de pacotes de rede não substitui testes em servidor dedicado, com dois ou mais jogadores.
- **Balanceamento:** valores de energia, cooldown e magnitude são parâmetros iniciais e podem mudar conforme os testes.
- **Compatibilidade com outros mods:** não deve ser considerada garantida até haver testes específicos.

A lista será atualizada conforme os sistemas forem testados, corrigidos e confirmados.

---

## 🎮 Comandos de desenvolvimento

Os comandos abaixo estão definidos no código e destinam-se a testes. IDs exatos dependem dos catálogos registrados na versão em uso.

| Comando | Finalidade |
|---|---|
| `/skyheros power <id>` | Tentar executar um poder pelo ID, nome ou ID curto aceito pelo catálogo. |
| `/skyheros ability <id>` | Ativar uma habilidade registrada. |
| `/skyheros list` | Mostrar parte do catálogo de habilidades. |
| `/skyheros catalog` | Exibir a auditoria de quantidade e executores de habilidades. |
| `/skyheros validate` | Conferir a contagem e a auditoria do catálogo de habilidades. |

Exemplos de uso:

```text
/skyheros power fire_projectile
/skyheros ability <id_da_habilidade>
/skyheros list
/skyheros catalog
/skyheros validate
```

Os exemplos são para demonstrar a sintaxe. Um ID pode não existir em determinada versão, e um poder cadastrado pode responder que ainda não tem implementação completa. Use as sugestões exibidas pelo próprio comando quando disponíveis.

---

## 🛠️ Preparar o ambiente de desenvolvimento

### Requisitos

- JDK 17
- Git
- Ambiente de desenvolvimento Java de sua preferência
- Conexão com a internet na primeira configuração do Gradle/Forge

### Clonar o repositório

```bash
git clone https://github.com/srkaiohshin-droid/Skyheros.git
cd Skyheros
```

### Compilar

No Windows, usando o wrapper incluído no projeto:

```powershell
.\gradlew.bat build
```

Para abrir o cliente de desenvolvimento, se a configuração local do Forge estiver preparada:

```powershell
.\gradlew.bat runClient
```

A primeira execução pode demorar enquanto o Gradle baixa dependências e prepara os artefatos do Minecraft/Forge. Se ocorrer uma falha, guarde o erro completo e os arquivos de log; não assuma que um build bem-sucedido significa que todas as funções estão funcionando em jogo.

### Instalar em uma instância de teste

Depois de uma compilação bem-sucedida, o JAR costuma ser gerado em `build/libs/`. Confira o nome e a data do arquivo gerado e instale-o **somente em uma instância de teste** com Minecraft 1.20.1, Forge compatível e GeckoLib na versão exigida. Não distribua um build experimental como versão estável.

---

## 🗺️ Roteiro de desenvolvimento

A ordem pode mudar conforme os testes e os problemas encontrados.

### Fase 1 — Base confiável
- [x] Estrutura Forge e inicialização do mod.
- [x] Registros iniciais de trajes, poderes e habilidades.
- [x] Comandos de teste e auditoria.
- [ ] Fazer uma compilação limpa e repetível a partir de um clone novo.
- [ ] Verificar o carregamento do JAR em uma instância limpa.

### Fase 2 — Voo e movimento
- [x] Ativação inicial do voo.
- [x] Controle inicial de movimento e pacote de rede.
- [ ] Direção do voo sincronizada com a direção da câmera.
- [ ] Animação estável de pairar, aceleração, deslocamento e aterrissagem.
- [ ] Transições suaves e pose natural dos braços.
- [ ] Testes de movimento em multiplayer.

### Fase 3 — Poderes fundamentais
- [ ] Superforça e resistência com efeitos mensuráveis.
- [ ] Supervelocidade com controle, colisões e efeitos próprios.
- [ ] Teletransporte com regras de destino e prevenção de situações inválidas.
- [ ] Poderes aquáticos e de combate com comportamento próprio.
- [ ] Custos, recargas, duração e feedback consistentes.

### Fase 4 — Trajes e identidade visual
- [ ] Transformação e gerenciamento de trajes.
- [ ] Modelos e texturas integrados ao jogo.
- [ ] Animações específicas por categoria de poder.
- [ ] Efeitos visuais e sonoros consistentes.

### Fase 5 — Progressão e expansão
- [ ] Progressão, domínio de poderes e desbloqueios.
- [ ] Combos com requisitos e resultados definidos.
- [ ] Auditoria automatizada para separar poderes implementados, parciais e pendentes.
- [ ] Testes de desempenho, estabilidade e compatibilidade.

---

## 🧭 Como vamos medir o progresso

No Skyheros, um recurso só deve passar para **funcional** depois de ser exercitado no jogo. A classificação usada neste projeto é:

- **Implementado no código:** existe uma classe, registro ou fluxo inicial.
- **Parcial:** funciona em alguma situação, mas falta integração, feedback, balanceamento ou tratamento de casos extremos.
- **Funcional em teste:** foi testado no jogo em condições definidas.
- **Validado:** passou por testes repetíveis e, quando aplicável, por teste em multiplayer.
- **Planejado:** ainda não existe implementação suficiente para afirmar que está funcionando.

Essa distinção é especialmente importante para os catálogos grandes: muitos IDs não significam automaticamente muitos poderes completos.

---

## 🤝 Contribuições e relatos de bugs

Relatos de bugs são úteis quando incluem:

1. Versão do Minecraft, Forge, GeckoLib e Skyheros.
2. Se o problema ocorreu em cliente individual ou servidor.
3. Passos para reproduzir.
4. Mensagem exibida no chat e trecho relevante do log/crash report.
5. ID do poder ou habilidade testada.

Antes de abrir uma solicitação, verifique se o problema já foi relatado. Para mudanças grandes, explique a proposta e como ela será testada.

---

## ⚠️ Aviso sobre o estado do projeto

O Skyheros é um projeto experimental em desenvolvimento ativo. Catálogos, IDs, APIs internas, parâmetros e recursos podem mudar. Não há garantia de estabilidade, balanceamento ou compatibilidade nesta fase.

O objetivo é evoluir de uma base modular para um mod de super-heróis completo, priorizando primeiro os sistemas fundamentais e sua qualidade, em vez de declarar concluídos recursos que ainda não foram verificados.

---

## 📄 Licença e propriedade intelectual

O arquivo `mods.toml` atualmente declara **All Rights Reserved**. Até que uma licença explícita seja adicionada e publicada no repositório, não presuma que o código possa ser redistribuído, modificado ou usado em outros projetos livremente.

Skyheros é um projeto independente. Nomes de personagens, conceitos e elementos de universos de super-heróis podem pertencer aos respectivos titulares. Este repositório não deve ser interpretado como afiliado, aprovado ou patrocinado por esses titulares.
