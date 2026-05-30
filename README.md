# 💊 Hora Certa - Gestão de Saúde na Palma da Mão

O **Hora Certa** é um aplicativo mobile desenvolvido em **Java no Android Studio** focado em **Saúde Digital**. Ele atua como um assistente pessoal para o gerenciamento de medicamentos, consultas e cuidados preventivos, garantindo que o usuário mantenha a adesão correta ao seu tratamento.

---

## 📱 Sobre o Projeto

O projeto nasceu da necessidade de reduzir o esquecimento de doses de medicamentos e facilitar a organização da rotina de saúde. Através de uma interface limpa e intuitiva, o usuário pode monitorar seu tratamento diário de forma segura e privada.

---

## 🎯 Funcionalidades Atuais (MVP)

### 🩺 Gestão de Medicamentos
* **Cadastro em Etapas:** Fluxo guiado para coletar Nome, Frequência, Horário e Tipo de Tratamento.
* **Dashboard Diário:** Lista de medicamentos do dia com status visual (Pendente/Tomado).
* **Histórico de Uso:** Registro automático de data e hora sempre que uma dose é confirmada.

### 📅 Agenda de Saúde
* **Consultas Médicas:** Agendamento de consultas com especialidade, médico, local, data e hora.
* **Lembretes Gerais:** Criação de lembretes personalizados para outros cuidados (ex: beber água, exames).

### 🔔 Notificações Inteligentes
* **Alarmes Automáticos:** O sistema agenda notificações no dispositivo para cada remédio ou compromisso cadastrado.
* **Lembretes em Tempo Real:** Receba alertas mesmo com o aplicativo fechado.

### 🔐 Segurança e LGPD
* **Consentimento Digital:** Fluxo inicial de aceite dos termos de uso e política de privacidade.
* **Privacy by Design:** 100% dos dados são armazenados localmente via **SQLite**.
* **Coleta Mínima:** Apenas dados essenciais (apelido, ano de nascimento) são solicitados para personalização.

---

## 🧠 Tecnologias Utilizadas

* **Linguagem:** Java
* **IDE:** Android Studio
* **Banco de Dados:** SQLite (Persistência Local)
* **Design:** Material Design 3 e XML Layouts
* **Agendamento:** AlarmManager & BroadcastReceivers
* **Versão Mínima:** Android 8.0 (API 26)

---

## 🚀 Como Executar o Projeto

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/ranielsilva/app-hora_certa.git
   ```
2. **Abra no Android Studio:** Importe o projeto e aguarde a sincronização do Gradle.
3. **Execute:** Utilize um emulador ou dispositivo físico (Android 8.0+).

---

## 📂 Estrutura de Documentação
Para detalhes mais profundos, consulte os arquivos na raiz do projeto:
* [📄 Documentação Técnica](./DOCUMENTACAO_TECNICA.md)
* [📖 Manual do Usuário](./MANUAL_DO_USUARIO.md)

---

## 🧪 Status do Projeto

✅ **MVP Finalizado:** As funcionalidades de cadastro, persistência local e notificações estão operacionais.

---

## 👨‍💻 Autores

Desenvolvido por **Raniel Silva** e **Thiago Cupertino**.

---

## 📌 Observações
Este projeto foi desenvolvido para fins acadêmicos, com foco em aprendizado e aplicação de conceitos de desenvolvimento mobile e saúde digital.
