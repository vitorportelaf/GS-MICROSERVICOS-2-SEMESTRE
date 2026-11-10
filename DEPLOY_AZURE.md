# Implantação no Azure: Azure SQL + Azure Container Apps

A API roda como container no **Azure Container Apps**, usando a imagem publicada no Docker Hub, e o banco é um
**Azure SQL Database** (Microsoft SQL Server gerenciado).

> Dá para rodar todos os comandos no **Azure Cloud Shell** (ícone `>_` no topo do portal), que já vem com o
> `az` instalado e logado. Troque `[rm]` pelo RM de um integrante e escolha uma senha forte.
> **Nunca** coloque a senha no GitHub, no README ou nas respostas da API.

## 1. Variáveis

### Bash (Cloud Shell, Linux, macOS)

```bash
export RESOURCE_GROUP=rg-[rm]-hora-certa
export LOCATION=brazilsouth
export SQL_SERVER=[rm]-sql-hora-certa
export CONTAINERAPPS_ENVIRONMENT=[rm]-env-hora-certa
export APP_CONTAINER=[rm]-hora-certa

export DB_SERVER_URL=[rm]-sql-hora-certa.database.windows.net
export DB_SERVER_PORT=1433
export DB_SCHEMA=horacerta
export DB_USER=horacertaadmin
export DB_PWD="<senha-forte>"

export SPRING_PROFILES_ACTIVE=prd
export DOCKER_IMAGE=docker.io/<usuario-dockerhub>/hora-certa:latest
```

### PowerShell (Windows)

```powershell
$env:RESOURCE_GROUP="rg-[rm]-hora-certa"
$env:LOCATION="brazilsouth"
$env:SQL_SERVER="[rm]-sql-hora-certa"
$env:CONTAINERAPPS_ENVIRONMENT="[rm]-env-hora-certa"
$env:APP_CONTAINER="[rm]-hora-certa"

$env:DB_SERVER_URL="[rm]-sql-hora-certa.database.windows.net"
$env:DB_SERVER_PORT="1433"
$env:DB_SCHEMA="horacerta"
$env:DB_USER="horacertaadmin"
$env:DB_PWD="<senha-forte>"

$env:SPRING_PROFILES_ACTIVE="prd"
$env:DOCKER_IMAGE="docker.io/<usuario-dockerhub>/hora-certa:latest"
```

> Os comandos abaixo estão em Bash. No PowerShell, troque `$VARIAVEL` por `$env:VARIAVEL` e a quebra de linha
> `\` por `` ` ``.

> Se a assinatura de estudante não permitir `brazilsouth`, use `eastus2`.

## 2. Preparar a assinatura (uma única vez)

```bash
az extension add --name containerapp --upgrade
az provider register --namespace Microsoft.App
az provider register --namespace Microsoft.OperationalInsights
```

## 3. Criar o Resource Group

```bash
az group create \
  --name $RESOURCE_GROUP \
  --location $LOCATION
```

## 4. Criar o SQL Server

```bash
az sql server create \
  --name $SQL_SERVER \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION \
  --admin-user $DB_USER \
  --admin-password "$DB_PWD"
```

## 5. Criar o banco (SQL Database)

```bash
az sql db create \
  --name $DB_SCHEMA \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER \
  --edition GeneralPurpose \
  --family Gen5 \
  --capacity 2 \
  --compute-model Serverless \
  --use-free-limit \
  --free-limit-exhaustion-behavior AutoPause
```

> Se a assinatura não aceitar `--use-free-limit`, remova as duas últimas opções.

## 6. Liberar o acesso ao banco (firewall)

Permite que serviços do Azure (o Container App) acessem o banco:

```bash
az sql server firewall-rule create \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER \
  --name PermitirServicosAzure \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0
```

Opcional: para consultar o banco do seu computador (ex.: Azure Data Studio ou VS Code), libere o seu IP:

```bash
az sql server firewall-rule create \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER \
  --name MeuComputador \
  --start-ip-address <seu-ip> \
  --end-ip-address <seu-ip>
```

## 7. Conferir o endereço do SQL Server

```bash
az sql server show \
  --resource-group $RESOURCE_GROUP \
  --name $SQL_SERVER \
  --query fullyQualifiedDomainName \
  --output tsv
```

Deve ser igual ao `DB_SERVER_URL`.

## 8. Criar o Container Apps Environment

```bash
az containerapp env create \
  --name $CONTAINERAPPS_ENVIRONMENT \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION
```

## 9. Criar o Container App (a API)

A senha do banco vai como **secret** do Container App, e não como variável aberta:

```bash
az containerapp create \
  --name $APP_CONTAINER \
  --resource-group $RESOURCE_GROUP \
  --environment $CONTAINERAPPS_ENVIRONMENT \
  --image $DOCKER_IMAGE \
  --ingress external \
  --target-port 8080 \
  --min-replicas 1 \
  --max-replicas 1 \
  --secrets "db-pwd=$DB_PWD" \
  --env-vars \
    DB_SERVER_URL="$DB_SERVER_URL" \
    DB_SERVER_PORT="$DB_SERVER_PORT" \
    DB_SCHEMA="$DB_SCHEMA" \
    DB_USER="$DB_USER" \
    DB_PWD="secretref:db-pwd" \
    SPRING_PROFILES_ACTIVE="$SPRING_PROFILES_ACTIVE"
```

## 10. Obter a URL da API

```bash
az containerapp show \
  --name $APP_CONTAINER \
  --resource-group $RESOURCE_GROUP \
  --query properties.configuration.ingress.fqdn \
  --output tsv
```

Abra `https://<fqdn>`: a rota raiz mostra o Swagger. Essa é a **URL da API no Azure** para o README e o TXT de
entrega. A primeira requisição pode demorar alguns segundos, porque o banco serverless "acorda".

## 11. Verificar o status

```bash
az containerapp show \
  --name $APP_CONTAINER \
  --resource-group $RESOURCE_GROUP \
  --query properties.runningStatus \
  --output tsv
```

## 12. Ver os logs (se algo falhar)

```bash
az containerapp logs show \
  --name $APP_CONTAINER \
  --resource-group $RESOURCE_GROUP \
  --type console \
  --tail 50
```

## 13. Atualizar para a imagem nova

Depois que o GitHub Actions publicar uma imagem nova no Docker Hub:

```bash
az containerapp update \
  --name $APP_CONTAINER \
  --resource-group $RESOURCE_GROUP \
  --image $DOCKER_IMAGE \
  --revision-suffix r$(date +%Y%m%d%H%M%S)
```

## 14. Estrutura dos recursos

```text
Azure
│
└── Resource Group: rg-[rm]-hora-certa
    │
    ├── SQL Server: [rm]-sql-hora-certa
    │   └── Database: horacerta
    │
    └── Container Apps Environment: [rm]-env-hora-certa
        └── Container App: [rm]-hora-certa
            └── Imagem: <usuario-dockerhub>/hora-certa:latest
```

## 15. Fluxo da aplicação

```text
Internet (Swagger / Postman)
   │  https://<fqdn>/api/v1/...
   ▼
Azure Container App  ([rm]-hora-certa)
   │  [rm]-sql-hora-certa.database.windows.net:1433
   ▼
Azure SQL Database  (horacerta)
```

## 16. Depois da avaliação

Para não gastar crédito, apague tudo **somente depois que a nota sair**:

```bash
az group delete --name $RESOURCE_GROUP --yes --no-wait
```
