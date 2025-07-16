# Usa a imagem oficial do OpenJDK 21 slim como base
FROM openjdk:21-slim

# Instala o Maven e o findutils.
# O 'rm -rf /var/lib/apt/lists/*' é para limpar o cache do apt e reduzir o tamanho da imagem.
RUN echo "--- Atualizando pacotes e instalando Maven e findutils ---" && \
    apt-get update && \
    apt-get install -y maven findutils && \
    rm -rf /var/lib/apt/lists/* && \
    echo "--- Instalação de dependências do sistema concluída ---"

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app
# Imprime o diretório de trabalho atual
RUN echo "--- Diretório de trabalho definido para: $(pwd) ---"

# COLOQUE ESTE DOCKERFILE NA RAIZ DO SEU PROJETO (votacao-distribuida/)
# para que os comandos COPY abaixo funcionem corretamente.

# Copia o arquivo pom.xml para o diretório de trabalho no contêiner.
# Isso permite que o Docker use o cache para as dependências se o pom.xml não mudar.
COPY pom.xml .

# Baixa todas as dependências do Maven para o repositório local do contêiner.
RUN echo "--- Baixando dependências do Maven (go-offline) ---" && \
    mvn dependency:go-offline && \
    echo "--- Dependências do Maven baixadas ---"

# Copia o diretório 'src' (contendo seu código-fonte) para o diretório de trabalho no contêiner.
COPY src ./src

# Compila a aplicação e copia as dependências para o diretório 'target/lib'.
# ESTA É A LINHA QUE COMPILA SEU CÓDIGO JAVA E GERENCIA AS DEPENDÊNCIAS.
# A LINHA 'find . -name "*.java" ... javac' DEVE SER REMOVIDA.
RUN echo "--- Compilando a aplicação e copiando dependências ---" && \
    mvn clean package -DskipTests dependency:copy-dependencies && \
    echo "--- Compilação e cópia de dependências concluídas ---"

# Define a classe principal da sua aplicação
ENV MAIN_CLASS="com.votingsystem.rmi.server.VotingServerMain"

# Comando para executar a aplicação.
# O classpath agora inclui 'target/classes' (onde o Maven coloca suas classes compiladas)
# e 'target/lib/*' (onde as dependências são copiadas).
CMD ["java", "-cp", "target/classes:target/lib/*", "${MAIN_CLASS}"]
