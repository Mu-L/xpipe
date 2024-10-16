# Identidades VM SSH

Se o usuário convidado da VM exigir autenticação baseada em chave para SSH, você pode ativar isso aqui.

Observe que é assumido que sua VM não está exposta ao público, portanto, o sistema host da VM é usado como um gateway SSH.
Como resultado, qualquer opção de identidade é especificada em relação ao sistema host da VM e não à sua máquina local.
Qualquer chave que especifiques aqui é interpretada como um ficheiro no anfitrião da VM.
Se estiveres a utilizar qualquer agente, espera-se que o agente esteja a ser executado no sistema anfitrião da VM e não na tua máquina local.

### Não tens nada

Se selecionado, o XPipe não fornecerá quaisquer identidades. Isto também desactiva quaisquer fontes externas como agentes.

### Ficheiro de identidade

Podes também especificar um ficheiro de identidade com uma frase-chave opcional.
Esta opção é o equivalente a `ssh -i <file>`.

Nota que esta deve ser a chave *privada*, não a pública.
Se misturares isso, o ssh apenas te dará mensagens de erro crípticas.

### Agente SSH

Caso as tuas identidades estejam armazenadas no SSH-Agent, o executável ssh pode usá-las se o agente for iniciado.
O XPipe iniciará automaticamente o processo do agente se ele ainda não estiver em execução.

Se não tiveres o agente configurado no sistema anfitrião da VM, recomenda-se que actives o encaminhamento do agente SSH para a ligação SSH original ao anfitrião da VM.
Podes fazer isso criando uma conexão SSH personalizada com a opção `ForwardAgent` ativada.

### Agente GPG

Se as tuas identidades estão armazenadas, por exemplo, num smartcard, podes optar por fornecê-las ao cliente SSH através do `gpg-agent`.
Esta opção habilitará automaticamente o suporte SSH do agente se ainda não estiver habilitado e reiniciará o daemon do agente GPG com as configurações corretas.

### Yubikey PIV

Se as tuas identidades estão armazenadas com a função de cartão inteligente PIV do Yubikey, podes recuperá-las
podes recuperá-las com a biblioteca YKCS11 do Yubico, que vem junto com a ferramenta Yubico PIV.

Nota que necessita de uma versão actualizada do OpenSSH para poder utilizar esta função.

### Biblioteca PKCS#11 personalizada

Isso instruirá o cliente OpenSSH a carregar o arquivo de biblioteca compartilhada especificado, que lidará com a autenticação.

Nota que precisas de uma versão actualizada do OpenSSH para usar esta funcionalidade.

### Pageant (Windows)

Caso estejas a usar o pageant no Windows, o XPipe irá verificar se o pageant está a ser executado primeiro.
Devido à natureza do pageant, é da tua responsabilidade tê-lo
a responsabilidade de o ter em execução, uma vez que tens de especificar manualmente todas as chaves que gostarias de adicionar de cada vez.
Se estiver em execução, o XPipe passará o pipe nomeado apropriado via
`-oIdentityAgent=...` para o ssh, não tens de incluir quaisquer ficheiros de configuração personalizados.

### Pageant (Linux & macOS)

Caso as tuas identidades estejam armazenadas no agente pageant, o executável ssh pode usá-las se o agente for iniciado.
O XPipe iniciará automaticamente o processo do agente se ele ainda não estiver em execução.

### Outra fonte externa

Esta opção permitirá que qualquer provedor de identidade externo em execução forneça suas chaves para o cliente SSH. Deves utilizar esta opção se estiveres a utilizar qualquer outro agente ou gestor de palavras-passe para gerir as tuas chaves SSH.
