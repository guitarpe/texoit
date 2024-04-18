package br.texoit.application.enuns;

public enum MessageSystem {
    ERROR_INVALID_FILE("Arquivo de formato inválido"),
    ERROR_READ_CSV_FILE("Erro ao ler o arquivo CSV"),
    ERROR_CONVERT_FILE("Erro ao ler o arquivo de entrada"),
    ERROR_FILE_CSV_EMPTY("Arquivo está vazio"),
    ERROR_FILE_CSV_NULL("Arquivo está null"),
    ERROR_FUNCTIONAL("Erro ao realizar tarefa"),
    ERROR_EMPTY_RESULTS("Não foram encontrados registros"),
    SUCCESS("Intervalo de prêmios recuperado com sucesso"),
    SUCCESS_REGISTER_MOVIES("Filmes registrados com sucesso"),
    SUCCESS_RETRIEVE_MOVIES("Filme recuperado com sucesso"),
    SUCCESS_UPDATED_MOVIES("Filme atualizado com sucesso"),
    SUCCESS_DELETED_MOVIES("Filme deletado com sucesso");

    private final String value;

    MessageSystem(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageSystem fromValue(String v) {
        for (MessageSystem c: MessageSystem.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
